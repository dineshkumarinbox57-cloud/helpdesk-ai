package com.helpdesk.controller;

import com.helpdesk.dto.AIRequest;
import com.helpdesk.service.AIService;
import com.helpdesk.entity.AISession;
import com.helpdesk.entity.Ticket;
import com.helpdesk.repository.AISessionRepository;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @Autowired
    private AISessionRepository aiSessionRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping("/suggest")
    public String getSuggestion(@RequestParam String issue) {
        return aiService.generateResponse(issue);
    }

    @PostMapping("/troubleshoot")
        public java.util.Map<String, Object> troubleshoot(
                @RequestBody AIRequest request) {

            String aiResponse =
                    aiService.generateResponse(request.getDescription());

                    System.out.println("Email = " + request.getEmail());
                    System.out.println("Description = " + request.getDescription());

            AISession session = new AISession();

            String issue = request.getDescription();

            if(issue.toLowerCase().contains("vpn"))
                session.setTitle("VPN Issue");
            else if(issue.toLowerCase().contains("login"))
                session.setTitle("Login Issue");
            else if(issue.toLowerCase().contains("wifi"))
                session.setTitle("WiFi Issue");
            else if(issue.length() > 20)
                session.setTitle(issue.substring(0, 20));
            else
                session.setTitle(issue);
            
            session.setAiResponse(aiResponse);
            session.setStatus("OPEN");
            session.setEmail(request.getEmail());
            session = aiSessionRepository.save(session);

            java.util.Map<String, Object> result =
                    new java.util.HashMap<>();

            result.put("sessionId", session.getId());
            result.put("aiResponse", aiResponse);

            return result;
        }
    @PostMapping("/feedback")
    public String feedback(
            @RequestParam @NonNull Long sessionId,
            @RequestParam boolean resolved) {

        AISession session =
                aiSessionRepository.findById(sessionId).orElseThrow();

        if(resolved) {
            session.setStatus("RESOLVED");
            aiSessionRepository.save(session);
            return "Issue resolved successfully";
        }
      
        session.setStatus("OPEN");
        aiSessionRepository.save(session);

        Ticket ticket = new Ticket();
        ticket.setTitle("AI Escalated Ticket");
        ticket.setDescription(session.getDescription());
        ticket.setStatus("OPEN");
        ticket.setCategory("AI support");
        ticket.setPriority("HIGH"); 
        ticket.setEmail(session.getEmail());
        ticketRepository.save(ticket);

        return "Ticket escalation required";
    }

        @GetMapping("/sessions/{email}")
        public List<AISession> getSessionsByEmail(
                @PathVariable String email) {

            return aiSessionRepository.findByEmail(email);
        }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Long id) {

        if (!aiSessionRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body("Session not found");
        }

        aiSessionRepository.deleteById(id);

        return ResponseEntity.ok("AI Session deleted");
    }
} 