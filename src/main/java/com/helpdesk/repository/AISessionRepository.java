package com.helpdesk.repository;

import com.helpdesk.entity.AISession;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AISessionRepository extends JpaRepository<AISession, Long> {

    List<AISession> findByEmail(String email);
}
