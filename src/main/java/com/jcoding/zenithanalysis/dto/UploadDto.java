package com.jcoding.zenithanalysis.dto;

import com.jcoding.zenithanalysis.entity.Course;


public class UploadDto {

    private int index;
    private Long id;
    private String title;
    private String url;
    private String course;
    private String uploadDate;
    private String message;

    public UploadDto() {
    }

    public UploadDto(int index,Long id, String title, String url, String course, String uploadDate, String message) {
        this.index = index;
        this.id = id;
        this.title = title;
        this.url = url;
        this.course = course;
        this.uploadDate = uploadDate;
        this.message = message;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
