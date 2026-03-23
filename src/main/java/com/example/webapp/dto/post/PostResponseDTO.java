package com.example.webapp.dto.post;

public record PostResponseDTO(
    Long id,
    Long userId,
    String content
) {}