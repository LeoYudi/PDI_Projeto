package com.company;

public class PPM extends Imagem {
  int[][][] valores;
  
  public PPM(String tipo, int altura, int largura, int maxval, int[][][] valores) {
    super(tipo, altura, largura, maxval);
    this.valores = valores;
  }
  
  //converte uma imagem PPM em um vetor de PGMs, uma posição para cada canal
  public PGM[] toPGM() {
    PGM[] imagens = new PGM[3];
    int[][] r, g, b;
    r = new int[this.altura][this.largura];
    g = new int[this.altura][this.largura];
    b = new int[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        r[i][j] = this.valores[i][j][0];
        g[i][j] = this.valores[i][j][1];
        b[i][j] = this.valores[i][j][2];
      }
    }
    imagens[0] = new PGM("P2", this.largura, this.altura, this.maxval, r);
    imagens[1] = new PGM("P2", this.largura, this.altura, this.maxval, g);
    imagens[2] = new PGM("P2", this.largura, this.altura, this.maxval, b);
    return imagens;
  }
  
  //converte 3 imagens PGM para uma imagem PPM
  public void toPPM(PGM r, PGM g, PGM b) {
    this.tipo = "P3";
    this.maxval = r.maxval;
    this.largura = r.largura;
    this.altura = r.altura;
    this.valores = new int[this.altura][this.largura][3];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        this.valores[i][j][0] = r.matriz[i][j];
        this.valores[i][j][1] = g.matriz[i][j];
        this.valores[i][j][2] = b.matriz[i][j];
      }
    }
  }
  
  //converte uma imagem PPM em um vetor de imagens PGM(CMY)
  public PGM[] toCMY() {
    PGM[] imagens;
    imagens = this.toPGM();
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        imagens[0].matriz[i][j] = this.maxval - imagens[0].matriz[i][j];
        imagens[1].matriz[i][j] = this.maxval - imagens[1].matriz[i][j];
        imagens[2].matriz[i][j] = this.maxval - imagens[2].matriz[i][j];
      }
    }
    return imagens;
  }
  
  //converte uma imagem PPM em um vetor de imagens PGM(HSI)
  public PGM[] toHSI() {
    PGM[] imagens;
    imagens = this.toPGM();
    double[][] r, g, b;
    r = new double[this.altura][this.largura];
    g = new double[this.altura][this.largura];
    b = new double[this.altura][this.largura];
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        r[i][j] = imagens[0].matriz[i][j] / (double) (imagens[0].matriz[i][j] + imagens[1].matriz[i][j] + imagens[2].matriz[i][j]);
        g[i][j] = imagens[1].matriz[i][j] / (double) (imagens[0].matriz[i][j] + imagens[1].matriz[i][j] + imagens[2].matriz[i][j]);
        b[i][j] = imagens[2].matriz[i][j] / (double) (imagens[0].matriz[i][j] + imagens[1].matriz[i][j] + imagens[2].matriz[i][j]);
      }
    }
    double h, s, i;
    int[][] resultH, resultS, resultI;
    resultH = new int[this.altura][this.largura];
    resultS = new int[this.altura][this.largura];
    resultI = new int[this.altura][this.largura];
    for (int linha = 0; linha < this.altura; linha++) {
      for (int coluna = 0; coluna < this.largura; coluna++) {
        h = Math.acos((0.5 * (2 * r[linha][coluna] - g[linha][coluna] - b[linha][coluna])) / Math.sqrt(Math.pow(r[linha][coluna] - g[linha][coluna], 2) + ((r[linha][coluna] - b[linha][coluna]) * (g[linha][coluna] - b[linha][coluna]))));
        if (b[linha][coluna] > g[linha][coluna])
          h = (2 * Math.PI) - h;
        s = 1 - 3 * min(r[linha][coluna], g[linha][coluna], b[linha][coluna]);
        i = (imagens[0].matriz[linha][coluna] + imagens[1].matriz[linha][coluna] + imagens[2].matriz[linha][coluna]) / 3.0;
        resultH[linha][coluna] = (int) (h * this.maxval / (2 * Math.PI));
        resultS[linha][coluna] = (int) (s * this.maxval);
        resultI[linha][coluna] = (int) i;
      }
    }
    imagens = new PGM[3];
    imagens[0] = new PGM("P2", this.altura, this.largura, this.maxval, resultH);
    imagens[1] = new PGM("P2", this.altura, this.largura, this.maxval, resultS);
    imagens[2] = new PGM("P2", this.altura, this.largura, this.maxval, resultI);
    return imagens;
  }
  
  //função utilizada para encontrar o valor minimo dentre 3 valores
  public double min(double r, double g, double b) {
    if (r < g) {
      return Math.min(r, b);
    } else return Math.min(g, b);
  }
}
