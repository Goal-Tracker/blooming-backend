package com.backend.blooming.notification.infrastructure.repository;

import com.backend.blooming.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
