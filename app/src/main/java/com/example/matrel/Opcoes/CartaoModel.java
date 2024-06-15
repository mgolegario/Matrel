package com.example.matrel.Opcoes;

import java.util.Date;

public class CartaoModel {
    Integer numCartao;
    Integer segCod;
    String dataVal;
    String nomeTitular;

    public CartaoModel() {
    }

    public CartaoModel(Integer numCartao, Integer segCod, String dataVal, String nomeTitular) {
        this.numCartao = numCartao;
        this.segCod = segCod;
        this.dataVal = dataVal;
        this.nomeTitular = nomeTitular;
    }

    public Integer getNumCartao() {
        return numCartao;
    }

    public void setNumCartao(Integer numCartao) {
        this.numCartao = numCartao;
    }

    public Integer getSegCod() {
        return segCod;
    }

    public void setSegCod(Integer segCod) {
        this.segCod = segCod;
    }

    public String getDataVal() {
        return dataVal;
    }

    public void setDataVal(String dataVal) {
        this.dataVal = dataVal;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }
}
