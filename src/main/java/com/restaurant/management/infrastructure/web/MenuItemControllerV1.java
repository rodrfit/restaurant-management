package com.restaurant.management.infrastructure.web;

import com.restaurant.management.infrastructure.web.dto.request.CreateMenuItemRequest;
import com.restaurant.management.infrastructure.web.dto.response.MenuItemResponse;
import com.restaurant.management.application.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
@Tag(name = "Itens do Menu", description = "Gestão de itens do menu do sistema")
public class MenuItemControllerV1 {

    private final MenuItemService service;

    @PostMapping
    @Operation(summary = "Criar item de menu", description = "Cria um novo item de menu no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Item de menu criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuItemResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<MenuItemResponse> create(@RequestBody @Valid CreateMenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(MenuItemResponse.from(service.create(request.toCommand())));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item de menu", description = "Atualiza os dados de um item de menu existente pelo seu ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item de menu atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuItemResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item de menu ou restaurante não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<MenuItemResponse> update(
            @Parameter(name = "id", description = "ID do item de menu a ser atualizado", example = "1")
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateMenuItemRequest request) {
        return ResponseEntity.ok(MenuItemResponse.from(service.update(id, request.toCommand())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar item de menu", description = "Deleta um item de menu a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Item de menu deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item de menu não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", description = "ID do item de menu a ser deletado", example = "1")
            @PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar itens do menu", description = "Lista os itens do menu cadastrados no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MenuItemResponse.class))
                    )
            )
    })
    public ResponseEntity<List<MenuItemResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(MenuItemResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item de menu", description = "Busca um item de menu a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item de menu encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuItemResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item de menu não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<MenuItemResponse> findById(
            @Parameter(name = "id", description = "ID do item de menu", example = "1")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(MenuItemResponse.from(service.findById(id)));
    }
}

