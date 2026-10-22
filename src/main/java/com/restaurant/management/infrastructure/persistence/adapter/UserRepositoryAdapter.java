package com.restaurant.management.infrastructure.persistence.adapter;

import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.repository.UserRepository;
import com.restaurant.management.infrastructure.persistence.JpaUserRepository;
import com.restaurant.management.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository delegate;

    public UserRepositoryAdapter(JpaUserRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<User> findById(Long id) {
        return delegate.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return delegate.findAll().stream().map(UserMapper::toDomain).toList();
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(delegate.save(Objects.requireNonNull(UserMapper.toEntity(user))));
    }

    @Override
    public void delete(User user) {
        delegate.delete(Objects.requireNonNull(UserMapper.toEntity(user)));
    }

    @Override
    public void deleteById(Long id) {
        delegate.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return delegate.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return delegate.existsByEmail(email);
    }

    @Override
    public List<User> findByNameContainingIgnoreCase(String name) {
        return delegate.findByNameContainingIgnoreCase(name).stream().map(UserMapper::toDomain).toList();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return delegate.findByLogin(login).map(UserMapper::toDomain);
    }

    @Override
    public List<User> findByNameIgnoreCase(String name) {
        return delegate.findByNameContainingIgnoreCase(name).stream().map(UserMapper::toDomain).toList();
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return delegate.existsByNameIgnoreCase(name);
    }
}
