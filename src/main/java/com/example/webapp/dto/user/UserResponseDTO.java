package com.example.webapp.dto.user;

import java.io.Serial;
import java.io.Serializable;

public record UserResponseDTO(
        Long id,
        String name,
        String email
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}