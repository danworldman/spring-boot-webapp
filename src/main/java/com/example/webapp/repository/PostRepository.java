package com.example.webapp.repository;

import com.example.webapp.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // посты конкретного пользователя
    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    // посты авторов, на которых подписан username
    @Query("SELECT p FROM Post p " +
            "JOIN FETCH p.user u " + // FETCH позволяет сразу получить все данные автора
            "JOIN u.followers f " +
            "WHERE f.username = :username")
    Page<Post> findPostsByFollowerUsername(@Param("username") String username, Pageable pageable);
}