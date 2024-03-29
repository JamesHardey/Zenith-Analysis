package com.jcoding.zenithanalysis.dto;

import com.jcoding.zenithanalysis.entity.Course;


public class UploadDto {

    private int index;
    private Long id;
    private String title;
    private String url;
    private String module;
    private String uploadDate;
    private String message;
    private String documentURL;

    public UploadDto() {
    }

    public UploadDto(int index,Long id, String title, String url, String module, String uploadDate, String message) {
        this.index = index;
        this.id = id;
        this.title = title;
        this.url = url;
        this.module = module;
        this.uploadDate = uploadDate;
        this.message = message;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }
}
