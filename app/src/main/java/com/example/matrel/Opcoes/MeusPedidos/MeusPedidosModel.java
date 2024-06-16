package com.example.matrel.Opcoes.MeusPedidos;

public class MeusPedidosModel {
    String nome;
    String img_url;
    Float preco;
    Integer quantidade;

    public MeusPedidosModel() {
    }

    public MeusPedidosModel(String nome, String img_url, Float preco, Integer quantidade) {
        this.nome = nome;
        this.img_url = img_url;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}