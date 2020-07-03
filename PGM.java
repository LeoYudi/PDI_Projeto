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
        this.matriz[i][j] = Math.max(aux, 0);
      }
    }
  }
  
  public void clarear(int valor) {
    int aux;
    for (int i = 0; i < this.matriz.length; i++) {
      for (int j = 0; j < this.matriz[0].length; j++) {
        aux = this.matriz[i][j] + valor;
        this.matriz[i][j] = Math.min(aux, this.maxval);
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
  
  public void gama(float gama, float constante) {
    double result;
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++)
        this.matriz[i][j] = (int) (this.maxval * constante * Math.pow(this.matriz[i][j] / (float) this.maxval, gama));
      
    }
  }
  
  public void equaliza() {
    float[] nivel = new float[maxval + 1];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++)
        nivel[this.matriz[i][j]]++;
    } //pega quantidade de cada nivel na imagem
    
    for (int i = 0; i < nivel.length; i++) {
      nivel[i] = nivel[i] / (float) (this.largura * this.altura);
    } //altera a quantidade para probabilidade
    
    int[] result = new int[nivel.length];
    double soma = 0.0;
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j <= i; j++)
        soma += nivel[j];
      
      result[i] = (int) (soma * this.maxval);
      soma = 0;
    } //em result vai ter o novo valor para cada nivel de cinza
    
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++)
        this.matriz[i][j] = result[this.matriz[i][j]];
      
    }
  }
  
  private int troca(int a, int b) {
    return a;
  }
}
