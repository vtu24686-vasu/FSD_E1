package com.example.jobportal.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeParserService {

    private static final List<String> KNOWN_SKILLS = List.of(
        "Java", "Spring Boot", "SQL", "MySQL", "React", "Node.js", "AWS",
        "Docker", "Kubernetes", "Python", "Machine Learning", "Figma", "HTML", "CSS"
    );

    public String parseSkills(String existingSkills, String fileName) {
        String source = ((existingSkills == null ? "" : existingSkills) + " " + (fileName == null ? "" : fileName)).toLowerCase();
        List<String> matches = KNOWN_SKILLS.stream()
            .filter(skill -> source.contains(skill.toLowerCase()))
            .toList();
        if (!matches.isEmpty()) {
            return String.join(", ", matches);
        }
        return "Resume uploaded. Configure Affinda/RChilli API keys to extract full skills, education, and experience.";
    }
}
