package com.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.helpdesk.entity.AISession;
import com.helpdesk.repository.AISessionRepository;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AIService {

@Autowired
    private AISessionRepository aiSessionRepository;

    @Value("${gemini.api.key}")
    private String geminiApiKey;
   

    public String generateResponse(String issue) {

        RestTemplate restTemplate = new RestTemplate();

        String url ="https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key="
+ geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
       
        Map<String, Object> body = new HashMap<>();

        Map<String, Object> part = new HashMap<>();
        part.put("text",
                "You are an IT Helpdesk Assistant. Give troubleshooting steps for: "
                        + issue);

        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(part);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", parts);

        body.put("contents", List.of(content));

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        String aiResponse;

        try {

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            Map.class);

            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.getBody().get("candidates");

            Map<String, Object> firstCandidate =
                    candidates.get(0);

            Map<String, Object> candidateContent =
                    (Map<String, Object>) firstCandidate.get("content");

            List<Map<String, Object>> partsResponse =
                    (List<Map<String, Object>>) candidateContent.get("parts");

            aiResponse =
                    (String) partsResponse.get(0).get("text");
        } catch (Exception e) {
            e.printStackTrace();
            return "AI IS TEMPORARILY UNAVAILABLE";
        }

        try {
            // Save AI Session
            AISession session = new AISession();

            session.setUserId(1L); // For simplicity, using a hardcoded user ID. In real app, get from auth context
            session.setTitle("AI Troubleshooting Session");
            session.setDescription("Troubleshooting for issue: " + issue);
            session.setAiResponse(aiResponse);
            session.setResolvedByAI(false);
            session.setCreatedDate(LocalDateTime.now());
            aiSessionRepository.save(session);

            return aiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}