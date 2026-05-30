package com.example.bookcatalog.controller;

import com.example.bookcatalog.domain.User;
import com.example.bookcatalog.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void registerUser_shouldReturnOk() {
        User user = new User();
        user.setUsername("roma");
        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.registerUser(user);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("roma", response.getBody().getUsername());
    }

    @Test
    void registerUser_shouldCallServiceOnce() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("secret");
        when(userService.registerUser(any(User.class))).thenReturn(user);

        userController.registerUser(user);

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void registerUser_shouldReturnSavedUser() {
        User input = new User();
        input.setUsername("bob");
        input.setEmail("bob@example.com");

        User saved = new User();
        saved.setId(42L);
        saved.setUsername("bob");
        saved.setEmail("bob@example.com");
        saved.setRole("ROLE_USER");

        when(userService.registerUser(any(User.class))).thenReturn(saved);

        ResponseEntity<User> response = userController.registerUser(input);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(42L, response.getBody().getId());
        assertEquals("bob@example.com", response.getBody().getEmail());
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userService.getAllUsers()).thenReturn(List.of(new User()));

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllUsers_whenEmpty_shouldReturnEmptyList() {
        when(userService.getAllUsers()).thenReturn(List.of());

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAllUsers_shouldCallServiceOnce() {
        when(userService.getAllUsers()).thenReturn(List.of());

        userController.getAllUsers();

        verify(userService, times(1)).getAllUsers();
    }
}
