package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TagRepository {
    Page<Tag> findAll(Pageable pageable);
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName_Value(String value);
    Page<Tag> findByQuestionsId(Long questionId, Pageable pageable);
    long countByQuestionsId(Long questionId);
    boolean existsByName(String name);
    Tag save(Tag tag);
    void delete(Tag tag);
}
