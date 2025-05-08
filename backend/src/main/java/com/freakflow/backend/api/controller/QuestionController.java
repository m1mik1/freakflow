package com.freakflow.backend.api.controller;

import com.freakflow.backend.application.dto.CreateQuestionDto;
import com.freakflow.backend.application.dto.QuestionDto;
import com.freakflow.backend.application.dto.QuestionSummaryDto;
import com.freakflow.backend.application.service.QuestionService;
import com.freakflow.backend.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public Page<QuestionSummaryDto> getQuestions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String tag) {
        return questionService.listQuestions(page, size, tag);
    }

    @PostMapping
    public QuestionDto createQuestion(@AuthenticationPrincipal User currentUser, @Valid @RequestBody CreateQuestionDto createQuestionDto) {

        return questionService.createQuestion(currentUser,createQuestionDto);
    }
}
