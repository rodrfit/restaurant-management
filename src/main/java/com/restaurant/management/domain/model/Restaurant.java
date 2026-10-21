package com.restaurant.management.domain.model;

import java.util.Objects;

public class Restaurant {
    private Long id;
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    private User owner;

    public Restaurant() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id)
                && Objects.equals(nome, that.nome)
                && Objects.equals(endereco, that.endereco)
                && Objects.equals(tipoCozinha, that.tipoCozinha)
                && Objects.equals(horarioFuncionamento, that.horarioFuncionamento)
                && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() { return Objects.hash(id, nome, endereco, tipoCozinha, horarioFuncionamento, owner); }
}
