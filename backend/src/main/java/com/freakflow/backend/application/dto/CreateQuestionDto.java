package com.freakflow.backend.application.dto;

import com.freakflow.backend.domain.model.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class CreateQuestionDto {
    public String title;
    public String body;
    public List<String> tags;
}
