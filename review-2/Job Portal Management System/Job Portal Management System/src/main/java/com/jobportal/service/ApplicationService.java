package com.jobportal.service;

import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobApplication;
import com.jobportal.entity.User;
import com.jobportal.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final JobApplicationRepository applicationRepository;
    private final MailService mailService;

    public JobApplication apply(Job job,
                                User student,
                                String phone,
                                String city,
                                String highestQualification,
                                String specialization,
                                Integer passingYear,
                                Double cgpa,
                                String coverLetter,
                                String resumePath) {
        JobApplication application = applicationRepository.findByJobAndStudent(job, student).orElseGet(JobApplication::new);
        application.setJob(job);
        application.setStudent(student);
        application.setStatus(application.getId() == null ? ApplicationStatus.APPLIED : application.getStatus());
        application.setPhone(phone);
        application.setCity(city);
        application.setHighestQualification(highestQualification);
        application.setSpecialization(specialization);
        application.setPassingYear(passingYear);
        application.setCgpa(cgpa);
        application.setCoverLetter(coverLetter);
        if (resumePath != null) {
            application.setApplicationResumePath(resumePath);
        }

        JobApplication saved = applicationRepository.save(application);

        mailService.sendMail(student.getEmail(), "Application Submitted", "Your application for " + job.getTitle() + " has been submitted.");
        mailService.sendMail(job.getEmployer().getEmail(), "New application received", "A new student has applied for " + job.getTitle() + ".");
        return saved;
    }

    public List<JobApplication> studentApplications(User student) {
        return applicationRepository.findByStudent(student);
    }

    public List<JobApplication> studentNotifications(User student) {
        return applicationRepository.findByStudentOrderByAppliedAtDesc(student);
    }

    public List<JobApplication> employerNotifications(Long employerId) {
        return applicationRepository.findByJobEmployerIdOrderByAppliedAtDesc(employerId);
    }

    public List<JobApplication> jobApplicants(Job job) {
        return applicationRepository.findByJob(job);
    }

    public JobApplication updateStatus(Long applicationId, ApplicationStatus status) {
        JobApplication application = applicationRepository.findById(applicationId).orElseThrow();
        application.setStatus(status);
        JobApplication updated = applicationRepository.save(application);

        String message = switch (status) {
            case SHORTLISTED -> "You are shortlisted for " + application.getJob().getTitle();
            case REJECTED -> "Application rejected for " + application.getJob().getTitle();
            default -> "Application updated for " + application.getJob().getTitle();
        };
        mailService.sendMail(application.getStudent().getEmail(), "Application Status Update", message);
        return updated;
    }

    public long countByStudent(Long studentId) {
        return applicationRepository.countByStudentId(studentId);
    }

    public long countByStudentAndStatus(Long studentId, ApplicationStatus status) {
        return applicationRepository.countByStudentIdAndStatus(studentId, status);
    }

    public long countByEmployerAndStatus(Long employerId, ApplicationStatus status) {
        return applicationRepository.countByJobEmployerIdAndStatus(employerId, status);
    }
}
