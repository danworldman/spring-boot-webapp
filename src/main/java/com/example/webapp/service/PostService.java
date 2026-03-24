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
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Post post = mapper.toEntity(dto);

        post.setUser(author);

        return mapper.toDto(postRepository.save(post));
    }
}