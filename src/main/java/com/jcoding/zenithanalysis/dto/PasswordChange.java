package com.jcoding.zenithanalysis.dto;

public class PasswordChange {

    private String resetPassword;
    private String newPassword;
    private String confirmPassword;

    public PasswordChange() {
    }

    public PasswordChange(String resetPassword, String newPassword, String confirmPassword) {
        this.resetPassword = resetPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
