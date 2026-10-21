package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.SaveMenuItemCommand;
import com.restaurant.management.application.service.MenuItemService;
import com.restaurant.management.domain.exception.MenuItemNotFoundException;
import com.restaurant.management.domain.exception.RestaurantNotFoundException;
import com.restaurant.management.domain.model.MenuItem;
import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.domain.repository.MenuItemRepository;
import com.restaurant.management.domain.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository,
                               RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem findById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
    }

    @Transactional
    @Override
    public MenuItem create(SaveMenuItemCommand command) {
        Restaurant restaurant = findRestaurant(command.restaurantId());
        return menuItemRepository.save(apply(new MenuItem(), command, restaurant));
    }

    @Transactional
    @Override
    public MenuItem update(Long id, SaveMenuItemCommand command) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
        Restaurant restaurant = findRestaurant(command.restaurantId());
        return menuItemRepository.save(apply(menuItem, command, restaurant));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new MenuItemNotFoundException(id);
        }
        menuItemRepository.deleteById(id);
    }

    private Restaurant findRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    private MenuItem apply(MenuItem menuItem, SaveMenuItemCommand command, Restaurant restaurant) {
        menuItem.setNome(command.nome());
        menuItem.setDescricao(command.descricao());
        menuItem.setPreco(command.preco());
        menuItem.setDisponivelNoLocal(command.disponivelNoLocal());
        menuItem.setFotoPath(command.fotoPath());
        menuItem.setRestaurant(restaurant);
        return menuItem;
    }
}
