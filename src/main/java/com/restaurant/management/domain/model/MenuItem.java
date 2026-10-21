package com.restaurant.management.domain.model;

import java.util.Objects;

public class MenuItem {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Boolean disponivelNoLocal;
    private String fotoPath;
    private Restaurant restaurant;

    public MenuItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    public Boolean getDisponivelNoLocal() { return disponivelNoLocal; }
    public void setDisponivelNoLocal(Boolean disponivelNoLocal) { this.disponivelNoLocal = disponivelNoLocal; }
    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem that = (MenuItem) o;
        return Objects.equals(id, that.id)
                && Objects.equals(nome, that.nome)
                && Objects.equals(descricao, that.descricao)
                && Objects.equals(preco, that.preco)
                && Objects.equals(disponivelNoLocal, that.disponivelNoLocal)
                && Objects.equals(fotoPath, that.fotoPath)
                && Objects.equals(restaurant, that.restaurant);
    }

    @Override
    public int hashCode() { return Objects.hash(id, nome, descricao, preco, disponivelNoLocal, fotoPath, restaurant); }
}
