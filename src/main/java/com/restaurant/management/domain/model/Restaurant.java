package com.restaurant.management.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Restaurant {

    private Long id;

    private String nome;

    private String endereco;

    private String tipoCozinha;

    private String horarioFuncionamento;

    private User owner;
}
