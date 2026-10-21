package com.restaurant.management.domain.repository;

import com.restaurant.management.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    void delete(User user);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    Optional<User> findByLogin(String login);
    List<User> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
