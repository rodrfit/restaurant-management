package com.restaurant.management.infrastructure.persistence.adapter;

import com.restaurant.management.domain.model.MenuItem;
import com.restaurant.management.domain.repository.MenuItemRepository;
import com.restaurant.management.infrastructure.persistence.JpaMenuItemRepository;
import com.restaurant.management.infrastructure.persistence.mapper.MenuItemMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MenuItemRepositoryAdapter implements MenuItemRepository {
    private final JpaMenuItemRepository delegate;

    public MenuItemRepositoryAdapter(JpaMenuItemRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return delegate.findById(id).map(MenuItemMapper::toDomain);
    }

    @Override
    public List<MenuItem> findAll() {
        return delegate.findAll().stream().map(MenuItemMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public MenuItem save(MenuItem menuItem) {
        return MenuItemMapper.toDomain(delegate.save(Objects.requireNonNull(MenuItemMapper.toEntity(menuItem))));
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
    public List<MenuItem> findByNameContainingIgnoreCase(String name) {
        return delegate.findByNomeContainingIgnoreCase(name).stream().map(MenuItemMapper::toDomain).toList();
    }

    @Override
    public Optional<MenuItem> findByNameIgnoreCase(String name) {
        return delegate.findAll().stream()
                .filter(entity -> Objects.nonNull(entity.getNome()) && entity.getNome().equalsIgnoreCase(name))
                .findFirst()
                .map(MenuItemMapper::toDomain);
    }
}
