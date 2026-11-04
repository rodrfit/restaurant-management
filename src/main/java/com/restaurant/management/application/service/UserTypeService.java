package com.restaurant.management.application.service;

import com.restaurant.management.application.command.CreateUserTypeCommand;
import com.restaurant.management.application.command.UpdateUserTypeCommand;
import com.restaurant.management.domain.model.UserType;

import java.util.List;

public interface UserTypeService {

    UserType create(CreateUserTypeCommand request);

    UserType update(Long id, UpdateUserTypeCommand request);

    void delete(Long id);

    UserType findById(Long id);

    List<UserType> findAll();
}
