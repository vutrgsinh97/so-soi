package com.example.sosoi.dto.request;

import java.time.LocalDate;

public record CreateTicketRequest(
    String shortDescription,
    String eat,
    String drink,
    Short feeling,
    LocalDate day) {
}