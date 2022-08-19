package com.jcoding.zenithanalysis.dto.course;

public class UserCourseDto {

    private Long index;
    private String title;
    private String details;

    public UserCourseDto() {
    }

    public UserCourseDto(Long index, String title, String details) {
        this.index = index;
        this.title = title;
        this.details = details;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
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
}
