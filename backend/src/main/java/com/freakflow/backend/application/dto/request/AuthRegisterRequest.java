package com.freakflow.backend.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRegisterRequest {

    @NotBlank @Size(min=3, max=50)
    public String name;

    @Email @NotBlank
    public String email;

    @NotBlank @Size(min=8)
    public String password;

    @NotBlank
    public String confirmPassword;

}
