package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaQuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question>, QuestionRepository {

}
