package com.jcoding.zenithanalysis.dto.assignment;

public class AssignDto {
    private Long id;
    private String title;
    private String details;
    private String module;
    private String time;
    private String uploadDate;
    private String submissionDate;
    private String documentURL;

    public AssignDto() {
    }

    public AssignDto(Long id,String title,String module, String details,String submissionDate) {
        this.id = id;
        this.title = title;
        this.module = module;
        this.details = details;
        this.submissionDate = submissionDate;
    }


    public AssignDto(Long id,String title,String module, String details, String uploadDate,String submissionDate) {
        this.id = id;
        this.title = title;
        this.module = module;
        this.details = details;
        this.uploadDate = uploadDate;
        this.submissionDate = submissionDate;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }
}
