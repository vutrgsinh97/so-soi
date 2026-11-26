package com.example.sosoi.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
    UUID id,
    String shortDescription,
    String eat,
    String drink,
    Short feeling,
    LocatedDate day,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    UUID profileId

) {
}
