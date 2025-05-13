package com.freakflow.backend.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VerificationResendRequest {
    @Email
    @NotBlank
    public String email;
}
