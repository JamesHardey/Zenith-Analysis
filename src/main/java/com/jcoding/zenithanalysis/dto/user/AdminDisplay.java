package com.jcoding.zenithanalysis.dto.user;

public class AdminDisplay {

    private String firstCharacter;
    private String name;
    private String email;

    public AdminDisplay() {
    }

    public AdminDisplay(String firstCharacter, String name, String email) {
        this.firstCharacter = firstCharacter;
        this.name = name;
        this.email = email;
    }

    public String getFirstCharacter() {
        return firstCharacter;
    }

    public void setFirstCharacter(String firstCharacter) {
        this.firstCharacter = firstCharacter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
