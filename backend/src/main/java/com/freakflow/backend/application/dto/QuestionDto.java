package com.freakflow.backend.application.dto;

import com.freakflow.backend.domain.model.Answer;
import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.Tag;
import com.freakflow.backend.domain.model.Vote;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public class QuestionDto {
    public Long id;
    public String title;
    public String slug;
    public String body;
    public List<String> tags;
    public Instant createdAt;
    public Instant updatedAt;
}
