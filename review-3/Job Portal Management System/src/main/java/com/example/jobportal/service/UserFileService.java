package com.example.jobportal.service;

import com.example.jobportal.entity.User;
import com.example.jobportal.entity.UserFile;
import com.example.jobportal.repository.UserFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFileService {

    private final ResumeStorageService storageService;
    private final UserFileRepository userFileRepository;

    public void storeFiles(List<MultipartFile> files, User user, String type) throws IOException {
        if (files == null) {
            return;
        }
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                ResumeStorageService.StoredResume stored = storageService.store(file, user);
                userFileRepository.save(UserFile.builder()
                    .user(user)
                    .fileName(stored.originalName())
                    .fileUrl(stored.publicPath())
                    .fileType(type)
                    .build());
            }
        }
    }

    public List<UserFile> filesFor(User user) {
        return userFileRepository.findByUserOrderByUploadedAtDesc(user);
    }
}
