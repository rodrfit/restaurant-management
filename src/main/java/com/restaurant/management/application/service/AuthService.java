package com.restaurant.management.application.service;

import com.restaurant.management.application.command.LoginCommand;
import com.restaurant.management.domain.model.User;

import java.util.Optional;

public interface AuthService {
    Optional<User> validateLogin(LoginCommand request);
}
