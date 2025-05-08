package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.Badge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BadgeRepository {
    Page<Badge> findAll(Pageable pageable);
    Optional<Badge> findById(Long id);
    Optional<Badge> findByName(String name);
    boolean existsByName(String name);
    Badge save(Badge badge);
    void delete(Badge badge);
}
