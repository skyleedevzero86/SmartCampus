package com.sleekydz86.chat.model.application.dto;

public record ChatMessageRequest(
        Long senderId,
        String message) {
}