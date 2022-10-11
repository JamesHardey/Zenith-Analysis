package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.dto.*;
import com.jcoding.zenithanalysis.dto.contact.ContactUsDto;
import com.jcoding.zenithanalysis.dto.event.EventCard;
import com.jcoding.zenithanalysis.dto.event.EventsDto;
import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.dto.user.PasswordChange;
import com.jcoding.zenithanalysis.dto.user.RegisterUser;
import com.jcoding.zenithanalysis.dto.user.VerifyUser;
import com.jcoding.zenithanalysis.entity.*;
import com.jcoding.zenithanalysis.repository.*;
import com.jcoding.zenithanalysis.utils.ZenithFileType;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppUserServices{

    @Autowired
    private AppUserRepo appUserRepo;

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

    @Autowired
    private ResourceRepo resourceRepo;

    @Value("${admin_email}")
    private String adminEmail;

    private static final Logger LOGGER = Logger.getLogger(AppUserServices.class.getPackageName());


    /* Create new User with the RegisterUser Dto*/
    public Long createUser(RegisterUser registerUser){
        AppUser newStudent = new AppUser();
        if(!registerUser.getPassword().equals(registerUser.getConfirmPassword())){
            LOGGER.info("Incorrect Password Combination");
            return 0L;
        }
        Optional<AppUser> student = appUserRepo.findByEmail(newStudent.getEmail());
        if(student.isPresent()){
            LOGGER.info("User Email is Present Already - "+newStudent.getEmail());
            return 0L;
        }
        newStudent.setName(registerUser.getName());
        newStudent.setEmail(registerUser.getEmail().toLowerCase());
        newStudent.setRole(repo.findByName("USER"));
        newStudent.setPassword(passwordEncoder.encode(registerUser.getPassword()));

        newStudent.setVerification(generateCode());
        newStudent.setEnabled(false);
        appUserRepo.save(newStudent);

        String body = "Welcome to Zenith Analysis \n" +
                "\n" +
                "Confirm with the code below \n" +
                "\n" +
            "This is your code "+newStudent.getVerification();
        try {
            zenithEmailSenderServices.sendEmail("Verification", body, List.of(newStudent.getEmail()));
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.info("Error in creating User");
            LOGGER.info("Error in sending Message Due to Internet");
            return 0L;
        }
        LOGGER.info("Sent Message to "+newStudent.toString());
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




    public void resendVerification(Long id){
        Optional<AppUser> student = appUserRepo.findById(id);
        if(student.isEmpty()) return;
        AppUser user = student.get();
        user.setVerification(generateCode());
        appUserRepo.save(user);
        String body = "Welcome to Zenith Analysis \n" +
                "\n" +
                "Confirm with the code below \n" +
                "\n" +
                "This is your code "+user.getVerification();

        try {
            zenithEmailSenderServices.sendEmail("Verification", body, List.of(user.getEmail()));
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.info("Error in resending verification to id");
            LOGGER.info("Error in sending Message Due to Internet");
        }
    }



    public void resendVerification(String email){
        Optional<AppUser> student = appUserRepo.findByEmail(email.toLowerCase());
        if(student.isEmpty()) return;
        AppUser user = student.get();
        user.setVerification(generateCode());
        appUserRepo.save(user);

        String body = "Welcome to Zenith Analysis \n" +
                "\n" +
                "Confirm with the code below \n" +
                "\n" +
                "This is your code "+user.getVerification();

        try {
            zenithEmailSenderServices.sendEmail("Verification", body, List.of(email));
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.info("Error in resending verification to email");
            LOGGER.info("Error in sending Message Due to Internet");
        }
    }




    public AppUser getUserByEmail(String email){
        if(appUserRepo.findByEmail(email.toLowerCase()).isEmpty()) return null;
        return appUserRepo.findByEmail(email.toLowerCase()).get();
    }



    public AppUser getUserByEmail(Long id){
        return appUserRepo.findById(id).get();
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




    public Page<UserAssignmentDto> getAssignmentByPage(Authentication authentication, int pageNumber){
        CustomAppUser appUser = (CustomAppUser) authentication.getPrincipal();
        Optional<AppUser> student = appUserRepo.findByEmail(appUser.getUser().getEmail());
        if(student.isEmpty()) return null;

        List<UserAssignmentDto> assignmentList = new ArrayList<>();
        List<Assignment> assignments = assignmentRepo.findAll();

        assignmentList = assignments.stream()
                                    .map((ass) ->
                                            new UserAssignmentDto(
                                                    ass.getTitle(),
                                                    ass.getInstructions(),
                                                    ass.getUploadDate(),
                                                    ass.getSubmissionDate(),
                                                    ass.getDocumentURL()
                                            ))
                                    .collect(Collectors.toList());



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

        List<Uploads> uploadDtoList = new ArrayList<>(uploadRepo.findAll());

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

    public List<ResourceDto> getALLResources() {
        List<Resource> resources = resourceRepo.findAll();
        return resources.stream()
                .map((res) ->
                        new ResourceDto(
                                res.getId(),
                                res.getResourceType(),
                                res.getTitle(),
                                res.getDocumentUrl(),
                                res.getUploadDate()
                        )
                )
                .collect(Collectors.toList());
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
            LOGGER.info("Error in parsing date for conversion");
            LOGGER.info("Error in convert String date to date");
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
            LOGGER.info("Error in parsing time for conversion");
            LOGGER.info("Error in convert String time to string");
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
            LOGGER.info("Error in contacting admin");
            LOGGER.info("Error in sending Message Due to Internet");
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
                "\n" +
                "";
        String subject = "Zenith-Analysis Password recovery";
        try {
            zenithEmailSenderServices.sendEmail(subject,body,List.of(email));
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.info("Error in sending recovery password");
            LOGGER.info("Error in sending Message Due to Internet");
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
            LOGGER.info("Error in sending welcome message");
            LOGGER.info("Error in sending Message Due to Internet");
        }
    }

    public List<ResourceDto> getResource(ZenithFileType zenithFileType) {
        String resourceType = "";

        switch (zenithFileType){
            case PRESENTATION: resourceType = "Presentation Guide"; break;
            case COVER_LETTER: resourceType = "Cover Letter"; break;
            case RESUME: resourceType= "Resume"; break;
        }

        return resourceRepo.findByResourceType(resourceType).stream()
                .map((res) -> new ResourceDto(
                        res.getId(),
                        res.getResourceType(),
                        res.getTitle(),
                        res.getDocumentUrl(),
                        res.getUploadDate()
                ))
                .collect(Collectors.toList());
    }
}

