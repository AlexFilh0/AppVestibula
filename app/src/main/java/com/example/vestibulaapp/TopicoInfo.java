package com.example.vestibulaapp;

public class TopicoInfo {
    public String nome;
    public int acertos;
    public int total;

    public TopicoInfo(String nome, int acertos, int total) {
        this.nome = nome;
        this.acertos = acertos;
        this.total = total;
    }

    @Override
    public String toString() {
        return nome; //+ " (" + acertos + "/" + total + " acertos)";
    }
}
