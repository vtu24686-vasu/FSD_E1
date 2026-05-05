package com.example.jobportal.controller;

import com.example.jobportal.entity.Role;
import com.example.jobportal.entity.User;
import com.example.jobportal.service.ResumeParserService;
import com.example.jobportal.service.ResumeStorageService;
import com.example.jobportal.service.UserService;
import com.example.jobportal.service.UserFileService;
import com.example.jobportal.service.NotificationService;
import com.example.jobportal.service.SavedJobService;
import com.example.jobportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final UserService userService;
    private final ResumeStorageService resumeStorageService;
    private final ResumeParserService resumeParserService;
    private final UserFileService userFileService;
    private final NotificationService notificationService;
    private final SavedJobService savedJobService;
    private final JobService jobService;

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("files", userFileService.filesFor(user));
        return "student/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @RequestParam String name,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) String skills,
                                @RequestParam(required = false) String education,
                                @RequestParam(required = false) MultipartFile resume,
                                @RequestParam(required = false) java.util.List<MultipartFile> certificates,
                                RedirectAttributes ra) {
        try {
            User user = userService.findByEmail(auth.getName());
            if (user.getRole() != Role.STUDENT) {
                return "redirect:/dashboard";
            }
            user.setName(name);
            user.setPhone(phone);
            user.setAddress(address);
            user.setSkills(skills);
            user.setEducation(education);

            if (resume != null && !resume.isEmpty()) {
                ResumeStorageService.StoredResume stored = resumeStorageService.store(resume, user);
                user.setResumeFileName(stored.originalName());
                user.setResumePath(stored.publicPath());
                user.setParsedResume(resumeParserService.parseSkills(skills, stored.originalName()));
            }

            userService.updateProfile(user);
            userFileService.storeFiles(certificates, user, "CERTIFICATE");
            ra.addFlashAttribute("message", "Profile updated successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/profile";
    }

    @PostMapping("/notifications/{id}/read")
    public String markRead(@PathVariable Long id, Authentication auth) {
        notificationService.markRead(id, userService.findByEmail(auth.getName()));
        return "redirect:/dashboard";
    }

    @PostMapping("/saved/{jobId}/toggle")
    public String toggleSaved(@PathVariable Long jobId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        savedJobService.toggle(user, jobService.getById(jobId));
        return "redirect:/dashboard";
    }
}
