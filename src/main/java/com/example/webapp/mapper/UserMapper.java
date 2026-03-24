package com.example.webapp.mapper;

import com.example.webapp.dto.user.CreateUserRequestDTO;
import com.example.webapp.dto.user.UpdateUserRequestDTO;
import com.example.webapp.dto.user.UserResponseDTO;
import com.example.webapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequestDTO dto) {
        if (dto == null) return null;
        User u = new User();
        u.setName(dto.name());
        u.setEmail(dto.email());
        u.setUsername(dto.username());
        u.setRole("ROLE_USER");
        return u;
    }

    public void updateEntity(User user, UpdateUserRequestDTO dto) {
        user.setName(dto.name());
        user.setEmail(dto.email());
    }

    public UserResponseDTO toDto(User user) {
        if (user == null) return null;
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}