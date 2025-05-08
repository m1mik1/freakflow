package com.freakflow.backend.domain.repository;

import com.freakflow.backend.domain.model.Notification;
import com.freakflow.backend.domain.model.NotificationType;
import com.freakflow.backend.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepository {
    Page<Notification> findByUser(User user, Pageable pageable);
    Optional<Notification> findById(Long id);
    Page<Notification> findByType(NotificationType type, Pageable pageable);
    Page<Notification> findByUserAndReadFalse(User user, Pageable pageable);
    long countByUserAndReadFalse(User user);
    boolean existsByUserAndReadFalse(User user);
    Notification save(Notification notification);
    void delete(Notification notification);
}
