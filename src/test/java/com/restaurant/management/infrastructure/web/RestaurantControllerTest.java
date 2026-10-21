package com.restaurant.management.infrastructure.web;

import com.restaurant.management.application.command.SaveRestaurantCommand;
import com.restaurant.management.application.service.RestaurantService;
import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.infrastructure.web.dto.request.CreateRestaurantRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class RestaurantControllerTest {

    private final RestaurantService service = Mockito.mock(RestaurantService.class);
    private final RestaurantControllerV1 controller = new RestaurantControllerV1(service);

    private Restaurant restaurant(Long id, String nome, String endereco, String tipoCozinha,
                                  String horario, Long ownerId) {
        User owner = new User();
        owner.setId(ownerId);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setNome(nome);
        restaurant.setEndereco(endereco);
        restaurant.setTipoCozinha(tipoCozinha);
        restaurant.setHorarioFuncionamento(horario);
        restaurant.setOwner(owner);
        return restaurant;
    }

    @Test
    void testCreateRestaurant() {
        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Restaurante Central", "Rua A, 123", "Italiana", "10h-22h", 5L);

        Mockito.when(service.create(any(SaveRestaurantCommand.class)))
                .thenReturn(restaurant(100L, "Restaurante Central", "Rua A, 123", "Italiana", "10h-22h", 5L));

        var result = controller.create(request);

        ArgumentCaptor<SaveRestaurantCommand> captor = ArgumentCaptor.forClass(SaveRestaurantCommand.class);
        Mockito.verify(service).create(captor.capture());
        assertEquals(5L, captor.getValue().ownerId());
        assertEquals(201, result.getStatusCode().value());
        assertEquals(100L, result.getBody().id());
        assertEquals("Restaurante Central", result.getBody().nome());
        assertEquals(5L, result.getBody().ownerId());
    }

    @Test
    void testGetAllRestaurants() {
        Mockito.when(service.findAll()).thenReturn(List.of(
                restaurant(1L, "Restaurante A", "Rua 1", "Japonesa", "9h-21h", 2L),
                restaurant(2L, "Restaurante B", "Rua 2", "Italiana", "10h-22h", 3L)));

        var result = controller.findAll();

        assertEquals("Restaurante A", result.getBody().get(0).nome());
        assertEquals("Restaurante B", result.getBody().get(1).nome());
    }

    @Test
    void testGetRestaurantById() {
        Mockito.when(service.findById(10L))
                .thenReturn(restaurant(10L, "Restaurante X", "Rua X", "Brasileira", "11h-23h", 4L));

        var result = controller.findById(10L).getBody();

        assertEquals(10L, result.id());
        assertEquals("Brasileira", result.tipoCozinha());
    }

    @Test
    void testUpdateRestaurant() {
        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Restaurante Atualizado", "Rua Nova, 456", "Mexicana", "12h-00h", 6L);

        Mockito.when(service.update(eq(20L), any(SaveRestaurantCommand.class)))
                .thenReturn(restaurant(20L, "Restaurante Atualizado", "Rua Nova, 456", "Mexicana", "12h-00h", 6L));

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
