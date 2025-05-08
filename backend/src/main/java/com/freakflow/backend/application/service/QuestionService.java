package com.freakflow.backend.application.service;

import com.freakflow.backend.application.dto.CreateQuestionDto;
import com.freakflow.backend.application.dto.QuestionDto;
import com.freakflow.backend.application.dto.QuestionSummaryDto;
import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.QuestionRepository;
import com.freakflow.backend.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;

    public Page<QuestionSummaryDto> listQuestions(int page, int size, String tag){
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

        Page<QuestionSummaryDto> dtoPage = questionPage.map(q -> {
            QuestionSummaryDto dto = new QuestionSummaryDto();
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

    public QuestionDto createQuestion(User author,CreateQuestionDto createQuestionDto) {
        log.info("createQuestion called: title='{}', author='{}'", createQuestionDto.title, author);

        Question question = new Question();
        question.setAuthor(author);
        question.setTitle(createQuestionDto.title);
        question.setBody(createQuestionDto.body);

        if (createQuestionDto.tags != null && !createQuestionDto.tags.isEmpty()) {
            var tags = createQuestionDto.tags.stream()
                    .map(name -> tagRepository.findByName_Value(name)
                            .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + name)))
                    .collect(Collectors.toSet());
            question.setTags(tags);
        }


        Question savedQuestion = questionRepository.save(question);
        log.info("Question with id: {} created", savedQuestion.getId());

        QuestionDto dto = new QuestionDto();
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
