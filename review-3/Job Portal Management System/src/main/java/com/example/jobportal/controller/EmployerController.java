package com.example.jobportal.controller;

import com.example.jobportal.entity.*;
import com.example.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerController {

    private final JobService jobService;
    private final UserService userService;
    private final ApplicationService applicationService;

    /* ---- Job CRUD ---- */

    @GetMapping("/jobs/new")
    public String newJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "employer/job-form";
    }

    @PostMapping("/jobs/new")
    public String createJob(@ModelAttribute Job job, Authentication auth,
                            RedirectAttributes ra) {
        User employer = userService.findByEmail(auth.getName());
        job.setEmployer(employer);
        jobService.createJob(job);
        ra.addFlashAttribute("message", "Job posted successfully!");
        return "redirect:/dashboard";
    }

    @GetMapping("/jobs/{id}/edit")
    public String editJobForm(@PathVariable Long id, Authentication auth, Model model) {
        Job job = jobService.getById(id);
        User employer = userService.findByEmail(auth.getName());
        if (!job.getEmployer().getId().equals(employer.getId())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("job", job);
        return "employer/job-form";
    }

    @PostMapping("/jobs/{id}/edit")
    public String updateJob(@PathVariable Long id, @ModelAttribute Job updatedJob,
                            Authentication auth, RedirectAttributes ra) {
        Job existing = jobService.getById(id);
        User employer = userService.findByEmail(auth.getName());
        if (!existing.getEmployer().getId().equals(employer.getId())) {
            return "redirect:/dashboard";
        }
        existing.setTitle(updatedJob.getTitle());
        existing.setDescription(updatedJob.getDescription());
        existing.setLocation(updatedJob.getLocation());
        existing.setCategory(updatedJob.getCategory());
        existing.setExperienceLevel(updatedJob.getExperienceLevel());
        existing.setSalary(updatedJob.getSalary());
        existing.setMinSalary(updatedJob.getMinSalary());
        existing.setMaxSalary(updatedJob.getMaxSalary());
        existing.setJobType(updatedJob.getJobType());
        existing.setDeadline(updatedJob.getDeadline());
        existing.setActive(updatedJob.isActive());
        jobService.updateJob(existing);
        ra.addFlashAttribute("message", "Job updated successfully!");
        return "redirect:/dashboard";
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        Job job = jobService.getById(id);
        User employer = userService.findByEmail(auth.getName());
        if (job.getEmployer().getId().equals(employer.getId())) {
            jobService.deleteJob(id);
            ra.addFlashAttribute("message", "Job deleted.");
        }
        return "redirect:/dashboard";
    }

    /* ---- Applicant Management ---- */

    @GetMapping("/jobs/{id}/applicants")
    public String viewApplicants(@PathVariable Long id, Authentication auth, Model model) {
        Job job = jobService.getById(id);
        User employer = userService.findByEmail(auth.getName());
        if (!job.getEmployer().getId().equals(employer.getId())) {
            return "redirect:/dashboard";
        }
        List<JobApplication> applications = applicationService.getApplicationsByJob(job);
        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        return "employer/applicants";
    }

    @PostMapping("/applications/{appId}/status")
    public String updateStatus(@PathVariable Long appId,
                               @RequestParam ApplicationStatus status,
                               @RequestParam Long jobId,
                               RedirectAttributes ra) {
        applicationService.updateStatus(appId, status);
        ra.addFlashAttribute("message", "Application status updated to " + status + ".");
        return "redirect:/employer/jobs/" + jobId + "/applicants";
    }

    @PostMapping("/applications/{appId}/interview")
    public String scheduleInterview(@PathVariable Long appId,
                                    @RequestParam Long jobId,
                                    @RequestParam String interviewAt,
                                    @RequestParam(required = false) String interviewMode,
                                    @RequestParam(required = false) String interviewNotes,
                                    RedirectAttributes ra) {
        applicationService.scheduleInterview(appId, LocalDateTime.parse(interviewAt), interviewMode, interviewNotes);
        ra.addFlashAttribute("message", "Interview scheduled and candidate notified.");
        return "redirect:/employer/jobs/" + jobId + "/applicants";
    }

    @GetMapping("/company")
    public String companyProfile(Authentication auth, Model model) {
        model.addAttribute("user", userService.findByEmail(auth.getName()));
        return "employer/company-profile";
    }

    @PostMapping("/company")
    public String updateCompanyProfile(Authentication auth,
                                       @RequestParam(required = false) String companyName,
                                       @RequestParam(required = false) String companyDescription,
                                       @RequestParam(required = false) String companyLocation,
                                       @RequestParam(required = false) String companyWebsite,
                                       RedirectAttributes ra) {
        User employer = userService.findByEmail(auth.getName());
        employer.setCompanyName(companyName);
        employer.setCompanyDescription(companyDescription);
        employer.setCompanyLocation(companyLocation);
        employer.setCompanyWebsite(companyWebsite);
        userService.updateProfile(employer);
        ra.addFlashAttribute("message", "Company profile updated.");
        return "redirect:/employer/company";
    }
}
