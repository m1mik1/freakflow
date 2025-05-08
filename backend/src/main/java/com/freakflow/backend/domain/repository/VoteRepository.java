package com.freakflow.backend.domain.repository;


import com.freakflow.backend.domain.model.Answer;
import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.model.Vote;

import java.util.Optional;

public interface VoteRepository {
    Optional<Vote> findById(Long id);
    Optional<Vote> findByAuthorAndQuestion(User author, Question question);
    Optional<Vote> findByAuthorAndAnswer(User author, Answer answer);
    Vote save(Vote vote);
    void delete(Vote vote);
    void deleteByAuthorAndQuestion(User author, Question question);
    void deleteByAuthorAndAnswer(User author, Answer answer);
}
