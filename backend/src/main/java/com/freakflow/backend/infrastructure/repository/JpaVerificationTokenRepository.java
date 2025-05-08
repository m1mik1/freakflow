package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.EmailVerificationToken;
import com.freakflow.backend.domain.repository.VerificationTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long>, VerificationTokenRepository {
}

