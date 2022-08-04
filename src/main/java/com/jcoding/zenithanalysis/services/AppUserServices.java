package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.dto.*;
import com.jcoding.zenithanalysis.dto.event.EventCard;
import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.entity.Assignment;
import com.jcoding.zenithanalysis.entity.Course;
import com.jcoding.zenithanalysis.entity.RegisterCourse;
import com.jcoding.zenithanalysis.repository.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppUserServices{

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private RegisterCourseRepo registerCourseRepo;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EventsRepo eventsRepo;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private RolesRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ZenithEmailSenderServices zenithEmailSenderServices;

    @Value("${admin_email}")
    private String adminEmail;


//    public void forgetPassword(AppUser appUser){
//        Optional<AppUser> student = appUserRepo.findByEmail(appUser.getEmail());
//
//        if(student.isEmpty()) return;
//        try {
//            zenithEmailSenderServices.sendEmail("Forget Password", appUser.getPassword(),appUser.getEmail() );
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }


    public void sendAlertMessage(){
        try {
            zenithEmailSenderServices.sendEmail(
                    "My Good name",
                    "What are you up to",
                    "jamesade646@gmail.com"
            );
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



    public Long createUser(RegisterUser registerUser){
        AppUser newStudent = new AppUser();
        if(!registerUser.getPassword().equals(registerUser.getConfirmPassword())){
            return 0L;
        }
        Optional<AppUser> student = appUserRepo.findByEmail(newStudent.getEmail());
        if(student.isPresent()){
            return 0L;
        }
        newStudent.setName(registerUser.getName());
        newStudent.setEmail(registerUser.getEmail().toLowerCase());
        newStudent.setRole(repo.findByName("USER"));
        newStudent.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        newStudent.setVerification(generateCode());
        newStudent.setEnabled(false);
        appUserRepo.save(newStudent);
        String body = "This is your code "+newStudent.getVerification();
        try {
            zenithEmailSenderServices.sendEmail("Verification", body, newStudent.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Message Sent ");
        return newStudent.getId();
    }



    private String generateCode(){
        return RandomString.make(6);
    }



    public boolean findIfExist(String email){
        return appUserRepo.findByEmail(email.toLowerCase()).isPresent();
    }


    public boolean ifNotVerified(String email){
        return !appUserRepo.findByEmail(email.toLowerCase()).get().isEnabled();
    }


    public RegisterCourse createCourseRegistration(AppUser appUser, Course course){
        RegisterCourse registerCourse = new RegisterCourse();
        registerCourse.setUser(appUser);
        registerCourse.setCourse(course);
        registerCourse.setRegisteredDate(LocalDateTime.now().toString());
        registerCourseRepo.save(registerCourse);
        return registerCourse;
    }

    public void resendVerification(Long id){
        Optional<AppUser> student = appUserRepo.findById(id);
        if(student.isEmpty()) return;
        AppUser user = student.get();
        user.setVerification(generateCode());
        appUserRepo.save(user);
        String body = "This is your code "+user.getVerification();

        try {
            zenithEmailSenderServices.sendEmail("Verification", body, user.getEmail());
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void resendVerification(String email){
        Optional<AppUser> student = appUserRepo.findByEmail(email.toLowerCase());
        if(student.isEmpty()) return;
        AppUser user = student.get();
        user.setVerification(generateCode());
        appUserRepo.save(user);
        String body = "This is your code "+user.getVerification();

        try {
            zenithEmailSenderServices.sendEmail("Verification", body, email);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public AppUser getUserByEmail(String email){
        return appUserRepo.findByEmail(email.toLowerCase()).get();
    }

    public AppUser getUserByEmail(Long id){
        return appUserRepo.findById(id).get();
    }


    public boolean verify(VerifyUser appUser){
        Optional<AppUser> user = appUserRepo.findById(appUser.getId());
        if(user.isEmpty()) return false;
        if(user.get().getVerification().equalsIgnoreCase(appUser.getCode())){
            user.get().setVerification(null);
            user.get().setEnabled(true);
            return true;
        }
        return false;
    }


    public List<Course> getCourses(Authentication authentication){
        CustomAppUser appUser = (CustomAppUser) authentication.getPrincipal();
        List<RegisterCourse> registerCourseList=registerCourseRepo.findAllByUser(appUser.getUser());
        if(registerCourseList == null || registerCourseList.isEmpty()) return List.of();

        List<Course> courses = new ArrayList<>();

        for(RegisterCourse registerCourse: registerCourseList){
            courses.add(registerCourse.getCourse());
        }

        return courses;
    }



    public List<UserAssignmentDto> getAssignments(Authentication authentication){
        CustomAppUser appUser = (CustomAppUser) authentication.getPrincipal();
        Optional<AppUser> student = appUserRepo.findByEmail(appUser.getUser().getEmail());
        if(student.isEmpty()) return null;
        List<RegisterCourse> registerCourseList = registerCourseRepo.findAllByUser(appUser.getUser());

        if(registerCourseList.size() < 1) return List.of();

        List<UserAssignmentDto> assignmentList = new ArrayList<>();
        int index = 0;
        for(RegisterCourse registerCourse: registerCourseList){
            List<Assignment> assignments = assignmentRepo.findAllByCourse(registerCourse.getCourse());
            for(Assignment ass: assignments){
                index++;
                assignmentList.add(new UserAssignmentDto(
                        index,ass.getTitle(),ass.getInstructions(),ass.getUploadDate(),ass.getSubmissionDate()
                ));
            }
        }

        return assignmentList;
    }


    public void contactAdmin(ContactUsDto contactUsDto){
        String subject = "Contact Zenith-Analysis";
        String body = contactUsDto.getMessage()+"\n\n\n"
                +"Message from: \n"
                +"Name: "+contactUsDto.getName()+"\n"
                +"Phone: "+contactUsDto.getPhone()+"\n"
                +"Email: "+contactUsDto.getEmail()+"\n"
                +"Time sent: "+contactUsDto.getDateSent()+"\n";

        try {
            zenithEmailSenderServices.sendEmail(subject,body,adminEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public boolean verifyPassword(PasswordChange passwordChange, Long id){
        Optional<AppUser> student = appUserRepo.findById(id);
        System.out.println(passwordChange.getNewPassword());
        System.out.println(passwordChange.getConfirmPassword());
        if(!(passwordChange.getNewPassword()
                .equals(passwordChange.getConfirmPassword()))
                || student.isEmpty()
        ) return false;

        AppUser user = student.get();
        String newPassword = passwordEncoder.encode(passwordChange.getNewPassword());
        String resetCode = passwordChange.getResetPassword();
        System.out.println(user.getPassword());
        System.out.println(resetCode);
        if(resetCode.equals(user.getVerification())){
            user.setPassword(newPassword);
            user.setEnabled(true);
            user.setVerification(null);
            appUserRepo.save(user);
            return true;
        }
        return false;
    }

    public void sendRecoveryPassword(String email){
        Optional<AppUser> student = appUserRepo.findByEmail(email.toLowerCase());
        String code = generateCode();
        if(student.isEmpty()) return;

        AppUser user = student.get();
        user.setVerification(code);
        System.out.println("The new recovery password is "+user.getPassword());
        user.setEnabled(false);
        appUserRepo.save(user);

        String body = "This is your recovery password : <"+code+">\n" +
                "/n" +
                "";
        String subject = "Zenith-Analysis Password recovery";
        try {
            zenithEmailSenderServices.sendEmail(subject,body,email);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public List<EventCard> getEventsCard(){
        return eventsRepo.findAll()
                .stream()
                .map((event)-> {
                    EventCard event1 = new EventCard();
                    event1.setTitle(event.getTitle());
                    event1.setDetails(event.getDetails());
                    LocalDate date = LocalDate.parse(event.getDate());
                    event1.setMonth(date.getMonth().toString().substring(0,3));
                    event1.setDay(Integer.toString(date.getDayOfMonth()));
                    event1.setYear(Integer.toString(date.getYear()));
                    event1.setTime(LocalTime.parse(event.getTime()));
                    return event1;
                })
                .collect(Collectors.toList());
    }

}

