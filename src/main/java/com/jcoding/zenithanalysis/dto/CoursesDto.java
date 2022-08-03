package com.jcoding.zenithanalysis.dto;

public class CoursesDto {

    private Long id;
    private String title;
    private String price;
    private String details;
    private Integer participants;

    public CoursesDto() {
    }

    public CoursesDto(Long id, String title, String price, String details) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.details = details;
    }

    public CoursesDto(Long id, String title, String price, String details, Integer participants) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.details = details;
        this.participants = participants;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }
}
