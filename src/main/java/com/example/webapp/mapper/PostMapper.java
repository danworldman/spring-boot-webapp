package com.example.webapp.mapper;

import com.example.webapp.dto.post.CreatePostRequestDTO;
import com.example.webapp.dto.post.PostResponseDTO;
import com.example.webapp.entity.Post;
import com.example.webapp.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostMapper {
    public Post toEntity(CreatePostRequestDTO dto){
        if (dto == null) return null;
        Post post = new Post();
        post.setContent(dto.content());
        return post;
    }

    public PostResponseDTO toDto(Post post){
        if (post == null) return null;

        Long userId = Optional.ofNullable(post.getUser())
                .map(User::getId)
                .orElse(null);

        return new PostResponseDTO(post.getId(),
                userId,
                post.getContent()
        );
    }
}