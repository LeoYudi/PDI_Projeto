package com.company;

//classe que teoricamente era para ser utilizada para caso existisse um método que engloba PGM e PPM
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
