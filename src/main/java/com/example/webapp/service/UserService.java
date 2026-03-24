package com.example.webapp.service;

import com.example.webapp.dto.user.CreateUserRequestDTO;
import com.example.webapp.dto.user.UpdateUserRequestDTO;
import com.example.webapp.dto.user.UserResponseDTO;
import com.example.webapp.entity.User;
import com.example.webapp.exception.*;
import com.example.webapp.mapper.UserMapper;
import com.example.webapp.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j // логгер
@RequiredArgsConstructor // Lombok для конструктора
@Service
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AsyncService asyncService;

    public UserResponseDTO getById(Long id) {
        return repo.findById(id)
                .map(mapper::toDto)
                // Вместо IllegalArgumentException теперь тут кастомный 404
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Cacheable(value = "users", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll(Pageable pageable) {
        log.info("Fetching page of users: {}", pageable);
        return repo.findAll(pageable)
                .map(mapper::toDto)
                .getContent();
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public UserResponseDTO create(CreateUserRequestDTO dto) {
        log.info("Starting to create user with email: {}", dto.email()); // {} - плейсхолдер

        if (repo.existsByEmail(dto.email())) {
            log.warn("Failed to create user: email {} already exists", dto.email());
            throw new BadRequestException("Email is already taken");
        }

        // мапим поля имя, email, логин и роль (ушло в маппер для чистоты сервиса)
        User entity = mapper.toEntity(dto);

        // хешируется пароль
        // тут сырой пароль из DTO и превращается в хэш
        String encodedPassword = passwordEncoder.encode(dto.password());
        entity.setPassword(encodedPassword);

        User saved = repo.save(entity);

        // асинхронная задача
        asyncService.processExternalTask(saved.getUsername());

        log.info("User successfully saved with ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public UserResponseDTO update(Long id, UpdateUserRequestDTO dto) {
        User user = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getEmail().equals(dto.email()) && repo.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already used");
        }

        mapper.updateEntity(user, dto);

        // repo.save(user) здесь НЕ нужен.
        // В конце метода транзакция закроется, и Hibernate сам сделает апдейт в БД
        return mapper.toDto(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        repo.deleteById(id);
    }
}