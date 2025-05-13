package com.freakflow.backend.application.dto.response;

import org.springframework.data.domain.Page;

public class QuestionSearchResponse {
    public TagResponse tagBanner;
    public Page<QuestionSummaryResponse> questions;
}
