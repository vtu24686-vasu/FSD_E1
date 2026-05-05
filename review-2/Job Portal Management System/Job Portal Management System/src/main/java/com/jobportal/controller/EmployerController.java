package com.jobportal.controller;

import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerController {
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    @GetMapping("/dashboard")
    public String dashboard(org.springframework.security.core.Authentication auth, Model model) {
        User employer = userService.findByEmail(auth.getName()).orElseThrow();
        var jobs = jobService.jobsByEmployer(employer);
        long totalApplicants = jobs.stream().mapToLong(j -> applicationService.jobApplicants(j).size()).sum();

        model.addAttribute("employer", employer);
        model.addAttribute("totalJobs", jobs.size());
        model.addAttribute("totalApplicants", totalApplicants);
        model.addAttribute("shortlistedCount", applicationService.countByEmployerAndStatus(employer.getId(), ApplicationStatus.SHORTLISTED));
        model.addAttribute("jobs", jobs.stream().limit(6).toList());
        return "employer/dashboard";
    }

    @GetMapping("/jobs")
    public String manageJobs(org.springframework.security.core.Authentication auth, Model model) {
        User employer = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("jobs", jobService.jobsByEmployer(employer));
        return "employer/jobs";
    }

    @PostMapping("/jobs")
    public String postJob(org.springframework.security.core.Authentication auth,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam String skillsRequired,
                          @RequestParam String salary,
                          @RequestParam String location,
                          @RequestParam Integer minExperience) {
        User employer = userService.findByEmail(auth.getName()).orElseThrow();
        Job job = new Job();
        job.setTitle(title);
        job.setDescription(description);
        job.setSkillsRequired(skillsRequired);
        job.setSalary(salary);
        job.setLocation(location);
        job.setMinExperience(minExperience);
        job.setEmployer(employer);
        jobService.save(job);
        return "redirect:/employer/jobs";
    }

    @PostMapping("/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.delete(id);
        return "redirect:/employer/jobs";
    }

    @GetMapping("/applicants/{jobId}")
    public String applicants(@PathVariable Long jobId, Model model) {
        Job job = jobService.getById(jobId);
        model.addAttribute("job", job);
        model.addAttribute("applications", applicationService.jobApplicants(job));
        return "employer/applicants";
    }

    @GetMapping("/notifications")
    public String notifications(org.springframework.security.core.Authentication auth, Model model) {
        User employer = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("applications", applicationService.employerNotifications(employer.getId()));
        return "employer/notifications";
    }

    @PostMapping("/application/{applicationId}/status")
    public String updateStatus(@PathVariable Long applicationId,
                               @RequestParam ApplicationStatus status,
                               @RequestParam Long jobId) {
        applicationService.updateStatus(applicationId, status);
        return "redirect:/employer/applicants/" + jobId;
    }
}
