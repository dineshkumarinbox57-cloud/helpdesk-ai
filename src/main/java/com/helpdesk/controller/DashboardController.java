package com.helpdesk.controller;

import com.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.helpdesk.entity.Ticket;
import com.helpdesk.service.TicketService;
import com.helpdesk.repository.AISessionRepository;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private AISessionRepository aiSessionRepository;
        @GetMapping("/dashboard")
        public Map<String, Long> getStats() {
            Map<String, Long> stats = new HashMap<>();
        
            List<Ticket> tickets = ticketService.getAllTickets();
        
            stats.put("totalTickets", (long) tickets.size());
            stats.put("openTickets",
                tickets.stream()
                       .filter(t -> "OPEN".equals(t.getStatus()))
                       .count());
        
            stats.put("resolvedTickets",
                tickets.stream()
                       .filter(t -> "RESOLVED".equals(t.getStatus()))
                       .count());

            stats.put("aiSessions", aiSessionRepository.count());
        
            return stats;
        }
}
