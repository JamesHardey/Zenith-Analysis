package com.jcoding.zenithanalysis.controller;


import com.jcoding.zenithanalysis.dto.*;
import com.jcoding.zenithanalysis.dto.assignment.AssignDto;
import com.jcoding.zenithanalysis.dto.course.AllowedCourses;
import com.jcoding.zenithanalysis.dto.course.CoursesDto;
import com.jcoding.zenithanalysis.dto.event.EventsDto;
import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.dto.user.NewAdminDto;
import com.jcoding.zenithanalysis.services.AdminServices;
import com.jcoding.zenithanalysis.utils.ConstantPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${resume_path}")
    private String resumePath;

    @Value("${cover_letter_path}")
    private String coverLetterPath;


    @Autowired
    private AdminServices adminServices;

    @GetMapping
    public String getHome(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        if(user.getUser().getRole().getName().equals("USER")) return "redirect:/home";
        model.addAttribute("courseNumber",adminServices.getListOfCourses().size());
        model.addAttribute("assignmentNumber",adminServices.getAllAssignment().size());
        model.addAttribute("usersNumber",adminServices.getAllUser().size());
        model.addAttribute("eventNumber",adminServices.getEvents().size());
        model.addAttribute("courses",adminServices.getAllRegisteredCourses().stream().limit(10).collect(Collectors.toList()));
        model.addAttribute("appUsers",adminServices.getAllUser().stream().limit(10).collect(Collectors.toList()));
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_HOME_PAGE;
    }




    /* Course Mapping*/

    @GetMapping("/courses")
    public String getCourses(Model model){
        model.addAttribute("courses",adminServices.getAllRegisteredCourses());
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_COURSES_PAGE;
    }

    @GetMapping("/add-course")
    public String addCourse(Model model){
        CoursesDto coursesDto = new CoursesDto();
        model.addAttribute("course",coursesDto);
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_ADD_COURSE;
    }

    @PostMapping("/add-course")
    public String saveCourse(
            @ModelAttribute("course") CoursesDto coursesDto,
            @RequestParam("file") MultipartFile file
            ){
        if(!adminServices.addCourse(coursesDto,file)) return "redirect:/admin/add-course?failed";
        return "redirect:/admin/courses";
    }

    @GetMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long courseId){
        adminServices.deleteCourse(courseId);
        return "redirect:/admin/courses";
    }

    @GetMapping("/course/{id}")
    @ResponseBody
    public CoursesDto getCourseById(@PathVariable("id") Long id){
        return adminServices.getCourseById(id);
    }

    @PostMapping("/course/save")
    public String updateCourse(CoursesDto coursesDto,
                               @RequestParam("file") MultipartFile file
                               ){
        if(coursesDto.getId() == null)
            return "redirect:/admin/courses?failed";
        if(!file.isEmpty()) coursesDto.setImageUrl(file.getOriginalFilename());
        adminServices.updateCourse(coursesDto, file);
        return "redirect:/admin/courses";
    }

    /* End of Mapping */




    /* User Mapping*/

    @GetMapping("/users")
    public String getUsers(Model model){
        model.addAttribute("users",adminServices.getAllUser());
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_USERS_PAGE;
    }

    @PostMapping("/delete/{id}")
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
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
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
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        model.addAttribute("courses", adminServices.getListOfCourses());
        return ConstantPages.ADMIN_ASSIGNMENT_PAGE;
    }

    @GetMapping("/add-assignment")
    public String addAssignment(Model model){
        AssignDto assignDto = new AssignDto();
        model.addAttribute("assign",assignDto);
        model.addAttribute("courses", adminServices.getListOfCourses());
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_ADD_ASSIGNMENT;
    }

    @PostMapping("/add-assignment")
    public String postAssignment(@ModelAttribute("assign") AssignDto assignDto){
        if(adminServices.addAssignment(assignDto)) return "redirect:/admin/assignments";
        return "redirect:/admin/add-assignment?error";
    }

    @GetMapping("/assignment/delete/{id}")
    public String deleteAssignment(@PathVariable("id") Long id){
        adminServices.deleteAssignment(id);
        return "redirect:/admin/assignments";
    }

    @GetMapping("/assignment/{id}")
    @ResponseBody
    public AssignDto getAssignmentById(@PathVariable("id") Long id){
        return adminServices.getAssignmentById(id);
    }

    @PostMapping("/assignment/save")
    public String updateAssignment(AssignDto assignDto){
        if(assignDto.getId() == null)
            return "redirect:/admin/assignments?failed";
        adminServices.updateAssignment(assignDto);
        return "redirect:/admin/assignments?success";
    }




    /* Events Mapping*/

    @GetMapping("/events")
    public String getEvents(Model model){
        model.addAttribute("events", adminServices.getEvents());
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_EVENT_PAGE;
    }

    @GetMapping("/add-event")
    public String addEvent(Model model){
        EventsDto event = new EventsDto();
        model.addAttribute("event",event);
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_ADD_EVENT;
    }

    @PostMapping("/add-event")
    public String addEventToDatabase(@ModelAttribute("event") EventsDto event){
        if(adminServices.addEvents(event)) return "redirect:/admin/events";
        return "redirect:/admin/add-event?error";
    }

    @GetMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id){
        adminServices.deleteEvent(id);
        return "redirect:/admin/events";
    }

    @GetMapping("/event/{id}")
    @ResponseBody
    public EventsDto getEventById(@PathVariable("id") Long id){
        return adminServices.getEventById(id);
    }

    @PostMapping("/event/save")
    public String updateEvent(EventsDto eventsDto){
        if(eventsDto.getId() == null)
            return "redirect:/admin/events?failed";
        adminServices.editEvent(eventsDto);
        return "redirect:/admin/events";
    }

    /* End of Mapping */



    /* Upload Mapping */

    @GetMapping("/uploads")
    public String getUploads(Model model){
        model.addAttribute("uploads", adminServices.getUploads());
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        model.addAttribute("courses", adminServices.getListOfCourses());
        return ConstantPages.ADMIN_UPLOAD_PAGE;
    }

    @GetMapping("/add-upload")
    public String addUpload(Model model){
        UploadDto uploadDto = new UploadDto();
        model.addAttribute("uploadDto",uploadDto);
        model.addAttribute("courses",adminServices.getListOfCourses());
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_ADD_UPLOAD;
    }

    @PostMapping("/add-upload")
    public String uploadLink(@ModelAttribute("uploadDto") UploadDto uploadDto){
        if(adminServices.addUpload(uploadDto)) return "redirect:/admin/uploads";
        return "redirect:/admin/add-upload?failed";
    }

    @GetMapping("/upload/delete/{id}")
    public String deleteUpload(@PathVariable("id") Long id){
        adminServices.deleteUpload(id);
        return "redirect:/admin/uploads";
    }

    @GetMapping("/upload/{id}")
    @ResponseBody
    public UploadDto getUploadsById(@PathVariable("id") Long id){
        return adminServices.getUploadById(id);
    }

    @PostMapping("/upload/save")
    public String updateUploadClass(UploadDto uploadDto){
        if(uploadDto.getId() == null)
            return "redirect:/admin/uploads?failed";

        adminServices.updateUploadClass(uploadDto);
        return "redirect:/admin/uploads";
    }


    /* Admin Mapping*/

    @GetMapping("/add-admin")
    public String createNewAdmin(Model model){
        NewAdminDto adminDto = new NewAdminDto();
        model.addAttribute("adminDto", adminDto);
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_ADD_ADMIN;
    }

    @PostMapping("/add-admin")
    public String addNewAdmin(@ModelAttribute("adminDto") NewAdminDto adminDto){
        if(adminServices.findUserByEmail(adminDto.getEmail()))
            return "redirect:/admin/add-admin?invalid";

        if(!adminServices.confirmAdminPassword(adminDto.getAdminPassword()))
            return "redirect:/admin/add-admin?error";

        adminServices.addNewAdmin(adminDto);
        return "redirect:/admin/users";
    }


    @GetMapping("/add-resume")
    public String addResume(Model model){
        model.addAttribute("resumeDto", new ResumeUploadDto());
        model.addAttribute("adminDisplay",adminServices.getDisplayDetails());
        return ConstantPages.ADMIN_UPLOAD_RESUME;
    }

    @PostMapping("/add-resume")
    public String uploadResume(
            @ModelAttribute("resumeDto") ResumeUploadDto resumeUploadDto,
            @RequestParam("file") MultipartFile file
    ){

        if( file!= null && file.getOriginalFilename().endsWith(".pdf")){
            String path = "";
            System.out.println(resumeUploadDto.getType());
            if(resumeUploadDto.getType().equals("Resume")){
                path = resumePath+"resume.pdf";
            }
            else path = coverLetterPath+"cover-letter.pdf";

            Path path1 = Paths.get(path);

            try {
                Files.deleteIfExists(path1);
                Files.createFile(path1);
                Files.write(path1, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/admin/add-resume?failed";
            }
            return "redirect:/admin/";
        }
        return "redirect:/admin/add-resume?failed";
    }


}
