package com.example.blog.service;

import com.example.blog.dto.RegisterRequest;
import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.register(request);

        assertEquals("test", user.getUsername());
        assertEquals("hashed", user.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_duplicateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.register(request));
    }
}