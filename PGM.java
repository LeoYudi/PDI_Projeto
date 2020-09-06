package com.company;

import java.util.Scanner;

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
        nova[i][j] = verificaValor(aplicaFiltroPixel(i, j, filtro, constante));
      }
    }
    return new PGM(this.tipo, this.largura, this.altura, this.maxval, nova);
  }
  
  public PGM filtroMedia(int n, Scanner scanner) {
    int[][] filtro = filtroMedia(n);
    int[][] nova = new int[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        nova[i][j] = verificaValor(aplicaFiltroPixel(i, j, filtro, 1 / (float) (n * n)));
      }
    }
    System.out.println("\nGostaria realizar um fatiamento (0 - Não || 1 - Sim)?");
    if (scanner.nextInt() == 1) {
      int inicio, fim, valor, total, valor2 = 0;
      System.out.println("\nDigite o inicio do intervalo:");
      inicio = scanner.nextInt();
      System.out.println("\nDigite o fim do intervalo:");
      fim = scanner.nextInt();
      System.out.println("\nDigite o valor a ser recebido pelo pixel caso dentro do intervalo:");
      valor = scanner.nextInt();
      System.out.println("\nDigite se os pixels fora do intervalo vão ser mantidos ou alterados para preto (0 para manter e 1 para alterar):");
      total = scanner.nextInt();
      if (total == 0) {
        System.out.println("\nDigite o valor a se recebido pelo pixel caso fora do intervalo:");
        valor2 = scanner.nextInt();
      }
      this.fatiamento(inicio, fim, valor, total != 0, valor2);
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
  
  private int[][] filtroMedia(int n) {
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
  
  public int verificaValor(int valor) {
    if (valor > this.maxval)
      return this.maxval;
    else return Math.max(valor, 0);
  }
}
