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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper mapper;

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAll(){
        return postRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public PostResponseDTO create(CreatePostRequestDTO dto, String username) {
        // 1. Ищем автора по логину (username), который пришел из контроллера
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // 2. Мапим контент поста
        Post post = mapper.toEntity(dto);

        // 3. ПРИВЯЗЫВАЕМ автора (теперь это 100% тот, кто залогинен)
        post.setUser(author);

        return mapper.toDto(postRepository.save(post));
    }

}
