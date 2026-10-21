package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.SaveMenuItemCommand;
import com.restaurant.management.domain.exception.MenuItemNotFoundException;
import com.restaurant.management.domain.exception.RestaurantNotFoundException;
import com.restaurant.management.domain.model.MenuItem;
import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.domain.repository.MenuItemRepository;
import com.restaurant.management.domain.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class MenuItemServiceImplTest {

    private final MenuItemRepository menuItemRepository = Mockito.mock(MenuItemRepository.class);
    private final RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);
    private final MenuItemServiceImpl service = new MenuItemServiceImpl(menuItemRepository, restaurantRepository);

    private final SaveMenuItemCommand command = new SaveMenuItemCommand(
            "Pizza", "Deliciosa pizza", 50.0, true, "/img/pizza.png", 1L);

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

        MenuItem created = service.create(command);

        assertEquals("Pizza", created.getNome());
        assertEquals(10L, created.getId());
        assertEquals(1L, created.getRestaurant().getId());
    }

    @Test
    void testCreateMenuItemWithUnknownRestaurantThrows() {
        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> service.create(command));
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void testFindByIdNotFoundThrows() {
        Mockito.when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void testUpdateMenuItem() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        MenuItem existing = new MenuItem();
        existing.setId(7L);
        existing.setNome("Antigo");

        Mockito.when(menuItemRepository.findById(7L)).thenReturn(Optional.of(existing));
        Mockito.when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        Mockito.when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem updated = service.update(7L, command);

        assertEquals(7L, updated.getId());
        assertEquals("Pizza", updated.getNome());
        assertEquals(50.0, updated.getPreco());
    }

    @Test
    void testDeleteMenuItem() {
        Mockito.when(menuItemRepository.existsById(3L)).thenReturn(true);

        service.delete(3L);

        verify(menuItemRepository).deleteById(3L);
    }

    @Test
    void testDeleteMenuItemNotFoundThrows() {
        Mockito.when(menuItemRepository.existsById(3L)).thenReturn(false);

        assertThrows(MenuItemNotFoundException.class, () -> service.delete(3L));
        verify(menuItemRepository, never()).deleteById(any());
    }
}
