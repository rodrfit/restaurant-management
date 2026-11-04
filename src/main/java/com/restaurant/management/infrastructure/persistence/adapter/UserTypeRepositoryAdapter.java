package com.restaurant.management.infrastructure.persistence.adapter;

import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.domain.repository.UserTypeRepository;
import com.restaurant.management.infrastructure.persistence.JpaUserTypeRepository;
import com.restaurant.management.infrastructure.persistence.mapper.UserTypeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserTypeRepositoryAdapter implements UserTypeRepository {
    private final JpaUserTypeRepository delegate;

    public UserTypeRepositoryAdapter(JpaUserTypeRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<UserType> findById(Long id) {
        return delegate.findById(id).map(UserTypeMapper::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return delegate.findAll().stream().map(UserTypeMapper::toDomain).toList();
    }

    @Override
    public UserType save(UserType userType) {
        return UserTypeMapper.toDomain(delegate.save(Objects.requireNonNull(UserTypeMapper.toEntity(userType))));
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
    public boolean existsByNameIgnoreCase(String name) {
        return delegate.existsByNameIgnoreCase(name);
    }

    @Override
    public List<UserType> findByNameContainingIgnoreCase(String name) {
        return delegate.findByNameContainingIgnoreCase(name).stream().map(UserTypeMapper::toDomain).toList();
    }

    @Override
    public Optional<UserType> findByNameIgnoreCase(String name) {
        return delegate.findByNameIgnoreCase(name).map(UserTypeMapper::toDomain);
    }
}
