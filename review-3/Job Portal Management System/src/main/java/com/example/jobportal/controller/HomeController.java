package com.example.jobportal.controller;

import com.example.jobportal.entity.*;
import com.example.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Comparator;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final SavedJobService savedJobService;
    private final NotificationService notificationService;

    @GetMapping("/")
    public String index(Model model) {
        List<Job> featuredJobs = jobService.searchJobs(null);
        model.addAttribute("featuredJobs", featuredJobs.stream().limit(6).toList());
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);

        boolean isEmployer = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isEmployer) {
            List<Job> myJobs = jobService.getJobsByEmployer(user);
            model.addAttribute("myJobs", myJobs);
            long totalApplications = myJobs.stream()
                .mapToLong(j -> j.getApplications() != null ? j.getApplications().size() : 0)
                .sum();
            model.addAttribute("totalApplications", totalApplications);
            model.addAttribute("shortlistedCandidates", applicationService.countShortlistedByJobs(myJobs));
            model.addAttribute("activeJobs", myJobs.stream().filter(Job::isActive).count());
            model.addAttribute("closedJobs", myJobs.stream().filter(job -> !job.isActive()).count());

            List<JobApplication> employerApplications = myJobs.stream()
                .flatMap(job -> applicationService.getApplicationsByJob(job).stream())
                .toList();
            long interviewsScheduled = employerApplications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.INTERVIEW_SCHEDULED || app.getInterviewAt() != null)
                .count();
            long selectedCandidates = employerApplications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.SELECTED)
                .count();
            long rejectedCandidates = employerApplications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.REJECTED)
                .count();
            long inReviewCandidates = employerApplications.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.UNDER_REVIEW)
                .count();
            Job topJob = myJobs.stream()
                .max(Comparator.comparingInt(job -> applicationService.getApplicationsByJob(job).size()))
                .orElse(null);

            model.addAttribute("interviewsScheduled", interviewsScheduled);
            model.addAttribute("selectedCandidates", selectedCandidates);
            model.addAttribute("rejectedCandidates", rejectedCandidates);
            model.addAttribute("inReviewCandidates", inReviewCandidates);
            model.addAttribute("averageApplications", myJobs.isEmpty() ? 0 : Math.round((double) totalApplications / myJobs.size()));
            model.addAttribute("topJobTitle", topJob != null ? topJob.getTitle() : "No jobs yet");
            model.addAttribute("topJobApplications", topJob != null ? applicationService.getApplicationsByJob(topJob).size() : 0);
            return "employer/dashboard";
        } else if (isAdmin) {
            model.addAttribute("totalUsers", userService.countAll());
            model.addAttribute("totalStudents", userService.countByRole(Role.STUDENT));
            model.addAttribute("totalEmployers", userService.countByRole(Role.EMPLOYER));
            model.addAttribute("totalJobs", jobService.countAll());
            model.addAttribute("totalApplications", applicationService.countAll());
            model.addAttribute("users", userService.findAll());
            model.addAttribute("jobs", jobService.getAllJobs());
            return "admin/dashboard";
        } else {
            List<JobApplication> myApplications = applicationService.getApplicationsByApplicant(user);
            model.addAttribute("myApplications", myApplications);
            model.addAttribute("positiveUpdates", myApplications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.SHORTLISTED || a.getStatus() == ApplicationStatus.SELECTED)
                .count());
            model.addAttribute("inProgressApplications", myApplications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.APPLIED || a.getStatus() == ApplicationStatus.UNDER_REVIEW)
                .count());
            model.addAttribute("rejectedApplications", myApplications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.REJECTED)
                .count());
            model.addAttribute("recommendedJobs", jobService.recommendJobs(user));
            model.addAttribute("savedJobs", savedJobService.savedBy(user));
            model.addAttribute("notifications", notificationService.latestFor(user));
            model.addAttribute("unreadNotifications", notificationService.unreadCount(user));
            return "student/dashboard";
        }
    }
}
