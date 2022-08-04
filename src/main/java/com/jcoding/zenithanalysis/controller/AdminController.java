package com.jcoding.zenithanalysis.controller;


import com.jcoding.zenithanalysis.dto.*;
import com.jcoding.zenithanalysis.dto.user.NewAdminDto;
import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.services.AdminServices;
import com.jcoding.zenithanalysis.utils.ConstantPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        model.addAttribute("eventNumber",adminServices.getEvents().size());
        model.addAttribute("courses",adminServices.getAllRegisteredCourses().stream().limit(10).collect(Collectors.toList()));
        model.addAttribute("appUsers",adminServices.getAllUser().stream().limit(10).collect(Collectors.toList()));
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_HOME_PAGE;
    }



    /* Course Mapping*/
    @GetMapping("/courses")
    public String getCourses(Model model){
        model.addAttribute("courses",adminServices.getAllRegisteredCourses());
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_COURSES_PAGE;
    }

    @GetMapping("/add-course")
    public String addCourse(Model model){
        CoursesDto coursesDto = new CoursesDto();
        model.addAttribute("course",coursesDto);
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_ADD_COURSE;
    }

    @PostMapping("/add-course")
    public String saveCourse(
            @ModelAttribute("course") CoursesDto coursesDto){
        adminServices.addCourse(coursesDto);
        return "redirect:/admin/add-course?success";
    }

    @GetMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long courseId){
        adminServices.deleteCourse(courseId);
        return "redirect:/admin/courses";
    }

    /* End of Mapping */




    /* User Mapping*/

    @GetMapping("/users")
    public String getUsers(Model model){
        model.addAttribute("users",adminServices.getAllUser());
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_USERS_PAGE;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        adminServices.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/approve-user/{id}")
    public String approveUser(@PathVariable("id") Long id){
        adminServices.approveUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/course-allow/{id}")
    public String getAllCourseStatusWithUser(
            @PathVariable("id") Long userId, Model model){
        List<AllowedCourses> courses = adminServices.getAllCourseAndStatusByUserId(userId);
        model.addAttribute("courses", courses);
        model.addAttribute("id",userId);
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_EDIT_USER;
    }

    @GetMapping("/allow-course/{courseId}/{id}")
    public String allowCourse(
            @PathVariable("courseId") Long courseIds,
            @PathVariable("id") Long id
    ){
        adminServices.allowCourse(id,courseIds);
        return "redirect:/admin/course-allow/"+id;
    }

    /* End of Mapping */





    /* Assignment Mapping */

    @GetMapping("/assignments")
    public String getAssignments(Model model){
        model.addAttribute("assignments", adminServices.getAllAssignment());
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_ASSIGNMENT_PAGE;
    }

    @GetMapping("/add-assignment")
    public String addAssignment(Model model){
        AssignDto assignDto = new AssignDto();
        model.addAttribute("assign",assignDto);
        model.addAttribute("courses", adminServices.getListOfCourses());
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_ADD_ASSIGNMENT;
    }

    @PostMapping("/add-assignment")
    public String postAssignment(@ModelAttribute("assign") AssignDto assignDto){
        if(adminServices.addAssignment(assignDto)) return "redirect:/admin/add-assignment?success";
        return "redirect:/admin/add-assignment?error";
    }

    @GetMapping("/assignment/delete/{id}")
    public String deleteAssignment(@PathVariable("id") Long id){
        adminServices.deleteAssignment(id);
        return "redirect:/admin/assignments";
    }

    /* Events Mapping*/

    @GetMapping("/events")
    public String getEvents(Model model){
        model.addAttribute("events", adminServices.getEvents());
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_EVENT_PAGE;
    }

    @GetMapping("/add-event")
    public String addEvent(Model model){
        EventsDto event = new EventsDto();
        model.addAttribute("event",event);
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_ADD_EVENT;
    }

    @PostMapping("/add-event")
    public String addEventToDatabase(@ModelAttribute("event") EventsDto event){
        if(adminServices.addEvents(event)) return "redirect:/admin/add-event?success";
        return "redirect:/admin/add-event?error";
    }

    @GetMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id){
        adminServices.deleteEvent(id);
        return "redirect:/admin/events";
    }

    /* End of Mapping */




    /* Upload Mapping */

    @GetMapping("/uploads")
    public String getUploads(Model model){
        model.addAttribute("uploads", adminServices.getUploads());
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_UPLOAD_PAGE;
    }

    @GetMapping("/add-upload")
    public String addUpload(Model model){
        UploadDto uploadDto = new UploadDto();
        model.addAttribute("uploadDto",uploadDto);
        model.addAttribute("courses",adminServices.getListOfCourses());
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_ADD_UPLOAD;
    }

    @PostMapping("/add-upload")
    public String uploadLink(@ModelAttribute("uploadDto") UploadDto uploadDto){
        if(adminServices.addUpload(uploadDto)) return "redirect:/admin/add-upload?success";
        return "redirect:/admin/add-upload?failed";
    }

    @GetMapping("/upload/delete/{id}")
    public String deleteUpload(@PathVariable("id") Long id){
        adminServices.deleteUpload(id);
        return "redirect:/admin/uploads";
    }



    /* Admin Mapping*/

    @GetMapping("/add-admin")
    public String createNewAdmin(Model model){
        NewAdminDto adminDto = new NewAdminDto();
        model.addAttribute("adminDto", adminDto);
        model.addAttribute("name",adminServices.getDisplayName());
        return ConstantPages.ADMIN_ADD_ADMIN;
    }

    @PostMapping("/add-admin")
    public String addNewAdmin(@ModelAttribute("adminDto") NewAdminDto adminDto){
        if(adminServices.findUserByEmail(adminDto.getEmail()))
            return "redirect:/admin/add-admin?invalid";

        if(!adminServices.confirmAdminPassword(adminDto.getAdminPassword()))
            return "redirect:/admin/add-admin?error";

        adminServices.addNewAdmin(adminDto);
        return "redirect:/admin/add-admin?success";
    }




}
