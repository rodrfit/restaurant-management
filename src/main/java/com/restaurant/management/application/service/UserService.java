package com.restaurant.management.application.service;

import com.restaurant.management.application.command.CreateUserCommand;
import com.restaurant.management.application.command.UpdateUserCommand;
import com.restaurant.management.application.command.ChangePasswordCommand;
import com.restaurant.management.domain.model.User;

import java.util.List;

public interface UserService {
    User create(CreateUserCommand request);
    User update(Long id, UpdateUserCommand request);
    void updatePassword(Long id, ChangePasswordCommand request);
    void delete(Long id);
    User findById(Long id);
    User assignType(Long userId, Long userTypeId);
    List<User> findAll(String name);
}
