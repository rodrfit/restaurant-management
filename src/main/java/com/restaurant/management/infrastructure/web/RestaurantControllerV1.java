package com.restaurant.management.infrastructure.web;

import com.restaurant.management.infrastructure.web.dto.request.CreateRestaurantRequest;
import com.restaurant.management.infrastructure.web.dto.response.RestaurantResponse;
import com.restaurant.management.application.service.RestaurantService;
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
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Gestão de restaurantes do sistema")
public class RestaurantControllerV1 {

    private final RestaurantService service;

    @PostMapping
    @Operation(summary = "Criar restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Restaurante criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponse.class)
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
                    description = "Dono do restaurante não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<RestaurantResponse> create(@RequestBody @Valid CreateRestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantResponse.from(service.create(request.toCommand())));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente pelo seu ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurante atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponse.class)
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
                    description = "Restaurante ou dono não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<RestaurantResponse> update(
            @Parameter(name = "id", description = "ID do restaurante a ser atualizado", example = "1")
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateRestaurantRequest request) {
        return ResponseEntity.ok(RestaurantResponse.from(service.update(id, request.toCommand())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar restaurante", description = "Deleta um restaurante a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Restaurante deletado com sucesso",
                    content = @Content
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
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", description = "ID do restaurante a ser deletado", example = "1")
            @PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Lista os restaurantes cadastrados no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestaurantResponse.class))
                    )
            )
    })
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(RestaurantResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante", description = "Busca um restaurante a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurante encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponse.class)
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
    public ResponseEntity<RestaurantResponse> findById(
            @Parameter(name = "id", description = "ID do restaurante", example = "1")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(RestaurantResponse.from(service.findById(id)));
    }
}

