package com.example.jobportal.security;

import com.example.jobportal.service.OtpService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OtpVerificationFilter extends OncePerRequestFilter {

    private final OtpService otpService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean authenticated = auth != null && auth.isAuthenticated()
            && !"anonymousUser".equals(auth.getPrincipal());
        boolean otpPath = path.equals("/verify-otp") || path.equals("/logout")
            || path.equals("/login") || path.equals("/error")
            || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")
            || path.startsWith("/uploads/") || path.startsWith("/sample-resumes/");

        if (authenticated && !otpPath && !otpService.isVerified(request.getSession())) {
            response.sendRedirect("/verify-otp");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
