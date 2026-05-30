package com.example.bookcatalog.service;

import com.example.bookcatalog.domain.User;
import com.example.bookcatalog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldHashPasswordAndSave() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("plainpassword");

        when(passwordEncoder.encode("plainpassword")).thenReturn("hashed_plainpassword");
        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userService.registerUser(user);

        // Then
        assertEquals("hashed_plainpassword", result.getPassword());
        verify(passwordEncoder, times(1)).encode("plainpassword");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        // Given
        User u1 = new User();
        u1.setUsername("alice");
        User u2 = new User();
        u2.setUsername("bob");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals("alice", result.get(0).getUsername());
        assertEquals("bob", result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }
}