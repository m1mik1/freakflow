package com.freakflow.backend.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagCreateRequest {
    @NotBlank @Size(max=30)
    public String name;

    @Size(max=200)
    public String description;

}
