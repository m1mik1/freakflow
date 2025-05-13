package com.freakflow.backend.application.dto.response;

import java.time.Instant;
import java.util.List;

public class QuestionResponse {
    public Long id;
    public String title;
    public String slug;
    public String body;
    public List<String> tags;
    public Instant createdAt;
    public Instant updatedAt;
}
