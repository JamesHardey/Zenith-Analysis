package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.dto.*;
import com.jcoding.zenithanalysis.dto.assignment.AssignDto;
import com.jcoding.zenithanalysis.dto.course.AllowedCourses;
import com.jcoding.zenithanalysis.dto.course.CoursesDto;
import com.jcoding.zenithanalysis.dto.course.RegisteredCourse;
import com.jcoding.zenithanalysis.dto.event.EventsDto;
import com.jcoding.zenithanalysis.dto.user.AdminDisplay;
import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.dto.user.NewAdminDto;
import com.jcoding.zenithanalysis.entity.*;
import com.jcoding.zenithanalysis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private EventsRepo eventsRepo;

    @Autowired
    private UploadRepo uploadRepo;

    @Autowired
    private RolesRepo rolesRepo;

    @Autowired
    private PasswordEncoder encoder;


    public void sendClassVideoLink(String body, Course course){
        List<RegisterCourse> list = registerCourseRepo.findAllByCourse(course);
        List<String> emails = new ArrayList<>();

        for(RegisterCourse registerCourse: list){
           emails.add(registerCourse.getUser().getEmail());
        }

        try {
            zenithEmailSenderServices.sendEmail("Link to Class Video", body, emails);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(AppUser appUser){
        registerCourseRepo.deleteAll(registerCourseRepo.findAllByUser(appUser));
        appUserRepo.delete(appUser);
    }

    public void deleteAssignment(Assignment assignment){
        assignmentRepo.delete(assignment);
    }

    public void deleteAssignment(Long id){
        assignmentRepo.delete(assignmentRepo.findById(id).get());
    }

    public void addAssignment(Assignment assignment){
        assignmentRepo.save(assignment);
    }

    public void deleteCourse(Course course){
        courseRepository.delete(course);
    }

    public void deleteCourse(Long courseId){
        Course course = courseRepository.findById(courseId).get();
        assignmentRepo.deleteAll(assignmentRepo.findAllByCourse(course));
        registerCourseRepo.deleteAll(registerCourseRepo.findAllByCourse(course));
        uploadRepo.deleteAll(uploadRepo.findAllByCourse(course));
        deleteCourse(course);
    }

    public boolean addCourse(CoursesDto courseDto
            , MultipartFile file
    ){
        courseDto.setPrice(courseDto.getPrice());
        courseDto.setImageUrl(file.getOriginalFilename());
        Course course = new Course(
                courseDto.getTitle(),
                courseDto.getPrice(),
                courseDto.getDetails(),
                courseDto.getImageUrl()
        );

        Course savedCourse = courseRepository.save(course);
        Path folderPath = Paths.get(
                "upload/"+savedCourse.getId()
        ).toAbsolutePath();

        try {
            Files.deleteIfExists(folderPath);
            Files.createDirectory(folderPath);
            String path1 = folderPath.toString();
            Path path = Paths.get(path1, file.getOriginalFilename());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            courseRepository.delete(course);
            return false;
        }
        return true;
    }

    public CoursesDto getCourseById(Long id){
        Course course = courseRepository.findById(id).get();
        CoursesDto coursesDto = new CoursesDto();
        coursesDto.setId(course.getId());
        coursesDto.setTitle(course.getCourseTitle());
        coursesDto.setPrice(course.getCoursePrice());
        coursesDto.setDetails(course.getCourseDetails());
        return coursesDto;
    }

    public void updateCourse(CoursesDto coursesDto, MultipartFile file){
        Course course = courseRepository.findById(coursesDto.getId()).get();
        course.setCourseTitle(coursesDto.getTitle());
        course.setCoursePrice(coursesDto.getPrice());
        course.setCourseDetails(coursesDto.getDetails());
        if(coursesDto.getImageUrl() != null) {
            course.setImageUrl(coursesDto.getImageUrl());
            Path folderPath = Paths.get(
                    "upload/"+course.getId()
            ).toAbsolutePath();
            try {
                if(!Files.exists(folderPath)) Files.createDirectory(folderPath);
                String path1 = folderPath.toString();
                Path path = Paths.get(path1, file.getOriginalFilename());
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        courseRepository.save(course);
    }


    public void deleteRegisterCourse(RegisterCourse registerCourse){
        registerCourseRepo.delete(registerCourse);
    }

    public RegisterCourse addCourseRegister(RegisterCourse registerCourse){
        return registerCourseRepo.save(registerCourse);
    }


    public List<EventsDto> getEvents(){
        List<EventsDto> eventsDtoList = new ArrayList<>();

        int index =0;
        for(Events event: eventsRepo.findAll()){
            index++;
            eventsDtoList.add(new EventsDto(
                    index,
                    event.getId(),
                    event.getTitle(),
                    event.getDetails(),
                    event.getDate(),
                    event.getTime(),
                    null
            ));
        }
        return eventsDtoList;
    }


    public List<Course> getListOfCourses(){
        return courseRepository.findAll();
    }


    public List<RegisteredCourse> getAllRegisteredCourses(){
        List<Course> courses = courseRepository.findAll();
        List<RegisterCourse> registerCourseList = registerCourseRepo.findAll();

        List<RegisteredCourse> registeredCourses = new ArrayList<>();
        int index = 0;
        for(Course course: courses){
            int count = 0;
            for(RegisterCourse registerCourse: registerCourseList){
                if(registerCourse.getCourse().getId().equals(course.getId())){
                    count++;
                }

            }
            index++;
            registeredCourses.add(new RegisteredCourse(index,course.getId() ,course.getCourseTitle(), course.getCoursePrice(),count));
        }
        return registeredCourses;
    }


    public List<AssignDto> getAllAssignment(){
        return assignmentRepo.findAll()
                .stream()
                .map((assign)-> new AssignDto(
                        assign.getId(),
                        assign.getTitle(),
                        assign.getCourse().getCourseTitle(),
                        assign.getInstructions(),
                        assign.getUploadDate(),
                        assign.getSubmissionDate()
                ))
                .collect(Collectors.toList());
    }

    public List<UploadDto> getUploads(){
        List<UploadDto> uploadDtoList = new ArrayList<>();
        int index =0;
        for(Uploads uploads: uploadRepo.findAll()){
            index++;
            uploadDtoList.add(new UploadDto(
                    index,
                    uploads.getId(),
                    uploads.getTitle(),
                    uploads.getUrl(),
                    uploads.getCourse().getCourseTitle(),
                    uploads.getUploadDate(),
                    uploads.getMessage()
            ));
        }
        return uploadDtoList;
    }


    public boolean addAssignment(AssignDto assignDto){
        Optional<Course> courseAvailability = courseRepository.findByCourseTitle(assignDto.getCourse());
        if(courseAvailability.isEmpty()) return false;
        Assignment newAssignment = new Assignment(
                assignDto.getTitle(),assignDto.getDetails(),
                courseAvailability.get(),assignDto.getSubmissionDate()
        );
        newAssignment.setUploadDate(LocalDate.now().toString());

        Assignment savedAssignment = assignmentRepo.save(newAssignment);
        sendAssignment(savedAssignment);
        Long id = savedAssignment.getId();
        return id > 0;
    }

    public AssignDto getAssignmentById(Long id){
        Assignment ass = assignmentRepo.findById(id).get();
        return new AssignDto(
                ass.getId(),
                ass.getTitle(),
                ass.getCourse().getCourseTitle(),
                ass.getInstructions(),
                ass.getSubmissionDate()
        );
    }

    public void updateAssignment(AssignDto assignDto){
        Assignment assignment = assignmentRepo.findById(assignDto.getId()).get();
        assignment.setTitle(assignDto.getTitle());
        assignment.setCourse(
                courseRepository.findByCourseTitle(
                        assignDto.getCourse()
                ).get());
        assignment.setInstructions(assignDto.getDetails());
        assignment.setSubmissionDate(assignDto.getSubmissionDate());
        assignmentRepo.save(assignment);
    }

    public boolean addUpload(UploadDto uploadDto){
        Uploads  upload = new Uploads();
        Course course = courseRepository.findByCourseTitle(uploadDto.getCourse()).get();
        upload.setTitle(uploadDto.getTitle());
        upload.setCourse(course);
        upload.setMessage(uploadDto.getMessage());
        upload.setUrl(uploadDto.getUrl());
        upload.setUploadDate(LocalDate.now().toString());
        uploadRepo.save(upload);
        sendUpload(upload);
        return true;
    }

    public UploadDto getUploadById(Long id){
        Uploads uploads = uploadRepo.findById(id).get();
        UploadDto uploadDto = new UploadDto();
        uploadDto.setId(uploads.getId());
        uploadDto.setUrl(uploads.getUrl());
        uploadDto.setTitle(uploads.getTitle());
        uploadDto.setCourse(uploads.getCourse().getCourseTitle());
        uploadDto.setMessage(uploads.getMessage());
        return uploadDto;
    }

    public void updateUploadClass(UploadDto uploadDto){
        Uploads upload = uploadRepo.findById(uploadDto.getId()).get();
        Course course = courseRepository.findByCourseTitle(uploadDto.getCourse()).get();
        upload.setTitle(uploadDto.getTitle());
        upload.setCourse(course);
        upload.setMessage(uploadDto.getMessage());
        upload.setUrl(uploadDto.getUrl());
        upload.setUploadDate(LocalDate.now().toString());
        uploadRepo.save(upload);
    }

    public boolean addEvents(EventsDto eventsDto){
        Events event = new Events();
        event.setTitle(eventsDto.getTitle());
        event.setDetails(eventsDto.getDetails());
        event.setDate(eventsDto.getDate());
        String time = formatTime(eventsDto.getTime());
        event.setTime((time!=null) ? time : eventsDto.getTime());
        eventsRepo.save(event);
        return true;
    }

    public void editEvent(EventsDto eventsDto){
        Events event = eventsRepo.findById(eventsDto.getId()).get();
        event.setTitle(eventsDto.getTitle());
        event.setTime(formatTime(eventsDto.getTime()));
        event.setDate(eventsDto.getDate());
        eventsRepo.save(event);
    }

    public void deleteEvent(Long id){
        Events events = eventsRepo.findById(id).get();
        eventsRepo.delete(events);
    }

    public EventsDto getEventById(Long id){
        Events event = eventsRepo.findById(id).get();
        return new EventsDto(
                event.getId(),
                event.getTitle(),
                event.getDetails(),
                event.getDate(),
                reFormatTime(event.getTime())
                );
    }


    public boolean addCourse(){
        return false;
    }

    private boolean sendAssignment(Assignment assignment){
        Course course = assignment.getCourse();
        List<RegisterCourse> registerCourseList= registerCourseRepo.findAllByCourse(course);
        List<AppUser> appUsers = new ArrayList<>();
        for(RegisterCourse registerCourse: registerCourseList){
            appUsers.add(registerCourse.getUser());
        }
        String subject = "Assignment From Zenith-Analysis";
        String body = "Assignment for "+assignment.getCourse().getCourseTitle()+"\n"
                +"\n\n"
                +assignment.getInstructions()+"\n"
                +"\n\n"
                +"Uploaded date: "+assignment.getUploadDate()+"\n"
                +"Submission date: "+assignment.getSubmissionDate()+"\n"
                +"\n\n"
                +"Zenith-Analysis";

        String[] emails = new String[appUsers.size()];
        int index = 0;
        for(AppUser appUser: appUsers){
            emails[index] = appUser.getEmail();
            index++;
        }

        if(emails.length >0){
            try {
                zenithEmailSenderServices.sendEmail(subject,body, Arrays.asList(emails));
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("Message Sent");
        }
        return true;
    }

    public void testAssignment(Assignment assignment){
        String subject = "Assignment From Zenith-Analysis";
        String body = "Assignment for "+assignment.getCourse().getCourseTitle()+"\n"
                +"\n\n"
                +assignment.getInstructions()+"\n"
                +"\n\n"
                +"Uploaded date: "+assignment.getUploadDate()+"\n"
                +"Submission date: "+assignment.getSubmissionDate()+"\n"
                +"\n\n"
                +"Zenith-Analysis";

        String[] emails = {"jamesade646@gmail.com",
                "jamesade747@gmail.com",
                "jimmyjames00710@yahoo.com",
                "jamesade.learning@gmail.com"
        };

        try {
            zenithEmailSenderServices.sendEmail(subject,body, List.of(emails));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("Message Sent");
    }

    private void sendUpload(Uploads uploads){
        List<RegisterCourse> list = registerCourseRepo.findAllByCourse(uploads.getCourse());
        List<String> emails = new ArrayList<>();

        if(list.size() < 1) return;

        for(RegisterCourse registerCourse: list){
            emails.add(registerCourse.getUser().getEmail());
        }

        String subject = uploads.getTitle();
        String body = uploads.getMessage();

        try {
            zenithEmailSenderServices.sendEmail(subject, body, emails);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteUpload(Long id){
        uploadRepo.delete(uploadRepo.findById(id).get());
    }

    public List<AppUser> getAllUser(){
        return appUserRepo.findAll().stream()
                .filter((appUser) -> appUser.getRole().getName().equals("USER"))
                .collect(Collectors.toList());
    }

    public List<AppUser> getAdminUsers(){
        return appUserRepo.findAll().stream()
                .filter((appUser) -> appUser.getRole().getName().equals("ADMIN"))
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id){
        Optional<AppUser> user = appUserRepo.findById(id);
        if(user.isEmpty()) return;
        deleteUser(user.get());
    }

    public void approveUser(Long id){
        Optional<AppUser> user = appUserRepo.findById(id);
        if(user.isEmpty()) return;
        AppUser appUser = user.get();
        appUser.setApproved(!appUser.isApproved());
        appUserRepo.save(appUser);
    }

    public List<AllowedCourses> getAllCourseAndStatusByUserId(Long id){
        List<Course> courses = courseRepository.findAll();
        AppUser appUser = appUserRepo.findById(id).get();
        List<AllowedCourses> allowedCoursesList = new ArrayList<>();
        List<RegisterCourse> registerCourseList = registerCourseRepo.findAllByUser(appUser);

        for(Course course:  courses){
            boolean enrolled = false;
            for(RegisterCourse registerCourse: registerCourseList){
                if(registerCourse.getCourse().getId().equals(course.getId())){
                    enrolled = true;
                }
            }
            allowedCoursesList.add(
                    new AllowedCourses(course.getId(),course.getCourseTitle(),enrolled)
            );
        }
        return allowedCoursesList;
    }

    public void allowCourse(Long userId,Long courseId){
        AppUser appUser = appUserRepo.findById(userId).get();
        Course course = courseRepository.findById(courseId).get();
        List<RegisterCourse> registerCourses = registerCourseRepo.findAllByUser(appUser);
        for(RegisterCourse registerCourse: registerCourses){
            if(registerCourse.getCourse().getId().equals(course.getId())){
                registerCourseRepo.delete(registerCourse);
                System.out.println("Done ..if 1");
                return;
            }
        }
        RegisterCourse registerCourse = new RegisterCourse(appUser,course,LocalDate.now().toString());
        registerCourseRepo.save(registerCourse);
    }

    public boolean findUserByEmail(String email){
        return appUserRepo.findByEmail(email.toLowerCase()).isPresent();
    }

    public boolean confirmAdminPassword(String password){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser presentAdmin = (CustomAppUser)authentication.getPrincipal();
        return encoder.matches(password,presentAdmin.getPassword());
    }

    public void addNewAdmin(NewAdminDto newAdminDto){
        AppUser appUser = new AppUser();
        appUser.setEnabled(true);
        appUser.setApproved(true);
        appUser.setVerification(null);
        appUser.setRole(rolesRepo.findByName("ADMIN"));
        appUser.setEmail(newAdminDto.getEmail().toLowerCase());
        appUser.setName(newAdminDto.getName());
        appUser.setPassword(encoder.encode(newAdminDto.getPassword()));
        appUserRepo.save(appUser);
    }

    public void addResume(ResumeUploadDto resumeUploadDto){

    }

    public AdminDisplay getDisplayDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomAppUser user = (CustomAppUser) authentication.getPrincipal();
        AppUser adminUser = user.getUser();
        String firstChar = Character.toString(adminUser.getName().charAt(0));

        return new AdminDisplay(
                firstChar,
                adminUser.getName(),
                adminUser.getEmail()
        );
    }


    private String formatTime(String time){
        SimpleDateFormat format_24 = new SimpleDateFormat("HH:mm");
        try {
            Date tim = format_24.parse(time);
            time = new SimpleDateFormat("hh:mm aa").format(tim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    private String reFormatTime(String time){
        SimpleDateFormat format_24 = new SimpleDateFormat("hh:mm aa");
        try {
            Date tim = format_24.parse(time);
            time = new SimpleDateFormat("HH:mm").format(tim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

}
