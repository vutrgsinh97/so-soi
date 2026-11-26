package com.example.sosoi.dto.request;

import java.time.LocalDate;

public record UpdateTicketRequest(
    String shortDescription,
    String eat,
    String drink,
    Short feeling,
    LocalDate day,
    Integer version) {
}