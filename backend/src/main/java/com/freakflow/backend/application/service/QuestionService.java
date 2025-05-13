package com.freakflow.backend.application.service;

import com.freakflow.backend.application.dto.request.QuestionCreateRequest;
import com.freakflow.backend.application.dto.response.QuestionResponse;
import com.freakflow.backend.application.dto.response.QuestionSearchResponse;
import com.freakflow.backend.application.dto.response.QuestionSummaryResponse;
import com.freakflow.backend.application.dto.response.TagResponse;
import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.QuestionRepository;
import com.freakflow.backend.domain.repository.TagRepository;
import com.freakflow.backend.infrastructure.repository.specification.QuestionSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final JpaSpecificationExecutor<Question> jpaSpecificationExecutor;


    public Page<QuestionSummaryResponse> listQuestions(int page, int size, String tag){
        log.info("listQuestions called: page={}, size={}, tag={}", page, size, tag);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Question> questionPage;
        if (tag ==null || tag.isEmpty()){
            log.debug("Tag is null or empty, fetching all questions");
            questionPage= questionRepository.findAll(pageable);
        }
        else{
            log.debug("Fetching questions filtered by tag: {}", tag);
            questionPage=questionRepository.findByTagsName(tag, pageable);
        }
        log.info("Fetched {} questions of {} total",
                questionPage.getNumberOfElements(),
                questionPage.getTotalElements());

        Page<QuestionSummaryResponse> dtoPage = questionPage.map(q -> {
            QuestionSummaryResponse dto = new QuestionSummaryResponse();
            dto.id=q.getId();
            dto.title=q.getTitle();
            dto.slug=q.getSlug().getValue();
            dto.answersCount=q.getAnswers().size();
            dto.createdAt=q.getCreatedAt();
            dto.tags=q.getTags().stream().map(tag1 -> tag1.getName().getValue()).toList();
            return dto;
        });
        return dtoPage;
    }

    public QuestionResponse getQuestionById(Long id){
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Question not found"));
        QuestionResponse dto = new QuestionResponse();

        dto.id=question.getId();
        dto.title=question.getTitle();
        dto.body=question.getBody();
        dto.slug=question.getSlug().getValue();
        dto.createdAt=question.getCreatedAt();
        dto.updatedAt=question.getUpdatedAt();
        dto.tags=question.getTags().stream().map(tag -> tag.getName().getValue()).toList();

        return dto;
    }

    public Page<QuestionSummaryResponse> searchQuestions(String query, int page, int size){
        log.info(" Searching questions – query='{}', page={}, size={}", query, page + 1, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        List<String> tagsFilter=  parseTags(query);
        String textFilter = parseText(query);

        Specification<Question> spec = Specification.where(null);

        if (!tagsFilter.isEmpty()) {
            log.info("Tag filter: {}", tagsFilter);
            spec = spec.and(QuestionSpecs.hasAllTags(tagsFilter));
        }
        if (textFilter != null && !textFilter.isBlank()) {
            log.info("Text search: '{}'", textFilter);
            spec = spec.and(QuestionSpecs.titleOrBodyContains(textFilter));
        }

        Page<Question> questions = jpaSpecificationExecutor.findAll(spec, pageable);
        Page<QuestionSummaryResponse> dtoPage = questions.map(q -> {
            var dto = new QuestionSummaryResponse();
            dto.id = q.getId();
            dto.title = q.getTitle();
            dto.slug = q.getSlug().getValue();
            dto.answersCount = q.getAnswers().size();
            dto.createdAt = q.getCreatedAt();
            dto.tags = q.getTags().stream()
                    .map(tag -> tag.getName().getValue())
                    .toList();
            return dto;
        });
        log.info("Returning {} DTOs to client", dtoPage.getNumberOfElements());
        return dtoPage;

    }

    public QuestionSearchResponse searchCombined(String query, int page, int size){

        Page<QuestionSummaryResponse> questions = searchQuestions(query, page, size);
        List<String> tags = parseTags(query);
        String text = parseText(query);
        TagResponse banner = null;
        if (tags.size() == 1 && (text == null || text.isBlank())) {
            banner = tagService.getByName(tags.get(0));
        }
        QuestionSearchResponse dto = new QuestionSearchResponse();
        dto.tagBanner = banner;          // null → отвечаем без баннера
        dto.questions = questions;
        log.info("Combined search – query='{}', bannerPresent={}, questions={}",
                query,
                banner != null,
                questions.getNumberOfElements());
        return dto;
    }

    private List<String> parseTags(String query){
        List<String> tags = new ArrayList<>();
        if (query != null) {
            Matcher m = Pattern.compile("\\[([^\\]]+)\\]").matcher(query);
            while (m.find()) {
                tags.add(m.group(1));
            }
        }
        return tags;
    }

    private String parseText(String query) {
        if (query == null) return null;
        return query.replaceAll("\\[[^\\]]+\\]", "").trim();
    }


    @Transactional
    public QuestionResponse createQuestion(User author, QuestionCreateRequest questionCreateRequest) {
        log.info("createQuestion called: title='{}', author='{}'", questionCreateRequest.title, author);

        Question question = new Question();
        question.setAuthor(author);
        question.setTitle(questionCreateRequest.title);
        question.setBody(questionCreateRequest.body);

        if (questionCreateRequest.tags != null && !questionCreateRequest.tags.isEmpty()) {
            var tags = questionCreateRequest.tags.stream()
                    .map(name -> tagRepository.findByName_Value(name)
                            .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + name)))
                    .collect(Collectors.toSet());
            question.setTags(tags);
        }


        Question savedQuestion = questionRepository.save(question);
        log.info("Question with id: {} created", savedQuestion.getId());

        QuestionResponse dto = new QuestionResponse();
        dto.id=savedQuestion.getId();
        dto.title=savedQuestion.getTitle();
        dto.slug=savedQuestion.getSlug().getValue();
        dto.createdAt=savedQuestion.getCreatedAt();
        dto.updatedAt=savedQuestion.getUpdatedAt();
        dto.body=savedQuestion.getBody();
        dto.tags=savedQuestion.getTags().stream().map(tag -> tag.getName().getValue()).toList();

        return dto;
    }


}
