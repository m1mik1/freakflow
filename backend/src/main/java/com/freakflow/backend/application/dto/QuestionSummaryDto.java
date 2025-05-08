package com.freakflow.backend.application.dto;

import com.freakflow.backend.domain.model.Question;

import java.time.Instant;
import java.util.List;

public class QuestionSummaryDto {
    public Long id;
    public String title;
    public String slug;
    public int answersCount;
    public List<String> tags;
    public Instant createdAt;

}
