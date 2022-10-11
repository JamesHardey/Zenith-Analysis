package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.dto.*;
import com.jcoding.zenithanalysis.dto.assignment.AssignDto;
import com.jcoding.zenithanalysis.dto.event.EventsDto;
import com.jcoding.zenithanalysis.dto.user.AdminDisplay;
import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.dto.user.NewAdminDto;
import com.jcoding.zenithanalysis.entity.*;
import com.jcoding.zenithanalysis.repository.*;
import com.jcoding.zenithanalysis.utils.ZenithFileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static com.jcoding.zenithanalysis.utils.ZenithUtils.*;
import static com.jcoding.zenithanalysis.utils.ZenithMessages.*;


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
    private AppUserRepo appUserRepo;

    @Autowired
    private EventsRepo eventsRepo;

    @Autowired
    private UploadRepo uploadRepo;

    @Autowired
    private RolesRepo rolesRepo;

    @Autowired
    private ResourceRepo resourceRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Value("${base_url}")
    private String baseUrl;

    private static final Logger LOGGER = Logger.getLogger(AdminServices.class.getPackageName());


    /******* Zenith App Users *******/

    public void deleteUser(AppUser appUser){
        appUserRepo.delete(appUser);
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

    /************** End *************/




    /******* Assignment methods ********/

    public List<AssignDto> getAllAssignment(){
        return assignmentRepo.findAll()
                .stream()
                .map((assign)-> new AssignDto(
                        assign.getId(),
                        assign.getTitle(),
                        assign.getModule(),
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
                    uploads.getModule(),
                    uploads.getUploadDate(),
                    uploads.getMessage()
            ));
        }
        return uploadDtoList;
    }

    private boolean sendAssignment(Assignment assignment){
        List<AppUser> appUsers = appUserRepo.findAllByApprovedTrue();
        appUsers.stream()
                .forEach(a -> System.out.println(a.getEmail()));

        Map<String, String> messageContent =
                assignmentMessage(assignment, baseUrl);

        List<String> emails =
                appUsers.stream()
                        .map(AppUser::getEmail)
                        .collect(Collectors.toList());

        String subject = messageContent.get("subject");
        String body = messageContent.get("body");

        if(emails.size() >0){
            try {
                zenithEmailSenderServices.sendEmail(
                        subject, body, emails);
                LOGGER.info("Message Sent");
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
                LOGGER.info("Error in sending Assignment ID: "+assignment.getId());
                LOGGER.info("Error in sending Message Due to Internet");
            }
        }
        return true;
    }

    public boolean addAssignment(AssignDto assignDto, MultipartFile file){
        Assignment newAssignment =
                new Assignment(
                                assignDto.getTitle(),
                                assignDto.getDetails(),
                                assignDto.getModule(),
                                assignDto.getSubmissionDate()
                                );
        newAssignment.setUploadDate(LocalDate.now().toString());
        Assignment savedAssignment = assignmentRepo.save(newAssignment);

        if(!file.isEmpty()){
            String fullPathName = getFileName(
                    savedAssignment.getId().toString(),
                    savedAssignment.getTitle(),
                    ZenithFileType.ASSIGNMENT,
                    file);

            if(uploadFile(fullPathName, file)) {
                sendAssignment(savedAssignment);
                savedAssignment.setDocumentURL(fullPathName);
                assignmentRepo.save(savedAssignment);
            }
            else{
                deleteAssignment(savedAssignment);
                return false;
            }
        }
        Long id = savedAssignment.getId();
        return id > 0;
    }

    public AssignDto getAssignmentById(Long id){
        Assignment ass = assignmentRepo.findById(id).get();
        return new AssignDto(
                ass.getId(),
                ass.getTitle(),
                ass.getModule(),
                ass.getInstructions(),
                ass.getSubmissionDate()
        );
    }

    public void updateAssignment(AssignDto assignDto, MultipartFile file){
        Assignment assignment = assignmentRepo.findById(assignDto.getId()).get();
        assignment.setTitle(assignDto.getTitle());
        assignment.setModule(assignDto.getModule());
        assignment.setInstructions(assignDto.getDetails());
        assignment.setSubmissionDate(assignDto.getSubmissionDate());
        if(!file.isEmpty()) {
            String fullPathName = getFileName(
                    assignment.getId().toString(),
                    assignment.getTitle(),
                    ZenithFileType.ASSIGNMENT,
                    file
            );
            uploadFile(fullPathName, file);
        }
        assignmentRepo.save(assignment);
    }

    public void deleteAssignment(Long id){
        Optional<Assignment> assignment = assignmentRepo.findById(id);
        if (assignment.isPresent()) {
            deleteAssignment(assignment.get());
        }
    }

    public void deleteAssignment(Assignment assignment){
        assignmentRepo.delete(assignment);
        String fullPathName = assignment.getDocumentURL();
        deleteInFolder(fullPathName);
    }

    /******* End of Assignment Methods *********/



    /* Class Activities upload method */

    public boolean addUpload(UploadDto uploadDto, MultipartFile file){
        Uploads upload = new Uploads();
        upload.setTitle(uploadDto.getTitle());
        upload.setModule(uploadDto.getModule());
        upload.setMessage(uploadDto.getMessage());
        upload.setUrl(uploadDto.getUrl());
        upload.setUploadDate(LocalDate.now().toString());
        Uploads savedUpload = uploadRepo.save(upload);

        if(!file.isEmpty()){
            String fullPathName = getFileName(
                    savedUpload.getId().toString(),
                    savedUpload.getTitle(),
                    ZenithFileType.UPLOAD,
                    file
            );

            if(uploadFile(fullPathName, file)) {
                sendUpload(savedUpload);
                savedUpload.setDocumentURL(fullPathName);
                uploadRepo.save(savedUpload);
            }
            else{
                deleteUpload(savedUpload.getId());
                return false;
            }
        }

        return true;
    }

    public UploadDto getUploadById(Long id){
        Uploads uploads = uploadRepo.findById(id).get();
        UploadDto uploadDto = new UploadDto();
        uploadDto.setId(uploads.getId());
        uploadDto.setUrl(uploads.getUrl());
        uploadDto.setTitle(uploads.getTitle());
        uploadDto.setModule(uploads.getModule());
        uploadDto.setMessage(uploads.getMessage());
        return uploadDto;
    }

    public void updateUploadClass(UploadDto uploadDto,MultipartFile file){
        Uploads upload = uploadRepo.findById(uploadDto.getId()).get();
        upload.setTitle(uploadDto.getTitle());
        upload.setModule(uploadDto.getModule());
        upload.setMessage(uploadDto.getMessage());
        upload.setUrl(uploadDto.getUrl());

        if(!file.isEmpty()) {
            String fullPathName = getFileName(
                    upload.getId().toString(),
                    upload.getTitle(),
                    ZenithFileType.UPLOAD,
                    file
            );
            uploadFile(fullPathName, file);
        }
        uploadRepo.save(upload);
    }

    private void sendUpload(Uploads uploads){
        List<AppUser> users = appUserRepo.findAllByApprovedTrue();
        List<String> emails =
                users.stream()
                        .map(AppUser::getEmail)
                        .collect(Collectors.toList());

        if(users.size() < 1) return;

        Map<String, String> message = classActivityMessage(uploads, baseUrl);
        String subject = message.get("subject");
        String body = message.get("body");

        try {
            zenithEmailSenderServices.sendEmail(subject, body, emails);
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.info("Error in sending uploads");
            LOGGER.info("Error in sending Message Due to Internet");
        }
    }

    public void deleteUpload(Long id){
        deleteUpload(uploadRepo.findById(id).get());
    }

    public void deleteUpload(Uploads uploads){
        uploadRepo.delete(uploads);
        String fullPathName = uploads.getDocumentURL();
        deleteInFolder(fullPathName);
    }

    /* End Class Activities */



    /* Events Methods */

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

    /* End Events Methods */

    public void testAssignment(Assignment assignment){
        String subject = "Assignment From Zenith-Analysis";
        String body = "Assignment for "+assignment.getModule()+"\n"
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
            LOGGER.info("Error in sending test assignment");
            LOGGER.info("Error in sending Message Due to Internet");
        }

        System.out.println("Message Sent");

    }

    public boolean uploadResume(String pathName,MultipartFile file){
        Path path1 = Paths.get(
                "upload/"+pathName
        ).toAbsolutePath();
        try {
            Files.deleteIfExists(path1);
            Files.createFile(path1);
            Files.write(path1, file.getBytes());
        } catch (IOException e) {
            LOGGER.info("Error in uploading resume file");
            return false;
        }
        return true;
    }



    /********** Resources ********/
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

    public boolean addResource(ResourceDto resourceDto, MultipartFile file) {

        ZenithFileType zenithFileType = getResourceType(resourceDto.getResourceType());
        if(file.isEmpty() || (zenithFileType == null)) return false;

        Resource resource = new Resource();
        resource.setResourceType(resourceDto.getResourceType());
        resource.setTitle(resourceDto.getTitle());
        resource.setUploadDate(LocalDate.now().toString());

        Resource savedResource = resourceRepo.save(resource);

        String fullPathName = getFileName(
                savedResource.getId().toString(),
                savedResource.getTitle(),
                zenithFileType,
                file
        );

        if(uploadFile(fullPathName,file)){
            resource.setDocumentUrl(fullPathName);
            return true;
        }
        return false;
    }

    public void deleteResource(Long resourceId) {
        Optional<Resource> resource = resourceRepo.findById(resourceId);
        resource.ifPresent(this::deleteResource);
    }

    private void deleteResource(Resource resource){
        resourceRepo.delete(resource);
    }

    public ResourceDto getResourceById(Long id) {
        Optional<Resource> resource = resourceRepo.findById(id);
        return new ResourceDto(
            resource.get().getId(),
            resource.get().getResourceType(),
            resource.get().getTitle(),
            resource.get().getDocumentUrl(),
            resource.get().getUploadDate()
        );
    }

    public void updateResource(ResourceDto resourceDto, MultipartFile file) {

        if(resourceDto.getResourceType() == null
        || resourceDto.getId() == null) return;

        Resource resource = resourceRepo.findById(resourceDto.getId()).get();
        ZenithFileType fileType = getResourceType(resourceDto.getResourceType());

        resource.setResourceType(resourceDto.getResourceType());
        resource.setTitle(resourceDto.getTitle());

        if(!file.isEmpty()){
            String fullPathName = getFileName(
                    resourceDto.getId().toString(),
                    resourceDto.getTitle(),
                    fileType,
                    file
            );
            if(uploadFile(fullPathName,file)){
                deleteInFolder(resource.getDocumentUrl());
                resource.setDocumentUrl(fullPathName);
            }

        }
        resourceRepo.save(resource);
    }
}
