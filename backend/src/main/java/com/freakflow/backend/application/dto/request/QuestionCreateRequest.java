package com.freakflow.backend.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class QuestionCreateRequest {
    @NotBlank @Size(min=5, max=100)
    public String title;

    @NotBlank @Size(min=10)
    public String body;

    @NotNull
    @Size(min=1)
    public List<@NotBlank String> tags;

}
