package com.restaurant.management.service.impl;

import com.restaurant.management.dto.request.CreateMenuItemRequest;
import com.restaurant.management.dto.response.MenuItemResponse;
import com.restaurant.management.exception.MenuItemAlreadyExistsException;
import com.restaurant.management.exception.RestaurantNotFoundException;
import com.restaurant.management.model.MenuItem;
import com.restaurant.management.model.Restaurant;
import com.restaurant.management.repository.MenuItemRepository;
import com.restaurant.management.repository.RestaurantRepository;
import com.restaurant.management.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<MenuItemResponse> findAll() {
        return menuItemRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemResponse findById(Long id) {
        return menuItemRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    @Override
    public MenuItemResponse create(CreateMenuItemRequest request) {
        if (request.id() != null && menuItemRepository.existsById(request.id())) {
            throw new MenuItemAlreadyExistsException(request.id());
        }

        if (request.restaurantId() == null) {
            throw new RestaurantNotFoundException(null);
        }

        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(request.restaurantId()));

        MenuItem menuItem = new MenuItem();
        if (request.id() != null) {
            menuItem.setId(request.id());
        }
        menuItem.setNome(request.nome());
        menuItem.setDescricao(request.descricao());
        menuItem.setPreco(request.preco());
        menuItem.setDisponivelNoLocal(request.disponivelNoLocal());
        menuItem.setFotoPath(request.fotoPath());
        menuItem.setRestaurant(restaurant);

        MenuItem saved = menuItemRepository.save(menuItem);
        return toResponse(saved);
    }

    @Override
    public MenuItemResponse update(Long id, CreateMenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        menuItem.setNome(request.nome());
        menuItem.setDescricao(request.descricao());
        menuItem.setPreco(request.preco());
        menuItem.setDisponivelNoLocal(request.disponivelNoLocal());
        menuItem.setFotoPath(request.fotoPath());
        menuItem.setRestaurant(restaurant);

        MenuItem updated = menuItemRepository.save(menuItem);
        return toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getNome(),
                menuItem.getDescricao(),
                menuItem.getPreco(),
                menuItem.getDisponivelNoLocal(),
                menuItem.getFotoPath(),
                menuItem.getRestaurant().getId()
        );
    }
}