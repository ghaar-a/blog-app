package com.example.blog.dto;

import lombok.Data;

@Data
public class AuthResponse {  // retornar JWT
    private String token;
}