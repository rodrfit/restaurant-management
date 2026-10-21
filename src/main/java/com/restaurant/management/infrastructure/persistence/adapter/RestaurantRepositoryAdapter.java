package com.restaurant.management.infrastructure.persistence.adapter;

import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.domain.repository.RestaurantRepository;
import com.restaurant.management.infrastructure.persistence.JpaRestaurantRepository;
import com.restaurant.management.infrastructure.persistence.mapper.RestaurantMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantRepositoryAdapter implements RestaurantRepository {
    private final JpaRestaurantRepository delegate;

    public RestaurantRepositoryAdapter(JpaRestaurantRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return delegate.findById(id).map(RestaurantMapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return delegate.findAll().stream().map(RestaurantMapper::toDomain).toList();
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return RestaurantMapper.toDomain(delegate.save(RestaurantMapper.toEntity(restaurant)));
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
    public List<Restaurant> findByNameContainingIgnoreCase(String name) {
        return delegate.findByNomeContainingIgnoreCase(name).stream().map(RestaurantMapper::toDomain).toList();
    }

    @Override
    public Optional<Restaurant> findByNameIgnoreCase(String name) {
        return delegate.findAll().stream()
                .filter(entity -> entity.getNome() != null && entity.getNome().equalsIgnoreCase(name))
                .findFirst()
                .map(RestaurantMapper::toDomain);
    }
}
