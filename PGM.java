package com.company;

public class PGM extends Imagem {
  int[][] matriz;
  
  public PGM(String tipo, int largura, int altura, int maxval, int[][] matriz) {
    super(tipo, largura, altura, maxval);
    this.matriz = matriz;
  }
  
  public void escurecer(int valor) {
    int aux;
    for (int i = 0; i < this.matriz.length; i++) {
      for (int j = 0; j < this.matriz[0].length; j++) {
        aux = this.matriz[i][j] - valor;
        if (aux < 0)
          this.matriz[i][j] = 0;
        else
          this.matriz[i][j] = aux;
      }
    }
  }
  
  public void clarear(int valor) {
    int aux;
    for (int i = 0; i < this.matriz.length; i++) {
      for (int j = 0; j < this.matriz[0].length; j++) {
        aux = this.matriz[i][j] + valor;
        if (aux > this.maxval)
          this.matriz[i][j] = maxval;
        else
          this.matriz[i][j] = aux;
      }
    }
  }
  
  public void negativo() {
    for (int i = 0; i < this.matriz.length; i++)
      for (int j = 0; j < this.matriz[0].length; j++)
        this.matriz[i][j] = this.maxval - this.matriz[i][j];
  }
  
  public PGM girar90() {
    int largura, altura;
    int[][] matriz;
    largura = this.altura;
    altura = this.largura;
    matriz = new int[altura][largura];
    for (int i = 0; i < this.matriz.length; i++)
      for (int j = 0; j < this.matriz[0].length; j++)
        matriz[j][largura - i - 1] = this.matriz[i][j];
    
    PGM imagem;
    imagem = new PGM(this.tipo, largura, altura, this.maxval, matriz);
    return imagem;
  }
  
  public void flipHorizontal() {
    int inicio = 0, fim = matriz[0].length - 1;
    for (int i = 0; i < this.matriz.length; i++) {
      while (inicio < fim) {
        this.matriz[i][inicio] = troca(this.matriz[i][fim], this.matriz[i][fim] = this.matriz[i][inicio]);
        inicio++;
        fim--;
      }
      inicio = 0;
      fim = matriz[0].length - 1;
    }
  }
  
  public void fatiamento(int inicio, int fim, int valor, boolean total) {
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        if (this.matriz[i][j] > inicio && this.matriz[i][j] < fim) {
          this.matriz[i][j] = valor;
        } else {
          if (total)
            this.matriz[i][j] = 0;
        }
      }
    }
  }
  
  public void gama(float gama) {
    double result;
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++)
        this.matriz[i][j] = (int) (this.maxval * Math.pow(this.matriz[i][j] / (float) this.maxval, gama));
      
    }
  }
  
  private int troca(int a, int b) {
    return a;
  }
}
