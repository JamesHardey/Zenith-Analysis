package com.jcoding.zenithanalysis.controller;

import com.jcoding.zenithanalysis.dto.contact.ContactUsDto;
import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.dto.Message;
import com.jcoding.zenithanalysis.dto.UserAssignmentDto;
import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.entity.Assignment;
import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.services.AppUserServices;
import com.jcoding.zenithanalysis.utils.ConstantPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/home")
public class UserController {

    @Autowired
    private AppUserServices appUserServices;

    @GetMapping
    public String getHome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        if(checkIfApproved(user.getUser())) {
            List<Course> courses = appUserServices.getCourses(authentication);
            String fullName = user.getUser().getName();
            String firstName = fullName.substring(0, fullName.indexOf(" "));
            List<UserAssignmentDto> assignment = appUserServices.getAssignments(authentication);
            model.addAttribute("name", fullName);
            model.addAttribute("courses", courses);
            model.addAttribute("assignments", assignment);
            model.addAttribute("isEmpty",(assignment.size() < 1));
            return ConstantPages.USER_HOME_PAGE;
        }
        return "redirect:/home/approval";
    }

    @GetMapping("/approval")
    public String getApproval(Model model){
        String msg = "You have not been approved for any course, please contact the Admin for that";
        Message message = new Message();
        message.setMessage(msg);
        model.addAttribute("message", message);
        return ConstantPages.APPROVAL_SUPPORT_PAGE;
    }
    

    @GetMapping("/assignment")
    public String getAssignmentPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        if(!checkIfApproved(user.getUser())) {
            return "redirect:/home/approval";
        }
        List<UserAssignmentDto> assignmentDtos = appUserServices.getAssignments(authentication);
        boolean isEmpty = assignmentDtos.isEmpty();
        model.addAttribute("isEmpty", isEmpty);
        model.addAttribute("assignments",assignmentDtos);
        return "redirect:/home";
    }


    @GetMapping("/events")
    public String getUserEvents(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        model.addAttribute("modelCards", appUserServices.getEventsCard());
        return ConstantPages.USER_EVENT_PAGE;
    }


    @GetMapping("/courses")
    public String getCourses(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        return ConstantPages.USER_COURSE_PAGE;
    }


    @GetMapping("/contact-us")
    public String contactUsPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        ContactUsDto contactUsDto = new ContactUsDto();
        model.addAttribute("contactDetail",contactUsDto);
        return ConstantPages.USER_CONTACT_PAGE;
    }


    @PostMapping("/contact-us")
    public String sendMessage(
            @ModelAttribute("contactDetail") ContactUsDto contactUsDto){
        contactUsDto.setDateSent(LocalDateTime.now().toString());
        appUserServices.contactAdmin(contactUsDto);
        return "redirect:/home/contact-us?sent";
    }

    private boolean checkIfApproved(AppUser appUser){
        return appUser.isApproved();
    }
}
