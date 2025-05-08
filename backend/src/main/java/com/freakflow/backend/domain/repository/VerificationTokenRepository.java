package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.EmailVerificationToken;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.valueobject.EmailAddress;

import java.util.Optional;

public interface VerificationTokenRepository {
    EmailVerificationToken save(EmailVerificationToken token);
    Optional<EmailVerificationToken> findByUserEmailAndCode(EmailAddress email, String code);
    void deleteByUser(User user);
}
