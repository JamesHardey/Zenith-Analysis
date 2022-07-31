package com.jcoding.zenithanalysis.dto;

public class VerifyUser {

    private Long id;
    private String code;

    public VerifyUser() {
    }

    public VerifyUser(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
