package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.Vote;
import com.freakflow.backend.domain.repository.VoteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVoteRepository extends JpaRepository<Vote,Long>, VoteRepository {
}
