package com.jobportal.controller;

import com.jobportal.entity.Role;
import com.jobportal.entity.User;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/logged-out")
    public String loggedOut() {
        return "auth/logout";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register/student")
    public String registerStudent(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String skills,
                                  Model model) {
        userService.registerStudent(name, email, password, skills);
        model.addAttribute("success", "Student account created. Please login.");
        return "auth/login";
    }

    @PostMapping("/register/employer")
    public String registerEmployer(@RequestParam String companyName,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   Model model) {
        userService.registerEmployer(companyName, email, password);
        model.addAttribute("success", "Employer account created. Please login.");
        return "auth/login";
    }

    @GetMapping("/home")
    public String home(org.springframework.security.core.Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow();
        if (user.getRole() == Role.STUDENT) {
            return "redirect:/student/dashboard";
        }
        return "redirect:/employer/dashboard";
    }
}
