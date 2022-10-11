package com.jcoding.zenithanalysis.dto;

import com.jcoding.zenithanalysis.entity.Course;

public class UserAssignmentDto {

    private String title;
    private String details;
    private String uploadDate;
    private String submissionDate;
    private String documentUrl;

    public UserAssignmentDto() {
    }

    public UserAssignmentDto(String title, String details, String uploadDate, String submissionDate, String documentUrl) {
        this.title = title;
        this.details = details;
        this.uploadDate = uploadDate;
        this.submissionDate = submissionDate;
        this.documentUrl = documentUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }
}
