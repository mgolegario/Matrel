package com.example.matrel;

public class DepartamentosModel {
    String nome;
    String img_url;

    public DepartamentosModel() {
    }

    public DepartamentosModel(String nome, String img_url) {
        this.nome = nome;
        this.img_url = img_url;
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
}
