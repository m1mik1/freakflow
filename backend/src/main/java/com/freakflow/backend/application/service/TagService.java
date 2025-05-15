package com.freakflow.backend.application.service;

import com.freakflow.backend.application.dto.request.TagCreateRequest;

import com.freakflow.backend.application.dto.response.TagResponse;
import com.freakflow.backend.domain.model.Tag;
import com.freakflow.backend.domain.repository.QuestionRepository;
import com.freakflow.backend.domain.repository.TagRepository;
import com.freakflow.backend.domain.valueobject.TagName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final QuestionRepository questionRepository;


    public Page<TagResponse> listTags(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name.value").ascending());

        Page<Tag> tags = tagRepository.findAll(pageable) ;

        Page<TagResponse> dtoPage = tags.map(tag -> {
            TagResponse dto = new TagResponse();
            dto.id = tag.getId();
            dto.name=tag.getName().getValue();
            dto.description=tag.getDescription();
            dto.questions=questionRepository.countByTags_Id(tag.getId());
            return dto;
        });
        log.info("Listing tags: page={} size={} → returned {} items",
                page, size, dtoPage.getNumberOfElements());
        return dtoPage;
    }

    @Transactional
    public TagResponse createTag(TagCreateRequest tagDto) {
        if (tagRepository.existsByName_Value(tagDto.name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }

        Tag tag = new Tag();
        tag.setName(new TagName(tagDto.name));
        tag.setDescription(tagDto.description);
        Tag savedTag = tagRepository.save(tag);

        TagResponse dto = new TagResponse();
        dto.id = savedTag.getId();
        dto.name=savedTag.getName().getValue();
        dto.description=savedTag.getDescription();
        return dto;
    }

    public TagResponse getByName(String name) {
        Tag tag = tagRepository.findByName_Value(name).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tag not found"));

        TagResponse dto = new TagResponse();
        dto.id = tag.getId();
        dto.name=tag.getName().getValue();
        dto.description=tag.getDescription();
        dto.questions=questionRepository.countByTags_Id(tag.getId());
        return dto;
    }

}
