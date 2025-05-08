package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> , UserRepository {
}
