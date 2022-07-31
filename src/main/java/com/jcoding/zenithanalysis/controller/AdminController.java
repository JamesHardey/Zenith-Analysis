package com.jcoding.zenithanalysis.controller;


import com.jcoding.zenithanalysis.services.AdminServices;
import com.jcoding.zenithanalysis.utils.ConstantPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminServices adminServices;

    @GetMapping
    public String getHome(Model model){
        model.addAttribute("courseNumber",adminServices.getListOfCourses().size());
        model.addAttribute("assignmentNumber",adminServices.getAllAssignment().size());
        model.addAttribute("usersNumber",adminServices.getAllUser().size());
        model.addAttribute("courses",adminServices.getAllRegisteredCourses().stream().limit(10).collect(Collectors.toList()));
        model.addAttribute("appUsers",adminServices.getAllUser().stream().limit(10).collect(Collectors.toList()));

        return ConstantPages.ADMIN_HOME_PAGE;
    }

    @GetMapping("/courses")
    public String getCourses(Model model){
        model.addAttribute("courses",adminServices.getAllRegisteredCourses());
        return ConstantPages.ADMIN_COURSES_PAGE;
    }

    @GetMapping("/users")
    public String getUsers(Model model){
        model.addAttribute("users",adminServices.getAllUser().stream());
        return ConstantPages.ADMIN_USERS_PAGE;
    }

    @GetMapping("/assignments")
    public String getAssignments(){
        return ConstantPages.ADMIN_ASSIGNMENT_PAGE;
    }

    @GetMapping("/events")
    public String getEvents(){

        return ConstantPages.ADMIN_EVENT_PAGE;
    }

    @GetMapping("/uploads")
    public String getUploads(){
        return ConstantPages.ADMIN_UPLOAD_PAGE;
    }

    @GetMapping("/add-assignment")
    public String addAssignment(){
        return ConstantPages.ADMIN_ADD_ASSIGNMENT;
    }

    @GetMapping("/add-course")
    public String addCourse(){
        return ConstantPages.ADMIN_ADD_COURSE;
    }

    @GetMapping("/add-event")
    public String addEvent(){
        return ConstantPages.ADMIN_ADD_EVENT;
    }

    @GetMapping("/add-upload")
    public String addUpload(){
        return ConstantPages.ADMIN_ADD_UPLOAD;
    }



}
