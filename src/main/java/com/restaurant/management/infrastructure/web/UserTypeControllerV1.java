package com.restaurant.management.infrastructure.web;

import com.restaurant.management.infrastructure.web.dto.request.CreateUserTypeRequest;
import com.restaurant.management.infrastructure.web.dto.request.UpdateUserTypeRequest;
import com.restaurant.management.infrastructure.web.dto.response.UserTypeResponse;
import com.restaurant.management.application.service.UserTypeService;
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
@RequestMapping("/api/v1/user-types")
@RequiredArgsConstructor
@Tag(name = "Tipos de Usuário", description = "Gestão de tipos de usuário do sistema")
public class UserTypeControllerV1 {

    private final UserTypeService service;

    @PostMapping
    @Operation(summary = "Criar tipo de usuário", description = "Cria um novo tipo de usuário no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Tipo de usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserTypeResponse.class)
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
                    responseCode = "409",
                    description = "Tipo de usuário já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<UserTypeResponse> create(@RequestBody @Valid CreateUserTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserTypeResponse.from(service.create(request.toCommand())));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza os dados de um tipo de usuário existente pelo seu ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserTypeResponse.class)
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
                    description = "Tipo de usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Tipo de usuário já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<UserTypeResponse> update(
            @Parameter(name = "id", description = "ID do tipo de usuário a ser atualizado", example = "1")
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateUserTypeRequest request) {
        return ResponseEntity.ok(UserTypeResponse.from(service.update(id, request.toCommand())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tipo de usuário", description = "Deleta um tipo de usuário a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Tipo de usuário deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", description = "ID do tipo de usuário a ser deletado", example = "1")
            @PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar tipos de usuário", description = "Lista os tipos de usuário cadastrados no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserTypeResponse.class))
                    )
            )
    })
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        return ResponseEntity.ok(service.findAll().stream().map(UserTypeResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de usuário", description = "Busca um tipo de usuário a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de usuário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserTypeResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<UserTypeResponse> findById(
            @Parameter(name = "id", description = "ID do tipo de usuário", example = "1")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(UserTypeResponse.from(service.findById(id)));
    }
}
