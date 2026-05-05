package com.example.jobportal.repository;

import com.example.jobportal.entity.Notification;
import com.example.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop8ByUserOrderByCreatedAtDesc(User user);
    long countByUserAndReadFalse(User user);
}
