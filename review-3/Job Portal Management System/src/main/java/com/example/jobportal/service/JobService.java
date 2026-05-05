package com.example.jobportal.service;

import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Job getById(Long id) {
        return jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> searchJobs(String keyword) {
        return searchJobs(keyword, null, null, null);
    }

    public List<Job> searchJobs(String keyword, String location, String category, String experience) {
        return jobRepository.searchActive(keyword, location, category, experience, null, null, null, null);
    }

    public Page<Job> searchJobs(String keyword, String location, String category, String experience,
                                String company, String jobType, Integer minSalary, Integer maxSalary,
                                String sort, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size, sortFor(sort));
        return jobRepository.searchActivePage(keyword, location, category, experience, company, jobType, minSalary, maxSalary, pageable);
    }

    private Sort sortFor(String sort) {
        if ("salary".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.DESC, "maxSalary").and(Sort.by(Sort.Direction.DESC, "minSalary"));
        }
        if ("relevance".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.ASC, "title");
        }
        return Sort.by(Sort.Direction.DESC, "createdAt");
    }

    public List<Job> getJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public long countAll() {
        return jobRepository.count();
    }

    public List<Job> recommendJobs(User user) {
        if (user == null || user.getSkills() == null || user.getSkills().isBlank()) {
            return searchJobs(null).stream().limit(3).toList();
        }
        String[] skills = user.getSkills().split(",");
        return searchJobs(null).stream()
            .filter(job -> {
                String haystack = (job.getTitle() + " " + job.getDescription() + " " + job.getCategory()).toLowerCase();
                for (String skill : skills) {
                    if (!skill.isBlank() && haystack.contains(skill.trim().toLowerCase())) {
                        return true;
                    }
                }
                return false;
            })
            .limit(3)
            .toList();
    }

    public List<String> suggestTitles(String term) {
        return jobRepository.suggestTitles(term == null ? "" : term, PageRequest.of(0, 8));
    }

    public void deactivateExpiredJobs() {
        jobRepository.findAll().stream()
            .filter(job -> job.isActive() && job.getDeadline() != null && job.getDeadline().isBefore(java.time.LocalDate.now()))
            .forEach(job -> {
                job.setActive(false);
                jobRepository.save(job);
            });
    }
}
