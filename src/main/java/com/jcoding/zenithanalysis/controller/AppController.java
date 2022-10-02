package com.jcoding.zenithanalysis.controller;


import com.jcoding.zenithanalysis.dto.contact.ContactUsDto;
import com.jcoding.zenithanalysis.dto.event.EventCard;
import com.jcoding.zenithanalysis.dto.user.*;
import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.services.AdminServices;
import com.jcoding.zenithanalysis.services.AppUserServices;
import com.jcoding.zenithanalysis.utils.ConstantPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/")
public class AppController {

    @Autowired
    private AppUserServices  appUserServices;

    @Autowired
    private AdminServices adminServices;


    @GetMapping
    public String home(Authentication authentication, Model model){
        if(authentication != null && authentication.isAuthenticated()){
            CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
            return (user
                    .getUser()
                    .getRole()
                    .getName()
                    .equals("ADMIN"))?
                    "redirect:/admin":"redirect:/home";
        }
        model.addAttribute("latestEvent",appUserServices.getLatestEvents(1));
        return ConstantPages.HOME_PAGE;
    }



    @GetMapping("/enroll")
    public String enrollCourse(Authentication authentication){
        return (
                authentication != null
                        && authentication.isAuthenticated()
        ) ? "redirect:/home/contact-us": "redirect:/register";
    }



    @GetMapping("/about")
    public String getAbout(Authentication authentication){
        return ConstantPages.ABOUT_PAGE;
    }


    @GetMapping("/contact-us")
    public String getContact(Authentication authentication, Model model){

        if(authentication != null && authentication.isAuthenticated()){
            return "redirect:/home/contact-us";
        }
        ContactUsDto contactUsDto = new ContactUsDto();
        model.addAttribute("contactDetail",contactUsDto);
        return ConstantPages.CONTACT_PAGE;
    }

    @PostMapping("/contact-us")
    public String sendMessage(
            @ModelAttribute("contactDetail") ContactUsDto contactUsDto){
        contactUsDto.setDateSent(LocalDateTime.now().toString());
        appUserServices.contactAdmin(contactUsDto);
        return "redirect:/contact-us?sent";
    }


    @GetMapping("/events")
    public String getEventPage(Authentication authentication, Model model){
        return getEventByPage(authentication, model, 1);
    }


    @GetMapping("/events/{pageNumber}")
    public String getEventByPage(Authentication authentication, Model model, @PathVariable int pageNumber){
        if(authentication != null && authentication.isAuthenticated()){
            return "redirect:/home/events";
        }
        Page<EventCard> eventCards = appUserServices.getEventsCard(pageNumber);
        model.addAttribute("modelCards", eventCards.getContent());
        model.addAttribute("eventPages",eventCards.getTotalPages());
        model.addAttribute("currentActPage",pageNumber);
        System.out.println("Pages "+eventCards.getTotalPages());
        return ConstantPages.EVENT_PAGE;
    }



    @GetMapping("/courses")
    public String getCourses(Authentication authentication, Model model){
        if(authentication != null && authentication.isAuthenticated()){
            return "redirect:/home/courses";
        }
        model.addAttribute("courses", appUserServices.getAllCourses());
        return ConstantPages.COURSE_PAGE;
    }


    @GetMapping("/login")
    public String getLoginPage(Model model){
        LoginUser newStudent = new LoginUser();
        model.addAttribute("loginUser", newStudent);
        return ConstantPages.LOGIN_PAGE;
    }




    @GetMapping("/register")
    public String getRegisterPage(Model model){
        RegisterUser newStudent = new RegisterUser();
        model.addAttribute("newStudent", newStudent);
        return ConstantPages.REGISTER_PAGE;
    }




    @PostMapping("/register")
    public String getRegisterPage(@ModelAttribute("newStudent") RegisterUser registerUser){
        String email = registerUser.getEmail().toLowerCase();

        if(appUserServices.findIfExist(email)
                && appUserServices.ifNotVerified(email)){
            appUserServices.resendVerification(email);
            Long id = appUserServices.getUserByEmail(email).getId();
            return "redirect:/verify?id="+id;
        }
        if(appUserServices.findIfExist(email)){
            return "redirect:/register?error";
        }
        if(!(registerUser.getPassword().equals(registerUser.getConfirmPassword()))){
            return "redirect:/register?password_error";
        }
        Long id = appUserServices.createUser(registerUser);
        return "redirect:/verify?id="+id;
    }



    @GetMapping("/verify")
    public String getVerificationPage(@Param("id")Long id, Model model){
        if(id == null){
            return "redirect:/error";
        }
        VerifyUser appUser = new VerifyUser();
        appUser.setId(id);
        model.addAttribute("user",appUser);
        return ConstantPages.VERIFICATION_PAGE;
    }


    @PostMapping("/verify/{id}")
    public String doVerification(
            @ModelAttribute("user") VerifyUser appUser,
            @PathVariable("id") Long id){
        appUser.setId(id);
        if(appUserServices.verify(appUser)){
            return "redirect:/login";
        }
        return "redirect:/verify?id="+id+"&invalid";
    }


    @GetMapping("/resend/{id}")
    public String verification(@PathVariable("id") Long id){
        appUserServices.resendVerification(id);
        return "redirect:/verify?id="+id;
    }


    @GetMapping("/forget-pass")
    public String forgetPassword(Model model){
        EmailParser email = new EmailParser();
        model.addAttribute("myEmail",email);
        return ConstantPages.PASSWORD_RECOVERY_PAGE;
    }


    @PostMapping("/forget-pass")
    public String changePassword(
            @ModelAttribute("myEmail") EmailParser email
    ){
        if(email == null || email.getEmail().isEmpty()) return "redirect:/forget-pass";
        AppUser appUser = appUserServices.getUserByEmail(email.getEmail());
        if(appUser == null) return "redirect:/forget-pass?invalid";
        Long id = appUser.getId();
        appUserServices.sendRecoveryPassword(email.getEmail());
        return "redirect:/change-pass?id="+id;
    }


    @GetMapping("/change-pass")
    public String changePassword(@Param("id")Long id, Model model){
        PasswordChange passwordChange = new PasswordChange();
        model.addAttribute("passwordChange", passwordChange);
        return ConstantPages.CHANGE_PASS_PAGE;
    }


    @PostMapping("/change-pass/{id}")
    public String changePassword(
            @PathVariable("id") Long id,
            @ModelAttribute("passwordChange") PasswordChange passwordChange
    ){
        if(id == null || passwordChange == null) return "redirect:/forget-pass";

        if(appUserServices.verifyPassword(passwordChange,id))
            return "redirect:/login";
        else
            return "redirect:/forget-pass?invalid_verification";
    }


    @GetMapping("/process_login")
    public String processLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AppController:role "+authentication.getAuthorities().toString());
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))){
            return "redirect:/admin";
        }
        else if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER"))){
            return "redirect:/home";
        }
        else return "redirect:/";
    }


//    @GetMapping("/error")
//    public String getErrorPage(){
//        return "error";
//    }

}
