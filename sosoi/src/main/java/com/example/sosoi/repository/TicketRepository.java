package com.example.sosoi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sosoi.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
  
}
