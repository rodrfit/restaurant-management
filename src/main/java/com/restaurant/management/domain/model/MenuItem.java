package com.restaurant.management.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class MenuItem {

    private Long id;

    private String nome;

    private String descricao;

    private Double preco;

    private Boolean disponivelNoLocal;

    private String fotoPath;

    private Restaurant restaurant;
}
