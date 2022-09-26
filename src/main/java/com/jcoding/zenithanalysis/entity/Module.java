package com.jcoding.zenithanalysis.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String moduleTitle;
    private String moduleDetails;

    public Module(){

    }

    public Module(String moduleTitle, String moduleDetails) {
        this.moduleTitle = moduleTitle;
        this.moduleDetails = moduleDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public String getModuleDetails() {
        return moduleDetails;
    }

    public void setModuleDetails(String moduleDetails) {
        this.moduleDetails = moduleDetails;
    }
}
