package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.Answer;
import com.freakflow.backend.domain.repository.AnswerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAnswerRepository extends JpaRepository<Answer,Long>, AnswerRepository {

}
