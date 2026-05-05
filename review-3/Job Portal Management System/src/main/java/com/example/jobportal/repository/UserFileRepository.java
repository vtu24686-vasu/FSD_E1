package com.example.jobportal.repository;

import com.example.jobportal.entity.User;
import com.example.jobportal.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    List<UserFile> findByUserOrderByUploadedAtDesc(User user);
}
