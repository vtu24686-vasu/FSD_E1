package com.example.jobportal.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OtpService {

    public static final String OTP_CODE = "LOGIN_OTP_CODE";
    public static final String OTP_EMAIL = "LOGIN_OTP_EMAIL";
    public static final String OTP_EXPIRES_AT = "LOGIN_OTP_EXPIRES_AT";
    public static final String OTP_VERIFIED = "LOGIN_OTP_VERIFIED";

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long VALID_SECONDS = 300;

    private final NotificationService notificationService;

    public void createAndSend(String email, HttpSession session) {
        String code = String.valueOf(100000 + RANDOM.nextInt(900000));
        session.setAttribute(OTP_CODE, code);
        session.setAttribute(OTP_EMAIL, email);
        session.setAttribute(OTP_EXPIRES_AT, Instant.now().plusSeconds(VALID_SECONDS).toEpochMilli());
        session.setAttribute(OTP_VERIFIED, false);

        notificationService.sendEmail(
            email,
            "JobPortal Login OTP",
            "Your JobPortal login OTP is " + code + ". It is valid for 5 minutes."
        );
    }

    public boolean verify(String email, String code, HttpSession session) {
        Object expectedEmail = session.getAttribute(OTP_EMAIL);
        Object expectedCode = session.getAttribute(OTP_CODE);
        Object expiresAt = session.getAttribute(OTP_EXPIRES_AT);

        if (email == null || code == null || expectedEmail == null || expectedCode == null || expiresAt == null) {
            return false;
        }
        if (!email.equals(expectedEmail.toString()) || !code.trim().equals(expectedCode.toString())) {
            return false;
        }
        if (Instant.now().toEpochMilli() > (Long) expiresAt) {
            return false;
        }

        session.setAttribute(OTP_VERIFIED, true);
        session.removeAttribute(OTP_CODE);
        session.removeAttribute(OTP_EXPIRES_AT);
        return true;
    }

    public boolean isVerified(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute(OTP_VERIFIED));
    }
}
