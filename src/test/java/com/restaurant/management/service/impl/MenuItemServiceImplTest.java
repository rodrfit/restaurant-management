package com.restaurant.management.service.impl;

import com.restaurant.management.dto.request.CreateMenuItemRequest;
import com.restaurant.management.model.MenuItem;
import com.restaurant.management.model.Restaurant;
import com.restaurant.management.repository.MenuItemRepository;
import com.restaurant.management.repository.RestaurantRepository;
import com.restaurant.management.exception.MenuItemAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class MenuItemServiceImplTest {

    private final MenuItemRepository menuItemRepository = Mockito.mock(MenuItemRepository.class);
    private final RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);
    private final MenuItemServiceImpl service = new MenuItemServiceImpl(menuItemRepository, restaurantRepository);

    @Test
    void testCreateMenuItem() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        Mockito.when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem item = invocation.getArgument(0);
            item.setId(10L);
            return item;
        });

        CreateMenuItemRequest request = new CreateMenuItemRequest(
                "Pizza", "Deliciosa pizza", 50.0, true, "/img/pizza.png", 1L);

        var response = service.create(request);

        assertEquals("Pizza", response.nome());
        assertEquals(10L, response.id());
        assertEquals(1L, response.restaurantId());
    }

    @Test
    void testCreateMenuItemThrowsConflictWhenIdAlreadyExists() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        Mockito.when(menuItemRepository.existsById(10L)).thenReturn(true);

        CreateMenuItemRequest request = new CreateMenuItemRequest(
                10L, "Pizza", "Deliciosa pizza", 50.0, true, "/img/pizza.png", 1L);

        var exception = assertThrows(MenuItemAlreadyExistsException.class, () -> service.create(request));

        assertEquals("Item de menu ja cadastrado", exception.getMessage());
    }
}
