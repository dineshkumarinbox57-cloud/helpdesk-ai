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

    @Value("${openai.api.key}")
    private String openaiApiKey;
   

    public String generateResponse(String issue) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put(
                "content",
                "You are an IT Helpdesk Assistant. Give troubleshooting steps for: "
                        + issue);

        messages.add(message);

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            Map.class);

            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) response.getBody().get("choices");

            Map<String, Object> firstChoice = choices.get(0);

            Map<String, String> responseMessage =
                    (Map<String, String>) firstChoice.get("message");

            String aiResponse = responseMessage.get("content");

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