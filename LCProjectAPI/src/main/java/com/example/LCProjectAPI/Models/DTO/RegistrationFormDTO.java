package com.example.LCProjectAPI.Models.DTO;

import javax.validation.constraints.NotBlank;

public class RegistrationFormDTO extends LoginFormDTO {

    @NotBlank(message = "Please confirm your password")
    private String verifyPassword;

    // Getters and Setters

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
