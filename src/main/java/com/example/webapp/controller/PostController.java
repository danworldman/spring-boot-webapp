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
        // Вызываем сервис и передаем туда логин (например, "admin")
        // Мы берем имя (логин) из защищенного контекста, а не из JSON
        String username = principal.getName();
        return service.create(dto, username);
    }
}