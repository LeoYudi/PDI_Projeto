package com.company;

import java.util.Arrays;

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
  
  public void fatiamento(int inicio, int fim, int valor, boolean total, int valor2) {
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        if (this.matriz[i][j] > inicio && this.matriz[i][j] < fim) {
          this.matriz[i][j] = valor;
        } else {
          if (total)
            this.matriz[i][j] = valor2;
        }
      }
    }
  }
  
  public void gama(float gama, float constante) {
    for (int i = 0; i < this.altura; i++)
      for (int j = 0; j < this.largura; j++)
        this.matriz[i][j] = (int) (this.maxval * constante * Math.pow(this.matriz[i][j] / (float) this.maxval, gama));
    
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
  
  public PGM laplace(float constante) {
    int[][] filtro = filtroLaplace();
    int[][] nova = new int[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        nova[i][j] = verificaValor(aplicaFiltroPixel(i, j, filtro, constante) + this.matriz[i][j]);
      }
    }
    return new PGM(this.tipo, this.largura, this.altura, this.maxval, nova);
  }
  
  public PGM outroFiltro(float constante) {
    int[][] filtro = outroFiltro();
    int[][] nova = new int[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        nova[i][j] = verificaValor(aplicaFiltroPixel(i, j, filtro, constante) + this.matriz[i][j]);
      }
    }
    return new PGM(this.tipo, this.largura, this.altura, this.maxval, nova);
  }
  
  public PGM filtroMedia(int n) {
    int[][] filtro = criaFiltroMedia(n);
    int[][] nova = new int[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        nova[i][j] = verificaValor(aplicaFiltroPixel(i, j, filtro, 1 / (float) (n * n)));
      }
    }
    return new PGM(this.tipo, this.largura, this.altura, this.maxval, nova);
  }
  
  public PGM filtroMediana(int valor) {
    int[][] nova = new int[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        nova[i][j] = aplicaMediana(valor, i, j);
      }
    }
    return new PGM(this.tipo, this.largura, this.altura, this.maxval, nova);
  }
  
  private int troca(int a, int b) {
    return a;
  }
  
  private int[][] filtroLaplace() {
    int[][] filtro = new int[3][3];
    filtro[0][0] = 0;
    filtro[0][1] = -1;
    filtro[0][2] = 0;
    filtro[1][0] = -1;
    filtro[1][1] = 4;
    filtro[1][2] = -1;
    filtro[2][0] = 0;
    filtro[2][1] = -1;
    filtro[2][2] = 0;
    return filtro;
  }
  
  private int[][] outroFiltro() {
    int[][] filtro = new int[3][3];
    filtro[0][0] = -1;
    filtro[0][1] = -1;
    filtro[0][2] = -1;
    filtro[1][0] = -1;
    filtro[1][1] = 8;
    filtro[1][2] = -1;
    filtro[2][0] = -1;
    filtro[2][1] = -1;
    filtro[2][2] = -1;
    return filtro;
  }
  
  private int[][] criaFiltroMedia(int n) {
    int[][] filtro = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        filtro[i][j] = 1;
      }
    }
    return filtro;
  }
  
  private int aplicaFiltroPixel(int altura, int largura, int[][] filtro, float constante) {
    float result = 0;
    int filtroI = 0, filtroJ;
    int aux = filtro.length / 2;
    for (int i = altura - aux; i <= altura + aux; i++) {
      filtroJ = 0;
      for (int j = largura - aux; j <= largura + aux; j++) {
        if (i >= 0 && i < this.altura && j >= 0 && j < this.largura)
          result += filtro[filtroI][filtroJ++] * this.matriz[i][j];
      }
      filtroI++;
    }
    result *= constante;
    return (int) result;
  }
  
  private int aplicaMediana(int valor, int altura, int largura) {
    int[] vetor = new int[valor * valor];
    int aux = valor / 2, cont = 0;
    for (int i = altura - aux; i < altura + aux; i++) {
      for (int j = largura - aux; j < largura + aux; j++) {
        if (i >= 0 && i < this.altura && j >= 0 && j < this.largura)
          vetor[cont++] = this.matriz[i][j];
        else
          vetor[cont++] = 0;
      }
    }
    Arrays.sort(vetor);
    return vetor[(valor * valor / 2) + 1];
  }
  
  public int verificaValor(int valor) {
    if (valor > this.maxval)
      return this.maxval;
    else return Math.max(valor, 0);
  }
  
  public int[][] soma(int[][] a, int[][] b, int max) {
    int[][] result = new int[a.length][a[0].length];
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[0].length; j++) {
        result[i][j] = a[i][j] + b[i][j];
        if (result[i][j] > max)
          result[i][j] = max;
      }
    }
    return result;
  }
  
  public int[][] sub(int[][] a, int[][] b) {
    int[][] result = new int[a.length][a[0].length];
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[0].length; j++) {
        result[i][j] = a[i][j] - b[i][j];
        if (result[i][j] < 0)
          result[i][j] = 0;
      }
    }
    return result;
  }
  
  public int[][] mult(int[][] a, int valor, int max) {
    int[][] result = new int[a.length][a[0].length];
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[0].length; j++) {
        result[i][j] = a[i][j] * valor;
        if (result[i][j] > max)
          result[i][j] = max;
      }
    }
    return result;
  }
}
