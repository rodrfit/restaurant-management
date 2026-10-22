package com.restaurant.management.service.impl;

import com.restaurant.management.dto.request.CreateRestaurantRequest;
import com.restaurant.management.model.Restaurant;
import com.restaurant.management.model.RestaurantOwner;
import com.restaurant.management.repository.RestaurantRepository;
import com.restaurant.management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class RestaurantServiceImplTest {

    private final RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final RestaurantServiceImpl service = new RestaurantServiceImpl(restaurantRepository, userRepository);

    @Test
    void testCreateRestaurant() {
        RestaurantOwner owner = new RestaurantOwner();
        owner.setId(5L);

        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.of(owner));
        Mockito.when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> {
            Restaurant r = invocation.getArgument(0);
            r.setId(100L);
            return r;
        });

        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Restaurante Central", "Rua A, 123", "Italiana", "10h-22h", 5L);

        var response = service.create(request);

        assertEquals("Restaurante Central", response.nome());
        assertEquals(100L, response.id());
        assertEquals(5L, response.ownerId());
    }
}
