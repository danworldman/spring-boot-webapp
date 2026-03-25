package com.example.webapp.service;

import com.example.webapp.dto.user.CreateUserRequestDTO;
import com.example.webapp.dto.user.UpdateUserRequestDTO;
import com.example.webapp.dto.user.UserResponseDTO;
import com.example.webapp.entity.User;
import com.example.webapp.event.UserRegisteredEvent;
import com.example.webapp.exception.BadRequestException;
import com.example.webapp.exception.ResourceNotFoundException;
import com.example.webapp.mapper.UserMapper;
import com.example.webapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // подключение Mockito к JUnit 5
class UserServiceTest {

    // @Mock создает пустышку зависимости, она ничего не умеет
    @Mock
    private UserRepository repo; // мок для репозитория

    @Mock
    private UserMapper mapper; // мок для маппера

    @Mock
    private PasswordEncoder passwordEncoder; // мок для хэширования пароля

    @Mock
    private AsyncService asyncService; // мок для асинхронного сервиса

    @Mock
    private ApplicationEventPublisher eventPublisher; // мок для события

    // @InjectMocks создает реальный UserService и вставляет в него созданные выше @Mock
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Успешное создание пользователя")
    void create_ShouldSaveUser_WhenEmailIsUnique() {
        // вводные тестовые данные
        var dto = new CreateUserRequestDTO("Ivan", "ivan@mail.com", "ivan_user", "pass123");
        var userEntity = new User();
        var response = new UserResponseDTO(1L, "Ivan", "ivan@mail.com",0L, 0L);

        // описание ситуации для данных
        when(repo.existsByEmail(dto.email())).thenReturn(false); // email свободен
        when(mapper.toEntity(dto)).thenReturn(userEntity); // маппер вернул сущность
        // пароль зашифрован и в БД летит hashed_pass
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_pass");
        when(repo.save(userEntity)).thenReturn(userEntity); // сохранение прошло успешно
        when(mapper.toDto(userEntity)).thenReturn(response); // маппер вернул итоговый DTO

        // вызов реального метода сервиса
        UserResponseDTO result = userService.create(dto);

        // проверка результата
        assertNotNull(result); // проверка что result не null
        assertEquals("ivan@mail.com", result.email()); // сравниваются значения email

        // проверка поведения, поскольку одного результата мало
        // проверка, что метод save вызвался ровно 1 раз
        verify(repo, times(1)).save(userEntity);

        // проверка, что UserService опубликовал событие через EventPublisher
        verify(eventPublisher, times(1)).publishEvent(any(UserRegisteredEvent.class));
    }

    @Test
    @DisplayName("Ошибка 404, если пользователь не найден по ID")
    void getById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        Long userId = 99L;

        // делаем мок репозитория так, чтобы он вернул пустоту
        when(repo.findById(userId)).thenReturn(Optional.empty());

        // проверка, что метод выбросит именно ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(userId));

        // проверка, что до маппинга дело не дойдёт
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Ошибка 400, если email уже занят")
    void create_ShouldThrowException_WhenEmailAlreadyExists() {
        var dto = new CreateUserRequestDTO("Ivan", "ivan@mail.com", "ivan_user", "pass123");
        when(repo.existsByEmail(dto.email())).thenReturn(true); // имитация, что email уже есть в БД

        assertThrows(BadRequestException.class, () -> userService.create(dto));

        // проверка, что метод save не вызывался
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("Обновление данных без вызова метода save")
    void update_ShouldUpdateUser_WhenUserExists() {
        Long userId = 1L;
        var dto = new UpdateUserRequestDTO("New Name", "new@mail.com");
        var existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@mail.com");

        // имитация, что в БД есть старый пользователь
        when(repo.findById(userId)).thenReturn(Optional.of(existingUser));
        when(repo.existsByEmail(dto.email())).thenReturn(false); // новый email свободен
        // если сущность мапится, то надо создать DTO для ответа
        when(mapper.toDto(existingUser)).thenReturn(new UserResponseDTO(userId, "New Name", "new@mail.com", 0L, 0L));

        UserResponseDTO result = userService.update(userId, dto);

        assertEquals("New Name", result.name());

        // проверка вызовом маппера для обновления полей сущности
        verify(mapper).updateEntity(existingUser, dto);
        // проверка, что не вызывался repo.save(), так как есть механизм транзакций Hibernate
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("Успешная подписка на пользователя")
    void follow_ShouldAddUserToFollowing_WhenBothExist() {
        Long followerId = 1L;
        Long followingId = 2L;

        User follower = new User();
        follower.setId(followerId);
        follower.setFollowing(new java.util.HashSet<>());

        User following = new User();
        following.setId(followingId);

        when(repo.findById(followerId)).thenReturn(Optional.of(follower));
        when(repo.findById(followingId)).thenReturn(Optional.of(following));

        userService.follow(followerId, followingId);

        assertTrue(follower.getFollowing().contains(following));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("Ошибка при подписке на самого себя")
    void follow_ShouldThrowException_WhenIdsAreSame() {
        Long id = 5L;
        assertThrows(BadRequestException.class, () -> userService.follow(id, id));
    }
}