package com.jcoding.zenithanalysis.dto;

public class ResumeUploadDto {

    private String type;
    private String pdfLocation;

    public ResumeUploadDto() {
    }

    public ResumeUploadDto(String type, String pdfLocation) {
        this.type = type;
        this.pdfLocation = pdfLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPdfLocation() {
        return pdfLocation;
    }

    public void setPdfLocation(String pdfLocation) {
        this.pdfLocation = pdfLocation;
    }
}
