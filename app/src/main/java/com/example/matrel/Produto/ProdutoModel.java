package com.example.matrel.Produto;

public class ProdutoModel {
    String nome;
    Integer avaliacoes;
    String type;
    String img_url;
    String descricao;
    Float preco;
    Boolean destaque;
    Boolean procurado;

    public ProdutoModel() {
    }

    public ProdutoModel(String nome, Integer avaliacoes, String type, String img_url, Float preco, Boolean destaque, Boolean procurado, String descricao) {
        this.nome = nome;
        this.avaliacoes = avaliacoes;
        this.type = type;
        this.img_url = img_url;
        this.preco = preco;
        this.destaque = destaque;
        this.procurado = procurado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(Integer avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean destaque) {
        this.destaque = destaque;
    }

    public Boolean getProcurado() {
        return procurado;
    }

    public void setProcurado(Boolean procurado) {
        this.procurado = procurado;
    }
}
