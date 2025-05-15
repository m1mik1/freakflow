package com.freakflow.backend.application.service;

import com.freakflow.backend.application.dto.request.QuestionCreateRequest;
import com.freakflow.backend.application.dto.response.*;
import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.QuestionRepository;
import com.freakflow.backend.domain.repository.TagRepository;
import com.freakflow.backend.domain.repository.UserRepository;
import com.freakflow.backend.infrastructure.repository.specification.QuestionSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
    private final UserRepository userRepository;
    private final JpaSpecificationExecutor<Question> jpaSpecificationExecutor;


    public Page<QuestionSummaryResponse> listQuestions(int page, int size, String sortBy, String filter) {

        log.info("listQuestions called: page={}, size={}, sortBy={}, filter={}", page, size, sortBy, filter);

        String sortField = sortBy;
        if (sortField == null || sortField.isBlank()) {
            sortField = "createdAt";
        }

        Sort sort;
        switch (sortField) {
            case "answersCount":
                sort = Sort.by(Sort.Direction.DESC, "answers.size");
                break;
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }
        Pageable pageable = PageRequest.of(page, size, sort);


        Page<Question> questionPage;

        if ("unanswered".equalsIgnoreCase(filter)) {
            log.debug("Filtering unanswered questions");
            questionPage = questionRepository.findByAnswersIsEmpty(pageable);
        } else {
            log.debug("Fetching all questions");
            questionPage = questionRepository.findAll(pageable);
        }

        log.info("Fetched {} questions of {} total", questionPage.getNumberOfElements(), questionPage.getTotalElements());

        Page<QuestionSummaryResponse> dtoPage = questionPage.map(q -> {
            QuestionSummaryResponse dto = new QuestionSummaryResponse();
            dto.id = q.getId();
            dto.title = q.getTitle();
            dto.slug = q.getSlug().getValue();
            dto.body = q.getBody();
            dto.answersCount = q.getAnswers().size();
            dto.votesCount = q.getVotes().size();
            dto.createdAt = q.getCreatedAt();
            dto.tags = q.getTags().stream().map(tag1 -> tag1.getName().getValue()).toList();
            dto.author=q.getAuthor().getName();
            return dto;
        });
        return dtoPage;
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        QuestionResponse dto = new QuestionResponse();

        dto.id = question.getId();
        dto.title = question.getTitle();
        dto.body = question.getBody();
        dto.slug = question.getSlug().getValue();
        dto.createdAt = question.getCreatedAt();
        dto.updatedAt = question.getUpdatedAt();
        dto.tags = question.getTags().stream().map(tag -> tag.getName().getValue()).toList();

        return dto;
    }

    public QuestionsInfoResponse getQuestionsInfo() {
        QuestionsInfoResponse dto = new QuestionsInfoResponse();
        dto.questionsCount = questionRepository.count();
        dto.unansweredCount = questionRepository.countByAnswersIsEmpty();
        return dto;
    }

    public Page<QuestionSummaryResponse> searchQuestions(String query, int page, int size) {
        String decoded = URLDecoder.decode(query, StandardCharsets.UTF_8);
        log.info(" Searching questions – query='{}', page={}, size={}", query, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        List<String> tagsFilter = parseTags(decoded);
        String textFilter = parseText(decoded);

        Specification<Question> tagSpec = null;
        if (!tagsFilter.isEmpty()) {
            log.info("Tag filter: {}", tagsFilter);
            tagSpec = QuestionSpecs.hasAnyTag(tagsFilter);
        }

        // 2) создаём спецификацию для текста (если есть)
        Specification<Question> textSpec = null;
        if (textFilter != null && !textFilter.isBlank()) {
            log.info("Text filter: '{}'", textFilter);
            textSpec = QuestionSpecs.titleOrBodyContains(textFilter);
        }

        Specification<Question> spec;
        if (tagSpec != null && textSpec != null) {
            spec = tagSpec.and(textSpec);
        } else if (tagSpec != null) {
            spec = tagSpec;
        } else if (textSpec != null) {
            spec = textSpec;
        } else {
            spec = Specification.where(null);
        }


        Page<Question> questions = jpaSpecificationExecutor.findAll(spec, pageable);
        Page<QuestionSummaryResponse> dtoPage = questions.map(q -> {
            var dto = new QuestionSummaryResponse();
            dto.id = q.getId();
            dto.title = q.getTitle();
            dto.slug = q.getSlug().getValue();
            dto.body = q.getBody();
            dto.answersCount = q.getAnswers().size();
            dto.votesCount = q.getVotes().size();
            dto.createdAt = q.getCreatedAt();
            dto.tags = q.getTags().stream().map(tag1 -> tag1.getName().getValue()).toList();
            dto.author=q.getAuthor().getName();
            return dto;
        });
        log.info("Returning {} DTOs to client", dtoPage.getNumberOfElements());
        return dtoPage;

    }

    public QuestionSearchResponse searchCombined(String query, int page, int size) {

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
        log.info("Combined search – query='{}', bannerPresent={}, questions={}", query, banner != null, questions.getNumberOfElements());
        return dto;
    }

    private List<String> parseTags(String query) {
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
    public QuestionResponse createQuestion(Long userId, QuestionCreateRequest questionCreateRequest) {
        log.info("createQuestion called: title='{}'", questionCreateRequest.title);
        User user= userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Question question = new Question();
        question.setAuthor(user);
        question.setTitle(questionCreateRequest.title);
        question.setBody(questionCreateRequest.body);

        if (questionCreateRequest.tags != null && !questionCreateRequest.tags.isEmpty()) {
            var tags = questionCreateRequest.tags.stream().map(name -> tagRepository.findByName_Value(name).orElseThrow(() -> new IllegalArgumentException("Tag not found: " + name))).collect(Collectors.toSet());
            question.setTags(tags);
        }


        Question savedQuestion = questionRepository.save(question);
        log.info("Question with id: {} created", savedQuestion.getId());

        QuestionResponse dto = new QuestionResponse();
        dto.id = savedQuestion.getId();
        dto.title = savedQuestion.getTitle();
        dto.slug = savedQuestion.getSlug().getValue();
        dto.createdAt = savedQuestion.getCreatedAt();
        dto.updatedAt = savedQuestion.getUpdatedAt();
        dto.body = savedQuestion.getBody();
        dto.tags = savedQuestion.getTags().stream().map(tag -> tag.getName().getValue()).toList();

        return dto;
    }


}
