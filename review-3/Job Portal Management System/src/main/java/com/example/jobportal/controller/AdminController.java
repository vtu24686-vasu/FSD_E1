package com.example.jobportal.controller;

import com.example.jobportal.entity.Job;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final JobService jobService;

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.toggleActive(id);
        ra.addFlashAttribute("message", "User status updated.");
        return "redirect:/dashboard";
    }

    @PostMapping("/jobs/{id}/toggle")
    public String toggleJob(@PathVariable Long id, RedirectAttributes ra) {
        Job job = jobService.getById(id);
        job.setActive(!job.isActive());
        jobService.updateJob(job);
        ra.addFlashAttribute("message", "Job status updated.");
        return "redirect:/dashboard";
    }
}
