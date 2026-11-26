package com.example.sosoi.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
    UUID id,
    String shortDescription,
    String eat,
    String drink,
    Short feeling,
    LocalDate day,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID profileId,
    Integer version) {
}
