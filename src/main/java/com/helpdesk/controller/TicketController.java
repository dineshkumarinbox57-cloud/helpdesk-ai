package com.helpdesk.controller;

import com.helpdesk.entity.Ticket;
import com.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.helpdesk.repository.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

   
    @Autowired
    private TicketService ticketService;
    TicketController(TicketRepository ticketRepository) {
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @PutMapping("/{id}/status")
    public Ticket updateStatus(
        @PathVariable Long id,
        @RequestParam String status) {

        return ticketService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "Ticket deleted successfully";
    } 

    @GetMapping("/user/{email}")
    public List<Ticket> getUserTickets(@PathVariable String email) {
   
    return ticketService.getTicketsByEmail(email);
}
}
