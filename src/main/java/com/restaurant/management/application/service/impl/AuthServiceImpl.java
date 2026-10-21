package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.LoginCommand;
import com.restaurant.management.application.service.AuthService;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.port.PasswordHasher;
import com.restaurant.management.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordHasher passwordHasher;

    @Override
    public Optional<User> validateLogin(LoginCommand command) {
        return repository.findByLogin(command.login())
                .filter(user -> passwordHasher.matches(command.password(), user.getPassword()));
    }
}
