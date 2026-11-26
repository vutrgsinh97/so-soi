package com.example.sosoi.dto.request;

public record CreateTicketRequest(
    String title,
    String description,
    String eat,
    String drink,
    Short feeling) {
}