package com.freakflow.backend.api.controller;

import com.freakflow.backend.application.dto.request.QuestionCreateRequest;
import com.freakflow.backend.application.dto.response.QuestionResponse;
import com.freakflow.backend.application.dto.response.QuestionSummaryResponse;
import com.freakflow.backend.application.dto.response.QuestionSearchResponse;
import com.freakflow.backend.application.service.QuestionService;
import com.freakflow.backend.domain.model.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public Page<QuestionSummaryResponse> getQuestions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String tag) {
        return questionService.listQuestions(page, size, tag);
    }

    @GetMapping("/{id}/{slug}")
    public ResponseEntity<QuestionResponse> getQuestion(
            @PathVariable Long id,
            @PathVariable String slug
    ) {
        QuestionResponse dto = questionService.getQuestionById(id);

        String realSlug = dto.slug;
        if (realSlug == null || !realSlug.equals(slug)) {
            URI correct = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/questions/{id}/{slug}")
                    .buildAndExpand(id, realSlug)
                    .toUri();

            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .location(correct)
                    .build();
        }

        return ResponseEntity
                .ok()
                .body(dto);
    }

    @GetMapping("/search")
    public ResponseEntity<QuestionSearchResponse> search(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        QuestionSearchResponse result = questionService.searchCombined(q, page, size);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody QuestionCreateRequest dto
    ) {
        QuestionResponse created = questionService.createQuestion(currentUser, dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id)
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

}
