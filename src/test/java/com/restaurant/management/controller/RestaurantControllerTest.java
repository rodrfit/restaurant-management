package com.restaurant.management.controller;

import com.restaurant.management.dto.request.CreateRestaurantRequest;
import com.restaurant.management.dto.response.RestaurantResponse;
import com.restaurant.management.service.RestaurantService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class RestaurantControllerTest {

    private final RestaurantService service = Mockito.mock(RestaurantService.class);
    private final RestaurantControllerV1 controller = new RestaurantControllerV1(service);

    @Test
    void testCreateRestaurant() {
        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Restaurante Central", "Rua A, 123", "Italiana", "10h-22h", 5L);

        RestaurantResponse response = new RestaurantResponse(
                100L, "Restaurante Central", "Rua A, 123", "Italiana", "10h-22h", 5L);

        Mockito.when(service.create(any(CreateRestaurantRequest.class))).thenReturn(response);

        var result = controller.create(request).getBody();

        assertEquals(100L, result.id());
        assertEquals("Restaurante Central", result.nome());
    }

    @Test
    void testGetAllRestaurants() {
        RestaurantResponse r1 = new RestaurantResponse(1L, "Restaurante A", "Rua 1", "Japonesa", "9h-21h", 2L);
        RestaurantResponse r2 = new RestaurantResponse(2L, "Restaurante B", "Rua 2", "Italiana", "10h-22h", 3L);

        Mockito.when(service.findAll()).thenReturn(List.of(r1, r2));

        var result = controller.findAll().getBody();

        assertEquals("Restaurante A", result.get(0).nome());
        assertEquals("Restaurante B", result.get(1).nome());
    }

    @Test
    void testGetRestaurantById() {
        RestaurantResponse response = new RestaurantResponse(
                10L, "Restaurante X", "Rua X", "Brasileira", "11h-23h", 4L);

        Mockito.when(service.findById(10L)).thenReturn(response);

        var result = controller.findById(10L).getBody();

        assertEquals(10L, result.id());
        assertEquals("Brasileira", result.tipoCozinha());
    }

    @Test
    void testUpdateRestaurant() {
        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Restaurante Atualizado", "Rua Nova, 456", "Mexicana", "12h-00h", 6L);

        RestaurantResponse response = new RestaurantResponse(
                20L, "Restaurante Atualizado", "Rua Nova, 456", "Mexicana", "12h-00h", 6L);

        Mockito.when(service.update(eq(20L), any(CreateRestaurantRequest.class))).thenReturn(response);

        var result = controller.update(20L, request).getBody();

        assertEquals("Restaurante Atualizado", result.nome());
        assertEquals("Mexicana", result.tipoCozinha());
    }

    @Test
    void testDeleteRestaurant() {
        var response = controller.delete(30L);
        Mockito.verify(service).delete(30L);
        assertEquals(204, response.getStatusCode().value());
    }
}
