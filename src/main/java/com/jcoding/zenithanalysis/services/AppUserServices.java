package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.dto.*;
import com.jcoding.zenithanalysis.dto.contact.ContactUsDto;
import com.jcoding.zenithanalysis.dto.course.CoursesDto;
import com.jcoding.zenithanalysis.dto.event.EventCard;
import com.jcoding.zenithanalysis.dto.event.EventsDto;
import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.dto.user.PasswordChange;
import com.jcoding.zenithanalysis.dto.user.RegisterUser;
import com.jcoding.zenithanalysis.dto.user.VerifyUser;
import com.jcoding.zenithanalysis.entity.*;
import com.jcoding.zenithanalysis.repository.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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
    private UploadRepo uploadRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ZenithEmailSenderServices zenithEmailSenderServices;

    @Value("${admin_email}")
    private String adminEmail;



//    public void sendAlertMessage(){
//        try {
//            zenithEmailSenderServices.sendEmail(
//                    "My Good name",
//                    "What are you up to",
//                    ["jamesade646@gmail.com"]
//            );
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }



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
            zenithEmailSenderServices.sendEmail("Verification", body, List.of(newStudent.getEmail()));
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
            zenithEmailSenderServices.sendEmail("Verification", body, List.of(user.getEmail()));
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
            zenithEmailSenderServices.sendEmail("Verification", body, List.of(email));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }




    public AppUser getUserByEmail(String email){
        if(appUserRepo.findByEmail(email.toLowerCase()).isEmpty()) return null;
        return appUserRepo.findByEmail(email.toLowerCase()).get();
    }



    public AppUser getUserByEmail(Long id){
        return appUserRepo.findById(id).get();
    }


    /* This method return all the courses available or uploaded by the admin*/
    public List<CoursesDto> getAllCourses(){
        List<CoursesDto> courses = new ArrayList<>();
        courseRepository.findAll()
                .stream()
                .forEach((course) -> {
                    CoursesDto courseDto = new CoursesDto(course.getId(),
                            course.getCourseTitle(),
                            course.getCoursePrice(),
                            course.getCourseDetails()
                            );
                    String coursePath = "default/image.jpg";
                    if(course.getImageUrl() != null){
                        String imagePath = "upload/"+course.getId()+"/"+course.getImageUrl();
                        if(Files.exists(Paths.get(imagePath)))
                            coursePath = course.getId()
                                    +"/" +course.getImageUrl();
                    }
                    courseDto.setImageUrl(coursePath);
                    courses.add(courseDto);
                });
        return courses;
    }



    /* Verify the user using the verification code */
    public boolean verify(VerifyUser appUser){
        Optional<AppUser> user = appUserRepo.findById(appUser.getId());
        if(user.isEmpty()) return false;
        if(user.get().getVerification().equalsIgnoreCase(appUser.getCode())){
            user.get().setVerification(null);
            user.get().setEnabled(true);
            sendWelcomeMessage(user.get().getEmail());
            return true;
        }
        return false;
    }



    /* Get registered courses by user */
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



    public Page<UserAssignmentDto> getAssignmentByPage(Authentication authentication, int pageNumber){
        CustomAppUser appUser = (CustomAppUser) authentication.getPrincipal();
        Optional<AppUser> student = appUserRepo.findByEmail(appUser.getUser().getEmail());
        if(student.isEmpty()) return null;
        List<RegisterCourse> registerCourseList = registerCourseRepo.findAllByUser(appUser.getUser());

        if(registerCourseList.size() < 1) return Page.empty();

        List<UserAssignmentDto> assignmentList = new ArrayList<>();
        int index = 0;
        for(RegisterCourse registerCourse: registerCourseList){
            List<Assignment> assignments = assignmentRepo.findAllByCourse(registerCourse.getCourse());
            for(Assignment ass: assignments){
                index++;
                assignmentList.add(new UserAssignmentDto(
                        index,ass.getTitle(),
                        ass.getInstructions(),
                        ass.getUploadDate(),
                        ass.getSubmissionDate()
                ));

            }
        }

        List<UserAssignmentDto> sortedByDate = assignmentList.stream()
                .sorted((ass1, ass2) ->
                        compareStringDates(
                                ass1.getUploadDate(),
                                ass2.getUploadDate()
                        ))
                .map( ass -> {
                    UserAssignmentDto assignmentDto = ass;
                    assignmentDto.setUploadDate(formatDate2(ass.getUploadDate()));
                    assignmentDto.setSubmissionDate(formatDate2(ass.getSubmissionDate()));
                    return assignmentDto;
                })
                .collect(Collectors.toList());

        pageNumber--;
        int size = 3;
        Pageable pageable = PageRequest.of(pageNumber , size);

        int max = Math.min(size * (pageNumber + 1), sortedByDate.size());
        return new PageImpl<>(sortedByDate.subList(pageNumber * size, max ), pageable, sortedByDate.size());
    }


    public Page<Uploads> getActivities(Authentication authentication, int pageNumber){

        CustomAppUser appUser = (CustomAppUser) authentication.getPrincipal();
        List<RegisterCourse> registerCourseList=registerCourseRepo.findAllByUser(appUser.getUser());
        if(registerCourseList == null || registerCourseList.isEmpty()) return Page.empty();

        List<Uploads> uploadDtoList = new ArrayList<>();

        for(RegisterCourse registerCourse: registerCourseList){
            Course course = registerCourse.getCourse();
            uploadDtoList.addAll(uploadRepo.findAllByCourse(course));
        }
        System.out.println(uploadDtoList.size());
        List<Uploads> sortedByDate = uploadDtoList.stream()
                .sorted((upload1, upload2) ->
                     compareStringDates(
                            upload1.getUploadDate(),
                            upload2.getUploadDate()
                    ))
                .collect(Collectors.toList());

        pageNumber--;
        int size = 3;
        Pageable pageable = PageRequest.of(pageNumber , size);

        int max = Math.min(size * (pageNumber + 1), sortedByDate.size());
        return new PageImpl<>(sortedByDate.subList(pageNumber * size, max ), pageable, sortedByDate.size());

    }



    private int compareStringDates(String date1String, String date2String){
        Date date1 = convertStringDateToDate(date1String);
        Date date2 = convertStringDateToDate(date2String);
        return date2.compareTo(date1);
    }



    /* Convert String Date into Date format for comparation*/
    private Date convertStringDateToDate(String date){
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    .parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatDate(String time){
        SimpleDateFormat format_24 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format_24.parse(time);
            time = new SimpleDateFormat("E, MMM dd yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }



    private String formatDate2(String time){
        SimpleDateFormat format_24 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format_24.parse(time);
            time = new SimpleDateFormat("dd-MMM-yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }




    public List<EventsDto> getLatestEvents(int counts){
        List<Events> events = eventsRepo.findAll()
                .stream()
                .sorted((event1, event2) ->
                    compareStringDates(
                            event1.getDate(),
                            event2.getDate()
                    ))
                .limit(counts)
                .collect(Collectors.toList());

        return events.stream()
                .map(event -> {
                    EventsDto events1 = new EventsDto();
                    events1.setTitle(event.getTitle());
                    events1.setTime(event.getTime());
                    events1.setDate(formatDate(event.getDate()));
                    events1.setDetails(event.getDetails());
                    return events1;
                })
                .collect(Collectors.toList());
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
            zenithEmailSenderServices.sendEmail(subject,body,List.of(adminEmail));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public boolean verifyPassword(PasswordChange passwordChange, Long id){
        Optional<AppUser> student = appUserRepo.findById(id);
        if(!(passwordChange.getNewPassword()
                .equals(passwordChange.getConfirmPassword()))
                || student.isEmpty()
        ) return false;

        AppUser user = student.get();
        String newPassword = passwordEncoder.encode(passwordChange.getNewPassword());
        String resetCode = passwordChange.getResetPassword();
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
            zenithEmailSenderServices.sendEmail(subject,body,List.of(email));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public Page<EventCard> getEventsCard(int pageNumber){
        List<EventCard> events = eventsRepo.findAll()
                .stream()
                .sorted( (evt1, evt2) -> compareStringDates(evt1.getDate(), evt2.getDate()))
                .map((event)-> {
                    EventCard event1 = new EventCard();
                    event1.setTitle(event.getTitle());
                    event1.setDetails(event.getDetails());
                    LocalDate date = LocalDate.parse(event.getDate());
                    event1.setMonth(date.getMonth().toString().substring(0,3));
                    event1.setDay(Integer.toString(date.getDayOfMonth()));
                    event1.setYear(Integer.toString(date.getYear()));
                    event1.setTime(event.getTime());
                    return event1;
                })
                .collect(Collectors.toList());
        pageNumber--;
        int size = 6;
        Pageable pageable = PageRequest.of(pageNumber , size);

        int max = Math.min(size * (pageNumber + 1), events.size());
        return new PageImpl<>(events.subList(pageNumber * size, max ), pageable, events.size());
    }

    private void sendWelcomeMessage(String email){
        String subject = "Welcome to Zenith-Analysis";
        String body = "You have successfully register for Zenith Anaylsis Business Program";
        try {
            zenithEmailSenderServices.sendEmail(subject,body,List.of(email));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}

