package com.example.jobportal.security;

import com.example.jobportal.service.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OtpAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OtpService otpService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        otpService.createAndSend(authentication.getName(), request.getSession());
        response.sendRedirect("/verify-otp");
    }
}
