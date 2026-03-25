package com.example.webapp.service;

import com.example.webapp.dto.user.CreateUserRequestDTO;
import com.example.webapp.dto.user.UpdateUserRequestDTO;
import com.example.webapp.dto.user.UserResponseDTO;
import com.example.webapp.entity.User;
import com.example.webapp.event.UserRegisteredEvent;
import com.example.webapp.exception.*;
import com.example.webapp.mapper.UserMapper;
import com.example.webapp.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

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
        // сырой пароль из DTO превращается в хэш
        String encodedPassword = passwordEncoder.encode(dto.password());
        entity.setPassword(encodedPassword);

        User saved = repo.save(entity);

        // асинхронная задача
        // вместо asyncService.processExternalTask(saved.getUsername());
        // публикуется событие
        eventPublisher.publishEvent(new UserRegisteredEvent(saved.getUsername()));

        log.info("User successfully saved with ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public UserResponseDTO update(Long id, UpdateUserRequestDTO dto) {
        User user = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getEmail().equals(dto.email()) && repo.existsByEmail(dto.email())) {
            throw new BadRequestException("Email already used");
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
            throw new ResourceNotFoundException("User not found");
        }
        repo.deleteById(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new BadRequestException("Вы не можете подписаться на самого себя");
        }

        // достаем из БД двух реальных людей
        // Метод findById достает из БД только данные самой строки таблицы users
        // связанные коллекции (подписки/подписчики) остаются в состоянии Proxy,
        // они подгрузятся из базы только в тот момент выхзова .get() на этих коллекциях
        User follower = repo.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Кто подписывается - не найден"));
        User following = repo.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("На кого подписываются - не найден"));

        // добавление в коллекцию following пользователя follower
        // Hibernate сам сделает INSERT в таблицу subscriptions
        follower.getFollowing().add(following);

        log.info("Пользователь {} подписался на {}", follower.getUsername(), following.getUsername());
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = repo.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // удаление из коллекции
        // Hibernate сам сделает DELETE из таблицы
        follower.getFollowing().removeIf(u -> u.getId().equals(followingId));
    }
}