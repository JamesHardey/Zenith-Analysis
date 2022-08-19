package com.jcoding.zenithanalysis.dto.course;

public class AllowedCourses {

    private Long id;
    private String title;
    private boolean status;

    public AllowedCourses() {
    }

    public AllowedCourses(Long id, String title, boolean status) {
        this.id = id;
        this.title = title;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
