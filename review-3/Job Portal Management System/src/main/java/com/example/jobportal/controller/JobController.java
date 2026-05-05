package com.example.jobportal.controller;

import com.example.jobportal.entity.*;
import com.example.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.time.LocalDate;
import java.util.Set;

@Controller
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;
    private final NotificationService notificationService;

    @GetMapping
    public String listJobs(@RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String location,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) String experience,
                           @RequestParam(required = false) String company,
                           @RequestParam(required = false) String jobType,
                           @RequestParam(required = false) Integer minSalary,
                           @RequestParam(required = false) Integer maxSalary,
                           @RequestParam(defaultValue = "latest") String sort,
                           @RequestParam(defaultValue = "0") int page,
                           Authentication auth,
                           Model model) {
        jobService.deactivateExpiredJobs();
        Page<Job> jobPage = jobService.searchJobs(keyword, location, category, experience, company, jobType, minSalary, maxSalary, sort, page, 6);
        List<Job> jobs = jobPage.getContent();
        model.addAttribute("jobs", jobs);
        model.addAttribute("jobPage", jobPage);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("location", location != null ? location : "");
        model.addAttribute("category", category != null ? category : "");
        model.addAttribute("experience", experience != null ? experience : "");
        model.addAttribute("company", company != null ? company : "");
        model.addAttribute("jobType", jobType != null ? jobType : "");
        model.addAttribute("minSalary", minSalary);
        model.addAttribute("maxSalary", maxSalary);
        model.addAttribute("sort", sort);
        if (auth != null) {
            User user = userService.findByEmail(auth.getName());
            Set<Long> savedIds = savedJobService.savedJobIds(user);
            model.addAttribute("savedJobIds", savedIds);
        }
        return "jobs/list";
    }

    @GetMapping("/{id}")
    public String jobDetail(@PathVariable Long id, Authentication auth, Model model) {
        Job job = jobService.getById(id);
        model.addAttribute("job", job);

        boolean alreadyApplied = false;
        if (auth != null) {
            User user = userService.findByEmail(auth.getName());
            alreadyApplied = applicationService.getApplicationsByApplicant(user)
                .stream().anyMatch(a -> a.getJob().getId().equals(id));
            model.addAttribute("currentUser", user);
            model.addAttribute("saved", savedJobService.isSaved(user, job));
        }
        model.addAttribute("alreadyApplied", alreadyApplied);
        return "jobs/detail";
    }

    @PostMapping("/{id}/apply")
    public String applyForJob(@PathVariable Long id, Authentication auth,
                              @RequestParam(required = false) String coverLetter,
                              @RequestParam(required = false) Integer expectedSalary,
                              @RequestParam(required = false) LocalDate availableFrom,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(auth.getName());
            Job job = jobService.getById(id);
            applicationService.apply(job, user, coverLetter, expectedSalary, availableFrom);
            redirectAttributes.addFlashAttribute("message", "Application submitted successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/jobs/" + id;
    }

    @PostMapping("/{id}/save")
    public String saveJob(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        User user = userService.findByEmail(auth.getName());
        Job job = jobService.getById(id);
        savedJobService.toggle(user, job);
        notificationService.notifyUser(user, "Saved jobs updated", "Your saved jobs list was updated.", "/dashboard");
        ra.addFlashAttribute("message", "Saved jobs updated.");
        return "redirect:/jobs/" + id;
    }

    @GetMapping("/suggest")
    @ResponseBody
    public List<String> suggest(@RequestParam(required = false) String term) {
        return jobService.suggestTitles(term);
    }

    @GetMapping("/companies/{id}")
    public String company(@PathVariable Long id, Model model) {
        User employer = userService.findById(id);
        model.addAttribute("employer", employer);
        model.addAttribute("openJobs", jobService.getJobsByEmployer(employer).stream().filter(Job::isActive).toList());
        return "jobs/company";
    }
}
