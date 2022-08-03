package com.jcoding.zenithanalysis.entity;

import javax.persistence.*;

@Entity
public class Events {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String details;
    private String date;
    private String time;


    public Events() {
    }

    public Events(String title, String details, String date, String time) {
        this.title = title;
        this.details = details;
        this.date = date;
        this.time = time;
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


}
