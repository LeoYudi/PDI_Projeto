package com.company;

public class Imagem {
  protected String tipo;
  protected int largura, altura, maxval;
  
  public Imagem(String tipo, int largura, int altura, int maxval) {
    this.tipo = tipo;
    this.largura = largura;
    this.altura = altura;
    this.maxval = maxval;
  }
}
