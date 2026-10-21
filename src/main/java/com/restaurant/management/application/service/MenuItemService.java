package com.restaurant.management.application.service;

import com.restaurant.management.application.command.SaveMenuItemCommand;
import com.restaurant.management.domain.model.MenuItem;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> findAll();
    MenuItem findById(Long id);
    MenuItem create(SaveMenuItemCommand request);
    MenuItem update(Long id, SaveMenuItemCommand request);
    void delete(Long id);
}
