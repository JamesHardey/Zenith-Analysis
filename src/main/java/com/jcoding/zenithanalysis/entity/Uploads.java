package com.jcoding.zenithanalysis.entity;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;


@Entity
public class Uploads {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String url;
    private String module;
    private String uploadDate;
    private String message;
    private String documentURL;


    public Uploads() {
    }

    public Uploads(String title, String url, String module, String uploadDate, String message) {
        this.title = title;
        this.url = url;
        this.module = module;
        this.uploadDate = uploadDate;
        this.message = message;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }
}
