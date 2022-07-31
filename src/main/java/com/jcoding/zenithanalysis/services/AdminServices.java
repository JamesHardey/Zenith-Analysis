package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.dto.RegisteredCourse;
import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.entity.Assignment;
import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.entity.RegisterCourse;
import com.jcoding.zenithanalysis.repository.AppUserRepo;
import com.jcoding.zenithanalysis.repository.AssignmentRepo;
import com.jcoding.zenithanalysis.repository.CourseRepository;
import com.jcoding.zenithanalysis.repository.RegisterCourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminServices {

    @Autowired
    private ZenithEmailSenderServices zenithEmailSenderServices;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegisterCourseRepo registerCourseRepo;

    @Autowired
    private AppUserRepo appUserRepo;


    public void uploadAssignment(String title, String instructions, Course course){
        Optional<Course> course1 = courseRepository.findById(course.getId());
        if(course1.isEmpty()){
            throw new RuntimeException("Course not available");
        }
//        Assignment assignment = new Assignment(
//                title,instructions,course,
//        );
//        assignmentRepo.save(assignment);
    }


    public void sendClassVideoLink(String body, Course course){
        List<RegisterCourse> list = registerCourseRepo.findAllByCourse(course);
        List<String> emails = new ArrayList<>();

        for(RegisterCourse registerCourse: list){
           emails.add(registerCourse.getUser().getEmail());
        }
        try {
            zenithEmailSenderServices.sendEmail("Link to Class Video", body, String.valueOf(emails));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void deleteUser(AppUser appUser){
        appUserRepo.delete(appUser);
    }

    public void deleteAssignment(Assignment assignment){
        assignmentRepo.delete(assignment);
    }

    public void addAssignment(Assignment assignment){
        assignmentRepo.save(assignment);
    }

    public void deleteCourse(Course course){
        courseRepository.delete(course);
    }

    public Course addCourse(Course course){
        return courseRepository.save(course);
    }

    public void deleteRegisterCourse(RegisterCourse registerCourse){
        registerCourseRepo.delete(registerCourse);
    }

    public RegisterCourse addCourseRegister(RegisterCourse registerCourse){
        return registerCourseRepo.save(registerCourse);
    }

    public List<Course> getListOfCourses(){
        return courseRepository.findAll();
    }

    public List<RegisteredCourse> getAllRegisteredCourses(){
        List<Course> courses = courseRepository.findAll();
        List<RegisterCourse> registerCourseList = registerCourseRepo.findAll();

        List<RegisteredCourse> registeredCourses = new ArrayList<>();

        for(Course course: courses){
            int count = 0;
            for(RegisterCourse registerCourse: registerCourseList){
                if(registerCourse.getCourse().getId().equals(course.getId())){
                    count++;
                }

            }
            registeredCourses.add(new RegisteredCourse(course.getCourseTitle(),count));
        }
        return registeredCourses;
    }

    public List<Assignment> getAllAssignment(){
        return assignmentRepo.findAll();
    }

    public List<AppUser> getAllUser(){
        return appUserRepo.findAll();
    }

}
