package com.jcoding.zenithanalysis.entity;

import javax.persistence.*;

@Entity
public class RegisterCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;
    private String registeredDate;
    private boolean approved;

    public RegisterCourse() {
    }

    public RegisterCourse(AppUser user, Course course, String registeredDate) {
        this.user = user;
        this.course = course;
        this.registeredDate = registeredDate;
        this.approved = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }


    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
