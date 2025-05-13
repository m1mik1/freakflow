package com.freakflow.backend.application.dto.response;

import java.time.Instant;
import java.util.List;

public class QuestionSummaryResponse {
    public Long id;
    public String title;
    public String slug;
    public int answersCount;
    public List<String> tags;
    public Instant createdAt;

}
