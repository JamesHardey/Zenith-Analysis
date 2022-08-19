package com.jcoding.zenithanalysis.dto.contact;

public class ContactUsDto {

    private String name;
    private String email;
    private String phone;
    private String message;
    private String dateSent;

    public ContactUsDto() {
    }

    public ContactUsDto(String name, String email, String phone, String message, String dateSent) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.message = message;
        this.dateSent = dateSent;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }
}
