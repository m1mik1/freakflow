package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.valueobject.EmailAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    boolean existsByEmail(EmailAddress email);
    List<User> findByEnabledFalseAndCreatedAtBefore(Instant cutoff);
    boolean existsByNameAndEnabledTrue(String name);
    Optional<User> findByEmail(EmailAddress email);
    boolean existsByName(String name);
    Optional<User> findByName(String name);
    Page<User> findByNameContainingIgnoreCase(String fragment, Pageable pageable);
    User save(User user);
    void delete(User user);
}
