package com.example.jobportal.service;

import com.example.jobportal.entity.Notification;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    /**
     * Mock email notification. Replace with JavaMailSender, SendGrid, or Mailgun for production.
     */
    public void sendEmail(String to, String subject, String body) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                if (fromEmail != null && !fromEmail.isBlank()) {
                    message.setFrom(fromEmail);
                }
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                log.info("Email sent to {} with subject {}", to, subject);
                return;
            } catch (Exception e) {
                log.warn("Unable to send email through SMTP. Falling back to console log: {}", e.getMessage());
            }
        }
        log.info("=== EMAIL NOTIFICATION ===");
        log.info("To      : {}", to);
        log.info("Subject : {}", subject);
        log.info("Body    : {}", body);
        log.info("==========================");
    }

    public void sendApplicationReceivedEmail(String applicantEmail, String jobTitle) {
        sendEmail(
            applicantEmail,
            "Application Received - " + jobTitle,
            "Thank you for applying to \"" + jobTitle + "\". Your application has been received and is under review."
        );
    }

    public void sendStatusUpdateEmail(String applicantEmail, String jobTitle, String status) {
        sendEmail(
            applicantEmail,
            "Application Update - " + jobTitle,
            "Your application for \"" + jobTitle + "\" has been updated. New status: " + status + "."
        );
    }

    public void sendSms(String to, String body) {
        log.info("=== SMS NOTIFICATION ===");
        log.info("To   : {}", to);
        log.info("Body : {}", body);
        log.info("========================");
    }

    public Notification notifyUser(User user, String title, String message, String link) {
        return notificationRepository.save(Notification.builder()
            .user(user)
            .title(title)
            .message(message)
            .link(link)
            .build());
    }

    public List<Notification> latestFor(User user) {
        return notificationRepository.findTop8ByUserOrderByCreatedAtDesc(user);
    }

    public long unreadCount(User user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    public void markRead(Long id, User user) {
        notificationRepository.findById(id).ifPresent(notification -> {
            if (notification.getUser().getId().equals(user.getId())) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        });
    }
}
