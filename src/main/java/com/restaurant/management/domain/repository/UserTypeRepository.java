package com.restaurant.management.domain.repository;

import com.restaurant.management.domain.model.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeRepository {

    Optional<UserType> findById(Long id);

    List<UserType> findAll();

    UserType save(UserType userType);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByNameIgnoreCase(String name);

    List<UserType> findByNameContainingIgnoreCase(String name);

    Optional<UserType> findByNameIgnoreCase(String name);
}
