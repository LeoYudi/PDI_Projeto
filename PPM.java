package com.company;

public class PPM extends Imagem {
  int[][][] valores;
  
  public PPM(String tipo, int altura, int largura, int maxval, int[][][] valores) {
    super(tipo, altura, largura, maxval);
    this.valores = valores;
  }
  
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
    imagens[0] = new PGM("P2", this.altura, this.largura, this.maxval, r);
    imagens[1] = new PGM("P2", this.altura, this.largura, this.maxval, g);
    imagens[2] = new PGM("P2", this.altura, this.largura, this.maxval, b);
    return imagens;
  }
  
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
}
