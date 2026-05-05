package com.jobportal.repository;

import com.jobportal.entity.ApplicationStatus;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobApplication;
import com.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStudent(User student);
    List<JobApplication> findByJob(Job job);
    List<JobApplication> findByStudentOrderByAppliedAtDesc(User student);
    List<JobApplication> findByJobEmployerIdOrderByAppliedAtDesc(Long employerId);
    long countByStudentId(Long studentId);
    long countByStudentIdAndStatus(Long studentId, ApplicationStatus status);
    long countByJobEmployerIdAndStatus(Long employerId, ApplicationStatus status);
    Optional<JobApplication> findByJobAndStudent(Job job, User student);
}
