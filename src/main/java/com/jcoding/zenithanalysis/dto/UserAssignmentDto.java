package com.jcoding.zenithanalysis.dto;

import com.jcoding.zenithanalysis.entity.Course;

public class UserAssignmentDto {

    private int index;
    private String title;
    private String details;
    private String uploadDate;
    private String submissionDate;
    private String courseTitle;

    public UserAssignmentDto() {
    }

    public UserAssignmentDto(int index, String title, String details, String uploadDate, String submissionDate) {
        this.index = index;
        this.title = title;
        this.details = details;
        this.uploadDate = uploadDate;
        this.submissionDate = submissionDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public void setCourseTitle(Course course){
        this.courseTitle = course.getCourseTitle();
    }

    public String getCourseTitle(){
        return courseTitle;
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
