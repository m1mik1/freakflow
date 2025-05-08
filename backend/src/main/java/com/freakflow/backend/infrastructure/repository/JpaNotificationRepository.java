package com.freakflow.backend.infrastructure.repository;

import com.freakflow.backend.domain.model.Notification;
import com.freakflow.backend.domain.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, Long> , NotificationRepository {
}
