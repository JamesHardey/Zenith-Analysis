package com.jcoding.zenithanalysis.dto;

import com.jcoding.zenithanalysis.entity.Course;

public class EventsDto {

    private int index;
    private Long id;
    private String title;
    private String details;
    private String date;
    private String time;
    private String course;

    public EventsDto() {
    }

    public EventsDto(int index,Long id, String title, String details, String date, String time, String course) {
        this.index = index;
        this.id = id;
        this.title = title;
        this.details = details;
        this.date = date;
        this.time = time;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
