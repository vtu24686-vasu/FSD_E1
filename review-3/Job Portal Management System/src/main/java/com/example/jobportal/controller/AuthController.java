package com.example.jobportal.controller;

import com.example.jobportal.dto.UserDto;
import com.example.jobportal.entity.Role;
import com.example.jobportal.service.OtpService;
import com.example.jobportal.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null)  model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roles", new Role[]{Role.STUDENT, Role.EMPLOYER});
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto,
                           RedirectAttributes redirectAttributes) {
        try {
            userService.register(userDto);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/verify-otp")
    public String otpPage(Authentication auth, HttpSession session, Model model) {
        if (auth == null) {
            return "redirect:/login";
        }
        if (otpService.isVerified(session)) {
            return "redirect:/dashboard";
        }
        model.addAttribute("email", auth.getName());
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp,
                            Authentication auth,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (auth == null) {
            return "redirect:/login";
        }
        if (otpService.verify(auth.getName(), otp, session)) {
            return "redirect:/dashboard";
        }
        redirectAttributes.addFlashAttribute("error", "Invalid or expired OTP. Please check your email and try again.");
        return "redirect:/verify-otp";
    }

    @PostMapping("/verify-otp/resend")
    public String resendOtp(Authentication auth, HttpSession session, RedirectAttributes redirectAttributes) {
        if (auth == null) {
            return "redirect:/login";
        }
        otpService.createAndSend(auth.getName(), session);
        redirectAttributes.addFlashAttribute("message", "A new OTP has been sent to your email.");
        return "redirect:/verify-otp";
    }
}
