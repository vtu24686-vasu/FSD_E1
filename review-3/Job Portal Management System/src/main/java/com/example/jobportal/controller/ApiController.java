package com.example.jobportal.controller;

import com.example.jobportal.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ChatbotService chatbotService;

    @PostMapping("/api/chatbot")
    public Map<String, String> chatbot(@RequestParam String question) {
        return Map.of("reply", chatbotService.reply(question));
    }
}
