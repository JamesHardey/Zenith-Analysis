package com.jcoding.zenithanalysis.dto;

public class AssignDto {

    private Long index;
    private Long id;
    private String title;
    private String details;
    private String uploadDate;
    private String submissionDate;

    public AssignDto() {
    }

    public AssignDto(Long index, Long id, String title, String details, String uploadDate, String submissionDate) {
        this.index = index;
        this.id = id;
        this.title = title;
        this.details = details;
        this.uploadDate = uploadDate;
        this.submissionDate = submissionDate;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourse() {
        return details;
    }

    public void setCourse(String details) {
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
}
