package com.restaurant.management.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "menu_items")
public class MenuItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private Double preco;
    private Boolean disponivelNoLocal;
    private String fotoPath;

    @ManyToOne
    private RestaurantEntity restaurant;

    public MenuItemEntity() {}

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
    public RestaurantEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItemEntity)) return false;
        MenuItemEntity that = (MenuItemEntity) o;
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
