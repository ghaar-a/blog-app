package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String content;

    private Set<String> tags;  // Nomes das tags
}