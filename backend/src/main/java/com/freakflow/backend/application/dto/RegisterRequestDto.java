package com.freakflow.backend.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequestDto {

    @NotBlank
    public String name;
    @Email
    @NotBlank
    public String email;
    @NotBlank
    public String password;
    @NotBlank
    public String confirmPassword;
}
