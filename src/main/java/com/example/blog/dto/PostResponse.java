package com.example.blog.dto;

import com.example.blog.model.Comment;
import com.example.blog.model.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private String authorUsername;
    private Set<Tag> tags;
    private Set<Comment> comments;
}