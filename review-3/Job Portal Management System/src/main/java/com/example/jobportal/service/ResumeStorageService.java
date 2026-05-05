package com.example.jobportal.service;

import com.example.jobportal.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@Service
public class ResumeStorageService {

    private final Path uploadDirectory;

    public ResumeStorageService(@Value("${app.upload.dir:uploads/resumes}") String uploadDir) throws IOException {
        this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.uploadDirectory);
    }

    public StoredResume store(MultipartFile file, User user) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please choose a resume file.");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "resume" : file.getOriginalFilename());
        String lowerName = originalName.toLowerCase(Locale.ROOT);
        if (!(lowerName.endsWith(".pdf") || lowerName.endsWith(".doc") || lowerName.endsWith(".docx"))) {
            throw new IllegalArgumentException("Resume must be a PDF, DOC, or DOCX file.");
        }

        String extension = lowerName.substring(lowerName.lastIndexOf('.'));
        String safeName = "user-" + user.getId() + "-" + UUID.randomUUID() + extension;
        Path target = uploadDirectory.resolve(safeName).normalize();
        if (!target.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid file path.");
        }

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return new StoredResume(originalName, "/uploads/resumes/" + safeName);
    }

    public record StoredResume(String originalName, String publicPath) {
    }
}
