package com.freakflow.backend.api.controller;


import com.freakflow.backend.application.dto.request.TagCreateRequest;
import com.freakflow.backend.application.dto.response.TagResponse;
import com.freakflow.backend.application.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/page")
    public Page<TagResponse> getTags(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        return tagService.listTags(page, size);
    }

    @GetMapping("/{name}")
    public TagResponse getTag(@PathVariable String name) {
        return tagService.getByName(name);
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagCreateRequest dto) {
        TagResponse created = tagService.createTag(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{name}")
                .buildAndExpand(created.name)
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
}
