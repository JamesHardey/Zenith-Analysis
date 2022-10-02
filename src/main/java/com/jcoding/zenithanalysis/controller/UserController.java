package com.jcoding.zenithanalysis.controller;

import com.jcoding.zenithanalysis.dto.UploadDto;
import com.jcoding.zenithanalysis.dto.contact.ContactUsDto;
import com.jcoding.zenithanalysis.dto.event.EventCard;
import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.dto.Message;
import com.jcoding.zenithanalysis.dto.UserAssignmentDto;
import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.entity.Assignment;
import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.entity.Uploads;
import com.jcoding.zenithanalysis.services.AppUserServices;
import com.jcoding.zenithanalysis.utils.ConstantPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/home")
public class UserController {

    @Autowired
    private AppUserServices appUserServices;

    @GetMapping
    public String getHome(Model model){
        return findByPage(model, 1);
    }


    @GetMapping("/activities/{pageNumber}")
    public String findByPage(Model model, @PathVariable int pageNumber){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        if(user.getUser().getRole().getName().equals("ADMIN")) return "redirect:/admin";
        if(checkIfApproved(user.getUser())) {
            String fullName = user.getUser().getName();
            Page<Uploads>  activities = appUserServices.getActivities(authentication,pageNumber);
            Page<UserAssignmentDto> assignment = appUserServices.getAssignmentByPage(authentication,1);
            model.addAttribute("name", fullName);
            model.addAttribute("currentAssPage", 1);
            model.addAttribute("currentActPage", pageNumber);
            model.addAttribute("coursesAssignments",assignment.getContent());
            model.addAttribute("events",appUserServices.getLatestEvents(2));
            model.addAttribute("noActivity",activities.getContent().size() < 1);
            model.addAttribute("noAssignments",assignment.getContent().size() < 1);
            model.addAttribute("activities", activities.getContent());
            model.addAttribute("isEmpty",(assignment.isEmpty()));
            model.addAttribute("totalPages", activities.getTotalPages());
            model.addAttribute("totalAssPage", assignment.getTotalPages());
            return ConstantPages.USER_NEW_HOME_PAGE;
        }

        return "redirect:/home/approval";
    }


    @GetMapping("/all-assignments/{pageNumber}")
    public String findAssignmentByPage(Model model,
                                       @PathVariable int pageNumber
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        if(user.getUser().getRole().equals("ADMIN")) return "redirect:/admin";
        if(checkIfApproved(user.getUser())) {
            String fullName = user.getUser().getName();
            Page<Uploads>  activities = appUserServices.getActivities(authentication,1);
            Page<UserAssignmentDto> assignment = appUserServices.getAssignmentByPage(authentication,pageNumber);
            model.addAttribute("name", fullName);
            model.addAttribute("currentAssPage", pageNumber);
            model.addAttribute("currentActPage", 1);
            model.addAttribute("coursesAssignments",assignment.getContent());
            model.addAttribute("activities", activities.getContent());
            model.addAttribute("isEmpty",(assignment.isEmpty()));
            model.addAttribute("totalPages", activities.getTotalPages());
            model.addAttribute("totalAssPage", assignment.getTotalPages());
            return ConstantPages.USER_NEW_HOME_PAGE;
        }

        return "redirect:/home/approval";
    }


    @GetMapping("/users")
    public String getUserHome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        if(checkIfApproved(user.getUser())) {
            List<Course> courses = appUserServices.getCourses(authentication);
            List<UserAssignmentDto> assignment = appUserServices.getAssignments(authentication);
            String fullName = user.getUser().getName();
            model.addAttribute("",fullName);
            model.addAttribute("",courses);
            model.addAttribute("assignments", assignment);
            model.addAttribute("isEmpty",(assignment.size() < 1));
            return ConstantPages.USER_NEW_HOME_PAGE;
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
        return getUserEventsByPage(model, 1);
    }


    @GetMapping("/events/{pageNumber}")
    public String getUserEventsByPage(Model model, @PathVariable int pageNumber){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        Page<EventCard> eventCards = appUserServices.getEventsCard(pageNumber);
        model.addAttribute("modelCards", eventCards.getContent());
        model.addAttribute("eventPages",eventCards.getTotalPages());
        model.addAttribute("currentActPage",pageNumber);
        return ConstantPages.USER_EVENT_PAGE;
    }


    @GetMapping("/courses")
    public String getCourses(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        model.addAttribute("courses", appUserServices.getAllCourses());
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

    @GetMapping("/ga")
    @ResponseBody
    private ResponseEntity getUploadBody(){
        return ResponseEntity.ok()
                .build();
    }
}
