package com.example.matrel;

public class DestaquesModel {
    String nome;
    Integer avaliacoes;
    String type;
    String img_url;
    Float preco;
    Boolean destaque;
    Boolean procurado;

    public DestaquesModel() {
    }


    public DestaquesModel(String nome, Integer avaliacoes, String type, String img_url, Float preco, Boolean destaque, Boolean procurado) {
        this.nome = nome;
        this.avaliacoes = avaliacoes;
        this.type = type;
        this.img_url = img_url;
        this.preco = preco;
        this.destaque = destaque;
        this.procurado = procurado;
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

    public void setPreco(float preco) {
        this.preco = preco;
    }
}