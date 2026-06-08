package com.helpdesk.repository;

import com.helpdesk.entity.Ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    

    List<Ticket> findByEmail(String email);
    
    
    

}