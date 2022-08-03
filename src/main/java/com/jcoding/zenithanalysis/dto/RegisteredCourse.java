package com.jcoding.zenithanalysis.dto;

import com.jcoding.zenithanalysis.entity.Course;
import javax.persistence.Entity;

public class RegisteredCourse {

    private int index;
    private Long id;
    private String course;
    private String price;
    private Integer numberOfStudents;

    public RegisteredCourse() {
    }

    public RegisteredCourse(int index,Long id, String course,String price, Integer numberOfStudents) {
        this.index = index;
        this.id = id;
        this.course = course;
        this.numberOfStudents = numberOfStudents;
        this.price = price;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
