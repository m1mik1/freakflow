package com.freakflow.backend.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthLoginRequest {
    @Email
    @NotBlank
    public String email;

    @NotBlank
    public String password;

}
