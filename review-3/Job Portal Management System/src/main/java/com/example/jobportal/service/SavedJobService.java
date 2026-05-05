package com.example.jobportal.service;

import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.SavedJob;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;

    public void toggle(User user, Job job) {
        savedJobRepository.findByUserAndJob(user, job)
            .ifPresentOrElse(savedJobRepository::delete,
                () -> savedJobRepository.save(SavedJob.builder().user(user).job(job).build()));
    }

    public List<SavedJob> savedBy(User user) {
        return savedJobRepository.findByUserOrderBySavedAtDesc(user);
    }

    public boolean isSaved(User user, Job job) {
        return savedJobRepository.existsByUserAndJob(user, job);
    }

    public Set<Long> savedJobIds(User user) {
        return savedBy(user).stream().map(saved -> saved.getJob().getId()).collect(Collectors.toSet());
    }
}
