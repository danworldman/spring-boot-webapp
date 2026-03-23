package com.example.webapp.repository;

import com.example.webapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    // нужно для аутентификации
    Optional<User> findByUsername(String username);

    // @EntityGraph(attributePaths = {"posts", "tasks"})
    //Page<User> findAll(Pageable pageable);
}