package com.restaurant.management.infrastructure.web;

import com.restaurant.management.application.command.SaveMenuItemCommand;
import com.restaurant.management.application.service.MenuItemService;
import com.restaurant.management.domain.model.MenuItem;
import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.infrastructure.web.dto.request.CreateMenuItemRequest;
import com.restaurant.management.infrastructure.web.dto.response.MenuItemResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MenuItemControllerTest {

    private final MenuItemService service = Mockito.mock(MenuItemService.class);
    private final MenuItemControllerV1 controller = new MenuItemControllerV1(service);

    private MenuItem menuItem(Long id, String nome, String descricao, Double preco, Boolean disponivel,
                              String fotoPath, Long restaurantId) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        MenuItem item = new MenuItem();
        item.setId(id);
        item.setNome(nome);
        item.setDescricao(descricao);
        item.setPreco(preco);
        item.setDisponivelNoLocal(disponivel);
        item.setFotoPath(fotoPath);
        item.setRestaurant(restaurant);
        return item;
    }

    @Test
    void testCreateMenuItem() {
        CreateMenuItemRequest request = new CreateMenuItemRequest(
                "Pizza", "Deliciosa pizza", 50.0, true, "/img/pizza.png", 1L);

        Mockito.when(service.create(any(SaveMenuItemCommand.class)))
                .thenReturn(menuItem(10L, "Pizza", "Deliciosa pizza", 50.0, true, "/img/pizza.png", 1L));

        ResponseEntity<MenuItemResponse> result = controller.create(request);

        ArgumentCaptor<SaveMenuItemCommand> captor = ArgumentCaptor.forClass(SaveMenuItemCommand.class);
        Mockito.verify(service).create(captor.capture());
        assertEquals("Pizza", captor.getValue().nome());
        assertEquals(1L, captor.getValue().restaurantId());
        assertEquals(201, result.getStatusCode().value());
        assertEquals(10L, result.getBody().id());
        assertEquals("Pizza", result.getBody().nome());
        assertEquals(1L, result.getBody().restaurantId());
    }

    @Test
    void testGetAllMenuItems() {
        Mockito.when(service.findAll()).thenReturn(List.of(
                menuItem(1L, "Pizza", "Queijo", 40.0, true, "/img/pizza.png", 1L),
                menuItem(2L, "Hamburguer", "Carne", 25.0, false, "/img/burger.png", 1L)));

        var result = controller.findAll();

        assertEquals("Pizza", result.getBody().get(0).nome());
        assertEquals("Hamburguer", result.getBody().get(1).nome());
    }

    @Test
    void testGetMenuItemById() {
        Mockito.when(service.findById(5L))
                .thenReturn(menuItem(5L, "Sushi", "Peixe cru", 60.0, true, "/img/sushi.png", 2L));

        var result = controller.findById(5L).getBody();

        assertEquals(5L, result.id());
        assertEquals("Sushi", result.nome());
    }

    @Test
    void testUpdateMenuItem() {
        CreateMenuItemRequest request = new CreateMenuItemRequest(
                "Pizza Atualizada", "Com borda recheada", 55.0, true, "/img/pizza2.png", 1L);

        Mockito.when(service.update(eq(15L), any(SaveMenuItemCommand.class)))
                .thenReturn(menuItem(15L, "Pizza Atualizada", "Com borda recheada", 55.0, true, "/img/pizza2.png", 1L));

        var result = controller.update(15L, request).getBody();

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
