package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.Comment;
import com.freakflow.backend.domain.model.Tag;
import com.freakflow.backend.domain.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentRepository {


}
