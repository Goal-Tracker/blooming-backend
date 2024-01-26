package com.backend.blooming.notification.infrastructure.repository;

import com.backend.blooming.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiverId(final Long userId);
}
