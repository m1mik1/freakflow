package com.freakflow.backend.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerificationRequestDto {
    @Email
    @NotBlank
    public String email;

    @Pattern(regexp = "^[A-Za-z0-9]{6}$",
            message = "Код має містити рівно 6 літер або цифр")
    public String code;
}
