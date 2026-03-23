package com.example.webapp.mapper;

import com.example.webapp.dto.user.CreateUserRequestDto;
import com.example.webapp.dto.user.UpdateUserRequestDto;
import com.example.webapp.dto.user.UserResponseDto;
import com.example.webapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequestDto dto) {
        User u = new User();
        u.setName(dto.name());
        u.setEmail(dto.email());
        u.setUsername(dto.username());
        u.setRole("ROLE_USER");
        return u;
    }

    public void updateEntity(User user, UpdateUserRequestDto dto) {
        user.setName(dto.name());
        user.setEmail(dto.email());
    }

    public UserResponseDto toDto(User user) {
        if (user == null) return null;
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}