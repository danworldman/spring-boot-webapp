package com.example.webapp.service;

import com.example.webapp.dto.user.CreateUserRequestDto;
import com.example.webapp.dto.user.UpdateUserRequestDto;
import com.example.webapp.dto.user.UserResponseDto;
import com.example.webapp.entity.User;
import com.example.webapp.exception.BadRequestException;
import com.example.webapp.mapper.UserMapper;
import com.example.webapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder; // Добавляем мок для энкодера

    @Test
    void create_ShouldSaveUser_WhenEmailIsUnique() {
        // 1. Arrange (Подготовка данных)
        var dto = new CreateUserRequestDto("Ivan", "ivan@mail.com", "ivan_user", "pass123");
        var user = new User(); // имитируем сущность
        var response = new UserResponseDto(1L, "Ivan", "ivan@mail.com");

        when(repo.existsByEmail(dto.email())).thenReturn(false); // email свободен
        when(mapper.toEntity(dto)).thenReturn(user);
        when(repo.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(response);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_pass");

        // 2. Act (Действие)
        UserResponseDto result = userService.create(dto);

        // 3. Assert (Проверка)
        assertNotNull(result);
        assertEquals("ivan@mail.com", result.email());
        verify(repo, times(1)).save(any()); // проверяем, что метод save реально вызвался
    }

    @Test
    void create_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        var dto = new CreateUserRequestDto("Ivan", "ivan@mail.com", "ivan_user", "pass123");
        when(repo.existsByEmail(dto.email())).thenReturn(true); // email ЗАНЯТ

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.create(dto));
        verify(repo, never()).save(any()); // ГЛАВНОЕ: проверяем, что до сохранения дело не дошло!
    }

    @Test
    void update_ShouldUpdateUser_WhenUserExistsAndEmailIsUnique() {
        // 1. Arrange
        Long userId = 1L;
        var dto = new UpdateUserRequestDto("New Name", "new@mail.com");
        var existingUser = new User(); // Существующий в базе юзер
        existingUser.setId(userId);
        existingUser.setEmail("old@mail.com");

        var response = new UserResponseDto(userId, "New Name", "new@mail.com");

        // Имитируем: юзер найден, email свободен
        when(repo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(repo.existsByEmail(dto.email())).thenReturn(false);
        when(mapper.toDto(existingUser)).thenReturn(response);

        // 2. Act
        UserResponseDto result = userService.update(userId, dto);

        // 3. Assert
        assertNotNull(result);
        assertEquals("New Name", result.name());

        // Проверяем, что маппер реально вызывался для обновления полей
        verify(mapper).updateEntity(existingUser, dto);
        // Проверяем, что save НЕ вызывался (так как у нас Dirty Checking в транзакции)
        verify(repo, never()).save(any());
    }
}