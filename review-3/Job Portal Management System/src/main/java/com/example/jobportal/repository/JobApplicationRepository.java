package com.example.jobportal.repository;

import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByApplicant(User applicant);
    List<JobApplication> findByJob(Job job);
    Optional<JobApplication> findByJobAndApplicant(Job job, User applicant);
}
