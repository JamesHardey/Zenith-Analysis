package com.jcoding.zenithanalysis.dto.user;

public class NewAdminDto {
    private String password;
    private String email;
    private String name;
    private String adminPassword;

    public NewAdminDto() {
    }

    public NewAdminDto(String password, String email,String name, String adminPassword) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.adminPassword = adminPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
