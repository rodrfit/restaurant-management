package com.restaurant.management.application.service;

import com.restaurant.management.application.command.SaveRestaurantCommand;
import com.restaurant.management.domain.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<Restaurant> findAll();
    Restaurant findById(Long id);
    Restaurant create(SaveRestaurantCommand request);
    Restaurant update(Long id, SaveRestaurantCommand request);
    void delete(Long id);
}
