package com.freakflow.backend.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerificationRequestDto {
    @Email
    @NotBlank
    public String email;

    @Pattern(regexp="\\d{6}") // ровно 6 цифр
    public String code;
}
