package com.freakflow.backend.application.dto.response;

import java.time.Instant;
import java.util.List;

public class QuestionSummaryResponse {
    public Long id;
    public String title;
    public String slug;
    public String body;
    public long answersCount;
    public long votesCount;
    public List<String> tags;
    public Instant createdAt;
    public String author;

}
