package com.restaurant.management.controller;

import com.restaurant.management.dto.request.CreateMenuItemRequest;
import com.restaurant.management.dto.response.MenuItemResponse;
import com.restaurant.management.service.MenuItemService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MenuItemControllerTest {

    private final MenuItemService service = Mockito.mock(MenuItemService.class);
    private final MenuItemControllerV1 controller = new MenuItemControllerV1(service);

    @Test
    void testCreateMenuItem() {
        CreateMenuItemRequest request = new CreateMenuItemRequest(
                "Pizza", "Deliciosa pizza", 50.0, true, "/img/pizza.png", 1L);

        MenuItemResponse response = new MenuItemResponse(
                10L, "Pizza", "Deliciosa pizza", 50.0, true, "/img/pizza.png", 1L);

        Mockito.when(service.create(any(CreateMenuItemRequest.class))).thenReturn(response);

        var resultEntity = controller.create(request);
        var body = resultEntity.getBody();

        assertEquals(10L, body.id());
        assertEquals("Pizza", body.nome());
    }

    @Test
    void testGetAllMenuItems() {
        MenuItemResponse i1 = new MenuItemResponse(1L, "Pizza", "Queijo", 40.0, true, "/img/pizza.png", 1L);
        MenuItemResponse i2 = new MenuItemResponse(2L, "Hamburguer", "Carne", 25.0, false, "/img/burger.png", 1L);

        Mockito.when(service.findAll()).thenReturn(List.of(i1, i2));

        var result = controller.findAll().getBody();

        assertEquals("Pizza", result.get(0).nome());
        assertEquals("Hamburguer", result.get(1).nome());
    }

    @Test
    void testGetMenuItemById() {
        MenuItemResponse response = new MenuItemResponse(
                5L, "Sushi", "Peixe cru", 60.0, true, "/img/sushi.png", 2L);

        Mockito.when(service.findById(5L)).thenReturn(response);

        var resultEntity = controller.findById(5L);
        var result = resultEntity.getBody();

        assertEquals(5L, result.id());
        assertEquals("Sushi", result.nome());
    }

    @Test
    void testUpdateMenuItem() {
        CreateMenuItemRequest request = new CreateMenuItemRequest(
                "Pizza Atualizada", "Com borda recheada", 55.0, true, "/img/pizza2.png", 1L);

        MenuItemResponse response = new MenuItemResponse(
                15L, "Pizza Atualizada", "Com borda recheada", 55.0, true, "/img/pizza2.png", 1L);

        Mockito.when(service.update(eq(15L), any(CreateMenuItemRequest.class))).thenReturn(response);

        var resultEntity = controller.update(15L, request);
        var result = resultEntity.getBody();

        assertEquals("Pizza Atualizada", result.nome());
        assertEquals("Com borda recheada", result.descricao());
    }

    @Test
    void testDeleteMenuItem() {
        var response = controller.delete(20L);
        Mockito.verify(service).delete(20L);
        assertEquals(204, response.getStatusCode().value());
    }
}
