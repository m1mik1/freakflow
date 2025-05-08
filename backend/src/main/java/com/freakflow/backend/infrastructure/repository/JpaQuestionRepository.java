package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaQuestionRepository extends JpaRepository<Question, Long>, QuestionRepository {


    @Query("""
  select q
    from Question q
   where lower(q.title) like lower(concat('%', :q, '%'))
      or lower(q.body)  like lower(concat('%', :q, '%'))
""")
    Page<Question> searchByTitleOrBody(@Param("q") String query, Pageable pageable);

}
