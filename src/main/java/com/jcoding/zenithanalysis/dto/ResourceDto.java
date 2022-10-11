package com.jcoding.zenithanalysis.dto;

public class ResourceDto {

    private Long id;
    private String resourceType;
    private String title;
    private String documentUrl;
    private String uploadDate;

    public ResourceDto() {
    }

    public ResourceDto(Long id,String resourceType, String title, String documentUrl, String uploadDate) {
        this.id = id;
        this.title = title;
        this.resourceType = resourceType;
        this.documentUrl = documentUrl;
        this.uploadDate = uploadDate;
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

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
