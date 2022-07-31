package com.jcoding.zenithanalysis.controller;

import com.jcoding.zenithanalysis.dto.UserAssignmentDto;
import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.services.AppUserServices;
import com.jcoding.zenithanalysis.utils.ConstantPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class UserController {

    @Autowired
    private AppUserServices appUserServices;

    @GetMapping
    public String getHome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Course> courses = appUserServices.getCourses(authentication);
        model.addAttribute("courses", courses);
        return ConstantPages.USER_HOME_PAGE;
    }

    @GetMapping("/assignment")
    public String getAssignmentPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UserAssignmentDto> assignmentDtos = appUserServices.getAssignments(authentication);
        boolean isEmpty = assignmentDtos.isEmpty();
        model.addAttribute("isEmpty", isEmpty);
        model.addAttribute("assignments",assignmentDtos);
        return ConstantPages.USER_ASSIGNMENT_PAGE;
    }

    @GetMapping("/events")
    public String getUserEvents(){
        return ConstantPages.USER_EVENT_PAGE;
    }

    @GetMapping("/courses")
    public String getCourses(){
        return ConstantPages.USER_COURSE_PAGE;
    }

    @GetMapping("/contact-us")
    public String contactUsPage(){
        return ConstantPages.USER_CONTACT_PAGE;
    }
}
