package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.Badge;
import com.freakflow.backend.domain.repository.BadgeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBadgeRepository extends JpaRepository<Badge, Long>, BadgeRepository {
}
