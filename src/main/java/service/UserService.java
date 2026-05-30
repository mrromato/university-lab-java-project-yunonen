package com.example.bookcatalog.service;

import com.example.bookcatalog.domain.User;
import com.example.bookcatalog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        log.debug("Запрос списка всех пользователей");
        return userRepository.findAll();
    }

    public User registerUser(User user) {
        log.info("Регистрация нового пользователя: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        log.info("Пользователь успешно зарегистрирован с id: {}", saved.getId());
        return saved;
    }
}