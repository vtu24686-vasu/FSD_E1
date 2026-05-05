package com.jobportal.service;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public Job save(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> allJobs() {
        return jobRepository.findAll();
    }

    public List<Job> jobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    public List<Job> search(String query) {
        if (query == null || query.isBlank()) {
            return jobRepository.findAll();
        }
        return jobRepository.findByTitleContainingIgnoreCaseOrSkillsRequiredContainingIgnoreCaseOrLocationContainingIgnoreCase(query, query, query);
    }

    public Job getById(Long id) {
        return jobRepository.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        jobRepository.deleteById(id);
    }
}
