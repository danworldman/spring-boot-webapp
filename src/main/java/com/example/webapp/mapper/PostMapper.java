package com.example.webapp.mapper;

import com.example.webapp.dto.post.CreatePostRequestDTO;
import com.example.webapp.dto.post.PostResponseDTO;
import com.example.webapp.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public Post toEntity(CreatePostRequestDTO dto){
        Post post = new Post();
        post.setContent(dto.content());
        return post;
    }

    public PostResponseDTO toDto(Post post){
        if (post == null) return null;
        return new PostResponseDTO(post.getId(),
                post.getUser().getId(),
                post.getContent()
        );
    }
}