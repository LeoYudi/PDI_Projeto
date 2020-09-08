package com.company;

import java.util.Arrays;

public class PGM extends Imagem {
  int[][] matriz;
  
  public PGM(String tipo, int largura, int altura, int maxval, int[][] matriz) {
    super(tipo, largura, altura, maxval);
    this.matriz = matriz;
  }
  
  //escurece a imagem / subtrai um valor espeficado
  public void escurecer(int valor) {
    int aux;
    for (int i = 0; i < this.matriz.length; i++) {
      for (int j = 0; j < this.matriz[0].length; j++) {
        aux = this.matriz[i][j] - valor;
        this.matriz[i][j] = Math.max(aux, 0);
      }
    }
  }
  
  //clareia uma imagem / soma um valor especificado
  public void clarear(int valor) {
    int aux;
    for (int i = 0; i < this.matriz.length; i++) {
      for (int j = 0; j < this.matriz[0].length; j++) {
        aux = this.matriz[i][j] + valor;
        this.matriz[i][j] = Math.min(aux, this.maxval);
      }
    }
  }
  
  //aplica efeito negativo na imagem
  public void negativo() {
    for (int i = 0; i < this.matriz.length; i++)
      for (int j = 0; j < this.matriz[0].length; j++)
        this.matriz[i][j] = this.maxval - this.matriz[i][j];
  }
  
  //rotaciona a imagem em 90 graus
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
  
  //efeito de espelhamento da imagem
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
  
  //aplica fatiamento na imagem com tratamento de alteração fora do intervalo ou não
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
  
  //aplica tranformação gama na imagem
  public void gama(float gama, float constante) {
    for (int i = 0; i < this.altura; i++)
      for (int j = 0; j < this.largura; j++)
        this.matriz[i][j] = (int) (this.maxval * constante * Math.pow(this.matriz[i][j] / (float) this.maxval, gama));
    
  }
  
  //equaliza a imagem
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
  
  //aplica filtro laplaciano na imagem
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
  
  //aplica filtro especificado em exercicio da aula
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
  
  //aplica filtro media
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
  
  //aplica filtro mediana
  public PGM filtroMediana(int valor) {
    int[][] nova = new int[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        nova[i][j] = aplicaMediana(valor, i, j);
      }
    }
    return new PGM(this.tipo, this.largura, this.altura, this.maxval, nova);
  }
  
  //função utilizada para realizar a troca de duas variaveis
  private int troca(int a, int b) {
    return a;
  }
  
  //função para criar o filtro laplaciano
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
  
  //função para criar o filtro especificado na aula
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
  
  //função para criar o filtro media
  private int[][] criaFiltroMedia(int n) {
    int[][] filtro = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        filtro[i][j] = 1;
      }
    }
    return filtro;
  }
  
  //função geral para aplicar um filtro em um pixel, retornando seu resultado
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
  
  //função que aplica o filtro mediana no pixel, retornando seu resultado
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
  
  //função de verificação dos limites de um valor da imagem
  public int verificaValor(int valor) {
    if (valor > this.maxval)
      return this.maxval;
    else return Math.max(valor, 0);
  }
  
  // função que soma duas imagens
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
  
  //funçao que subtrai duas imagens
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
  
  //função que realiza a multiplicação de um valor na imagem
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
