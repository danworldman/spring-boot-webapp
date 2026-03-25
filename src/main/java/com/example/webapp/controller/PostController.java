package com.example.webapp.controller;

import com.example.webapp.dto.post.CreatePostRequestDTO;
import com.example.webapp.dto.post.PostResponseDTO;
import com.example.webapp.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;

    @GetMapping
    public List<PostResponseDTO> getAll(
            @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return service.getAll(pageable);
    }

    // посты только тех, на кого подписан
    @GetMapping("/feed")
    public List<PostResponseDTO> getMyFeed(
            Principal principal,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        return service.getFeedByUsername(principal.getName(), pageable);
    }

    @PostMapping
    public PostResponseDTO create(@Valid @RequestBody CreatePostRequestDTO dto, Principal principal) {
        return service.create(dto, principal.getName());
    }
}