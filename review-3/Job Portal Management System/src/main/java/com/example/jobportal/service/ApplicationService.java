package com.example.jobportal.service;

import com.example.jobportal.entity.*;
import com.example.jobportal.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final NotificationService notificationService;

    public JobApplication apply(Job job, User applicant) {
        return apply(job, applicant, null, null, null);
    }

    public JobApplication apply(Job job, User applicant, String coverLetter, Integer expectedSalary, LocalDate availableFrom) {
        // Check if already applied
        applicationRepository.findByJobAndApplicant(job, applicant)
            .ifPresent(a -> { throw new IllegalStateException("Already applied to this job."); });

        JobApplication application = JobApplication.builder()
            .job(job)
            .applicant(applicant)
            .status(ApplicationStatus.APPLIED)
            .resumePath(applicant.getResumePath())
            .coverLetter(coverLetter)
            .expectedSalary(expectedSalary)
            .availableFrom(availableFrom)
            .build();
        JobApplication saved = applicationRepository.save(application);
        notificationService.sendApplicationReceivedEmail(applicant.getEmail(), job.getTitle());
        notificationService.notifyUser(applicant, "Application submitted",
            "Your application for " + job.getTitle() + " was submitted.", "/jobs/" + job.getId());
        return saved;
    }

    public List<JobApplication> getApplicationsByApplicant(User applicant) {
        return applicationRepository.findByApplicant(applicant);
    }

    public List<JobApplication> getApplicationsByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    public JobApplication updateStatus(Long applicationId, ApplicationStatus status) {
        JobApplication app = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationId));
        app.setStatus(status);
        JobApplication saved = applicationRepository.save(app);
        notificationService.sendStatusUpdateEmail(
            app.getApplicant().getEmail(),
            app.getJob().getTitle(),
            status.name()
        );
        if (app.getApplicant().getPhone() != null && !app.getApplicant().getPhone().isBlank()) {
            notificationService.sendSms(app.getApplicant().getPhone(),
                "Your application for " + app.getJob().getTitle() + " is now " + status.name() + ".");
        }
        notificationService.notifyUser(app.getApplicant(), "Application status updated",
            "Your application for " + app.getJob().getTitle() + " is now " + status.name() + ".",
            "/dashboard");
        return saved;
    }

    public JobApplication scheduleInterview(Long applicationId, LocalDateTime interviewAt, String mode, String notes) {
        JobApplication app = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationId));
        app.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        app.setInterviewAt(interviewAt);
        app.setInterviewMode(mode);
        app.setInterviewNotes(notes);
        JobApplication saved = applicationRepository.save(app);
        notificationService.sendStatusUpdateEmail(app.getApplicant().getEmail(), app.getJob().getTitle(), "INTERVIEW_SCHEDULED");
        notificationService.notifyUser(app.getApplicant(), "Interview scheduled",
            "Interview for " + app.getJob().getTitle() + " is scheduled on " + interviewAt + ".",
            "/dashboard");
        return saved;
    }

    public long countAll() {
        return applicationRepository.count();
    }

    public long countShortlistedByJobs(List<Job> jobs) {
        return jobs.stream()
            .flatMap(job -> getApplicationsByJob(job).stream())
            .filter(app -> app.getStatus() == ApplicationStatus.SHORTLISTED || app.getStatus() == ApplicationStatus.SELECTED)
            .count();
    }
}
