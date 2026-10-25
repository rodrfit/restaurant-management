package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.SaveRestaurantCommand;
import com.restaurant.management.domain.exception.RestaurantNotFoundException;
import com.restaurant.management.domain.exception.UserNotFoundException;
import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.repository.RestaurantRepository;
import com.restaurant.management.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class RestaurantServiceImplTest {

    private final RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final RestaurantServiceImpl service = new RestaurantServiceImpl(restaurantRepository, userRepository);

    private final SaveRestaurantCommand command = new SaveRestaurantCommand(
            "Restaurante Central", "Rua A, 123", "Italiana", "10h-22h", 5L);

    @Test
    void testCreateRestaurant() {
        User owner = new User();
        owner.setId(5L);

        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.of(owner));
        Mockito.when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> {
            Restaurant r = invocation.getArgument(0);
            r.setId(100L);
            return r;
        });

        Restaurant created = service.create(command);

        assertEquals("Restaurante Central", created.getNome());
        assertEquals(100L, created.getId());
        assertEquals(5L, created.getOwner().getId());
    }

    @Test
    void testCreateRestaurantWithUnknownOwnerThrows() {
        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.create(command));
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testFindByIdNotFoundThrows() {
        Mockito.when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void testDeleteRestaurantNotFoundThrows() {
        Mockito.when(restaurantRepository.existsById(9L)).thenReturn(false);

        assertThrows(RestaurantNotFoundException.class, () -> service.delete(9L));
        verify(restaurantRepository, never()).deleteById(any());
    }
}
