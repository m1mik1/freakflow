package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.valueobject.Slug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    Page<Question> findAll(Pageable pageable);
    Page<Question> findByTagsName(String tagName, Pageable pageable);
    Page<Question> findByAuthor(User author, Pageable pageable);
    Page<Question> searchByTitleOrBody(String query, Pageable pageable);
    Optional<Question> findById(Long id);
    Optional<Question> findBySlugValue(String slugValue);
    Question save(Question question);
    void delete(Question question);
}
