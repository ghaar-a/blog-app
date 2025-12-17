package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    @NotBlank
    private String content;

    private Long parentCommentId;  // Opcional para replies
}