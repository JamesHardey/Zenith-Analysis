package com.jcoding.zenithanalysis.dto;

import com.jcoding.zenithanalysis.entity.Course;
import javax.persistence.Entity;

public class RegisteredCourse {

    private String course;
    private Integer numberOfStudents;

    public RegisteredCourse() {
    }

    public RegisteredCourse(String course, Integer numberOfStudents) {
        this.course = course;
        this.numberOfStudents = numberOfStudents;
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

}
