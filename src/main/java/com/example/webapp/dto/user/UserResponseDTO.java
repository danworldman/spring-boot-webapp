package com.example.webapp.dto.user;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        long followersCount, // количество тех, кто подписан на юзера
        long followingCount  // количество тех, на кого подписан юзер
){}