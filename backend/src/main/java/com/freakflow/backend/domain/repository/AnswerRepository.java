package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.Answer;
import com.freakflow.backend.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AnswerRepository {
    Optional<Answer> findById(Long id);
    Page<Answer> findByQuestionId(Long questionId, Pageable pageable);
    Page<Answer> findByAuthor(User author, Pageable pageable);
    Optional<Answer> findByQuestionIdAndAcceptedTrue(Long questionId);
    boolean existsByQuestionIdAndAcceptedTrue(Long questionId);
    long countByQuestionId(Long questionId);
    Answer save(Answer answer);
    void delete(Answer answer);

}
