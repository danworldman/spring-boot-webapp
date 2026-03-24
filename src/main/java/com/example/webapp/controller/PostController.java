package com.example.webapp.controller;

import com.example.webapp.dto.post.CreatePostRequestDTO;
import com.example.webapp.dto.post.PostResponseDTO;
import com.example.webapp.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;

    @GetMapping
    public List<PostResponseDTO> getAll(){
        return service.getAll();
    }

    @PostMapping
    public PostResponseDTO create(@Valid @RequestBody CreatePostRequestDTO dto, Principal principal) {
        String username = principal.getName();
        return service.create(dto, username);
    }
}