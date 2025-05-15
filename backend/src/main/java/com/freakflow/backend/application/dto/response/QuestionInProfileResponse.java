package com.freakflow.backend.application.dto.response;

import java.time.Instant;

public class QuestionInProfileResponse {
    public Long id;
    public String title;
    public String slug;
    public long answersCount;
    public long votesCount;
    public Instant createdAt;
}
