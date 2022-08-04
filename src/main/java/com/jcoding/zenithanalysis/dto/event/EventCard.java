package com.jcoding.zenithanalysis.dto.event;

import java.time.LocalTime;

public class EventCard {

    private String year;
    private String month;
    private String day;
    private LocalTime time;
    private String title;
    private String details;

    public EventCard() {
    }

    public EventCard(String year, String month, String day, LocalTime time, String title, String details) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.title = title;
        this.details = details;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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
}
