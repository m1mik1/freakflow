package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.Comment;
import com.freakflow.backend.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepository {
    Page<Comment> findByQuestionId(Long questionId, Pageable pageable);
    long countByQuestionId(Long questionId);
    boolean existsByQuestionId(Long id);
    Page<Comment> findByAnswerId(Long answerId, Pageable pageable);
    long countByAnswerId(Long answerId);
    boolean existsByAnswerId(Long id);
    Optional<Comment> findById(Long id);
    Page<Comment> findByAuthor(User author, Pageable pageable);
    Comment save(Comment comment);
    void delete(Comment comment);

}
