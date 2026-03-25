package com.example.webapp.service;

import com.example.webapp.dto.post.CreatePostRequestDTO;
import com.example.webapp.dto.post.PostResponseDTO;
import com.example.webapp.entity.Post;
import com.example.webapp.entity.User;
import com.example.webapp.exception.ResourceNotFoundException;
import com.example.webapp.mapper.PostMapper;
import com.example.webapp.repository.PostRepository;
import com.example.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper mapper;

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAll(Pageable pageable) {
        log.info("Fetching all posts with pageable: {}", pageable);
        return postRepository.findAll(pageable)
                .map(mapper::toDto)
                .getContent();
    }

    // посты тех, на кого подписан пользователь
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getFeedByUsername(String username, Pageable pageable) {
        log.info("Fetching feed for user: {}", username);
        return postRepository.findPostsByFollowerUsername(username, pageable)
                .map(mapper::toDto)
                .getContent();
    }

    @Transactional
    public PostResponseDTO create(CreatePostRequestDTO dto, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Post post = mapper.toEntity(dto);
        post.setUser(author);

        log.info("User {} created a new post", username);
        return mapper.toDto(postRepository.save(post));
    }
}