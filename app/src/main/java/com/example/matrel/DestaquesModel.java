package com.example.matrel;

public class DestaquesModel {
    String nome;
    String avaliacoes;
    String type;
    String img_url;

    public DestaquesModel() {
    }

    public DestaquesModel(String nome, String avaliacoes, String type, String img_url) {
        this.nome = nome;
        this.avaliacoes = avaliacoes;
        this.type = type;
        this.img_url = img_url;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(String avaliacoes) {
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
}
