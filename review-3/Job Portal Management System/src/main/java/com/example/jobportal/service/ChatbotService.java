package com.example.jobportal.service;

import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    public String reply(String question) {
        String q = question == null ? "" : question.toLowerCase();
        if (q.contains("apply")) {
            return "Open any job detail page and click Apply Now. Your dashboard will show the application status.";
        }
        if (q.contains("resume")) {
            return "Job seekers can upload PDF, DOC, or DOCX resumes from the profile section on the dashboard.";
        }
        if (q.contains("status") || q.contains("shortlist") || q.contains("interview")) {
            return "Application updates appear on the job seeker dashboard and are also sent through the mock notification service.";
        }
        if (q.contains("post") || q.contains("employer")) {
            return "Employers can register, open the dashboard, and use Post New Job to publish openings.";
        }
        return "I can help with job search, applying, resumes, employer posting, and application status tracking.";
    }
}
