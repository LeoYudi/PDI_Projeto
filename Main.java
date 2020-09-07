package com.company;

import java.io.IOException;
import java.util.Scanner;

import static com.company.ManipuladorArquivo.escritorPGM;
import static com.company.ManipuladorArquivo.escritorPPM;

public class Main {
  
  static Scanner scanner = new Scanner(System.in);
  static PGM pgm;
  static PGM resultPGM;
  static PPM ppm;
  static PGM[] rgb;
  
  public static void main(String[] args) throws IOException {
    System.out.println("Digite o nome do arquivo: ");
    String path = scanner.next();
    if (path.contains(".pgm")) {
      pgm = ManipuladorArquivo.leitorPGM(path);
      resultPGM = new PGM(pgm.tipo, pgm.largura, pgm.altura, pgm.maxval, pgm.matriz);
      System.out.println("\nNão será possível realizar operações de arquivos PPM");
    } else if (path.contains(".ppm")) {
      ppm = ManipuladorArquivo.leitorPPM(path);
      System.out.println("\nPara realizar operações de arquivos PGM faça a conversão primeiro (número 1)");
    } else {
      System.err.println("\nArquivo precisa ser de extensão .ppm ou .pgm");
      return;
    }
    do {
      if (PGMouPPM()) {
        if (pgm == null && rgb == null) {
          System.err.println("\nPrimeiro converta para PGM nas operações de PPM");
        } else if (pgm != null) {
          switchPGM();
          System.out.println("\nResultado foi salvo no arquivo 'result.pgm'");
          escritorPGM("result.pgm", resultPGM);
        } else {
          for (int i = 0; i < rgb.length; i++) {
            switch (i) {
              case 0:
                System.out.println("\nOperação para cor vermelha:");
                switchPGM();
                System.out.println("\nResultado foi salvo no arquivo 'r.pgm'");
                escritorPGM("r.pgm", resultPGM);
                break;
              case 1:
                System.out.println("\nOperação para cor verde:");
                switchPGM();
                System.out.println("\nResultado foi salvo no arquivo 'g.pgm'");
                escritorPGM("g.pgm", resultPGM);
                break;
              case 2:
                System.out.println("\nOperação para cor azul:");
                switchPGM();
                System.out.println("\nResultado foi salvo no arquivo 'b.pgm'");
                escritorPGM("b.pgm", resultPGM);
                break;
            }
          }
        }
      } else {
        switchPPM(path);
      }
    } while (continua());
  }
  
  public static int operacaoPGM() {
    System.out.println("\n\n");
    System.out.println("1 - Escurecer");
    System.out.println("2 - Clarear");
    System.out.println("3 - Negativo");
    System.out.println("4 - Girar 90°");
    System.out.println("5 - Flip Horizontal");
    System.out.println("6 - Fatiamento de Níveis de Intensidade");
    System.out.println("7 - Transformação Gama");
    System.out.println("8 - Equalizar");
    System.out.println("9 - Filtro Laplaciano");
    System.out.println("10 - Outro filtro");
    System.out.println("11 - Filtro Média");
    System.out.println("12 - Filtro Mediana");
    System.out.println("13 - Filtro High Boost\n");
    System.out.println("Qual operação gostaria de realizar com a imagem (1-13)? ");
    
    Scanner scanner = new Scanner(System.in);
    
    return scanner.nextInt();
  }
  
  public static int operacaoPPM() {
    System.out.println("1 - PPM para PGM");
    System.out.println("2 - PGM para PPM (certifique-se que os arquivos r.pgm, g.pgm e b.pgm já estão salvos na pasta raiz do projeto e setados com os valores desejados)");
    System.out.println("3 - RGB para CMY");
    System.out.println("4 - RGB para HSI\n");
    System.out.println("Qual operação gostaria de realizar (1-4)?");
    Scanner scanner = new Scanner(System.in);
    
    return scanner.nextInt();
  }
  
  public static boolean continua() {
    System.out.println("\nGostaria de fazer outra operação (0-Não || 1-Sim)?");
    return scanner.nextInt() == 1;
  }
  
  public static boolean PGMouPPM() {
    System.out.println("\nOperação para arquivos 1-PGM ou 2-PPM?");
    return scanner.nextInt() == 1;
  }
  
  public static boolean verificaRGB(PGM r, PGM g, PGM b) {
    if (r.altura == g.altura && r.altura == b.altura) {
      if (r.largura == g.largura && r.largura == b.largura) {
        return r.maxval == g.maxval && r.maxval == b.maxval;
      }
    }
    return false;
  }
  
  public static void switchPGM() {
    switch (operacaoPGM()) {
      case 1:
        System.out.println("\n Quanto gostaria de escurecer (numero>0)?");
        resultPGM.escurecer(scanner.nextInt());
        break;
      case 2:
        System.out.println("\n Quanto gostaria de clarear (numero>0)?");
        resultPGM.clarear(scanner.nextInt());
        break;
      case 3:
        resultPGM.negativo();
        break;
      case 4:
        resultPGM = resultPGM.girar90();
        break;
      case 5:
        resultPGM.flipHorizontal();
        break;
      case 6:
        int inicio, fim, valor, total, valor2 = 0;
        System.out.println("\nDigite o inicio do intervalo:");
        inicio = scanner.nextInt();
        System.out.println("\nDigite o fim do intervalo:");
        fim = scanner.nextInt();
        System.out.println("\nDigite o valor a ser recebido pelo pixel caso dentro do intervalo:");
        valor = scanner.nextInt();
        System.out.println("\nDigite se os pixels fora do intervalo vão ser mantidos ou alterados (0 para manter e 1 para alterar):");
        total = scanner.nextInt();
        if (total == 0) {
          System.out.println("\nDigite o valor a se recebido pelo pixel caso fora do intervalo:");
          valor2 = scanner.nextInt();
        }
        resultPGM.fatiamento(inicio, fim, valor, total != 0, valor2);
        break;
      case 7:
        float gama, constante;
        System.out.println("\n Digite o valor de gama:");
        gama = scanner.nextFloat();
        System.out.println("\n Digite o valor da constante:");
        constante = scanner.nextFloat();
        resultPGM.gama(gama, constante);
        break;
      case 8:
        resultPGM.equaliza();
        break;
      case 9:
        System.out.println("\n Digite o valor da constante: ");
        constante = scanner.nextFloat();
        resultPGM = resultPGM.laplace(constante);
        break;
      case 10:
        System.out.println("\n Digite o valor da constante: ");
        constante = scanner.nextFloat();
        resultPGM = resultPGM.outroFiltro(constante);
        break;
      case 11:
        int n;
        System.out.println("\n Digite o valor da dimensão do filtro: ");
        n = scanner.nextInt();
        resultPGM = resultPGM.filtroMedia(n);
        break;
      case 12:
        System.out.println("\nDigite o valor da dimensão do filtro: ");
        valor = scanner.nextInt();
        resultPGM = resultPGM.filtroMediana(valor);
        break;
      case 13:
        System.out.println("\nDigite o valor da constante:");
        valor = scanner.nextInt();
        resultPGM = resultPGM.filtroMedia(3);
        resultPGM.matriz = pgm.sub(pgm.matriz, resultPGM.matriz);
        resultPGM.matriz = pgm.mult(resultPGM.matriz, valor, resultPGM.maxval);
        resultPGM.matriz = pgm.soma(resultPGM.matriz, pgm.matriz, resultPGM.maxval);
        break;
    }
  }
  
  public static void switchPPM(String path) throws IOException {
    switch (operacaoPPM()) {
      case 1:
        if (ppm == null) {
          System.out.println("\nNão é possível realizar esta operação");
          break;
        }
        rgb = ppm.toPGM();
        escritorPGM("r.pgm", rgb[0]);
        escritorPGM("g.pgm", rgb[1]);
        escritorPGM("b.pgm", rgb[2]);
        System.out.println("\n Resultado foi salvo nos arquivos 'r.ppm', 'g.ppm' e 'b.ppm'");
        break;
      
      case 2:
        PGM r = ManipuladorArquivo.leitorPGM("r.pgm");
        PGM g = ManipuladorArquivo.leitorPGM("g.pgm");
        PGM b = ManipuladorArquivo.leitorPGM("b.pgm");
        if (!verificaRGB(r, g, b)) {
          System.err.println("\n Arquivos com dimensões diferentes ou maxval diferente");
          break;
        }
        ppm.toPPM(r, g, b);
        escritorPPM(path, ppm);
        System.out.println("\n Resultado foi salvo no arquivo '" + path + "'");
        break;
      
      case 3:
        if (ppm == null) {
          System.out.println("\nNão é possível realizar esta operação");
          break;
        }
        PGM[] cmy;
        cmy = ppm.toCMY();
        escritorPGM("c.pgm", cmy[0]);
        escritorPGM("m.pgm", cmy[1]);
        escritorPGM("y.pgm", cmy[2]);
        System.out.println("\n Resultados foram salvos nos arquivos c.pgm, m.pgm e y.pgm");
        break;
      
      case 4:
        if (ppm == null) {
          System.out.println("\nNão é possível realizar esta operação");
          break;
        }
        PGM[] hsi;
        hsi = ppm.toHSI();
        escritorPGM("h.pgm", hsi[0]);
        escritorPGM("s.pgm", hsi[1]);
        escritorPGM("i.pgm", hsi[2]);
        System.out.println("\n Resultados foram salvos nos arquivos h.pgm, s.pgm e i.pgm");
        break;
    }
  }
}
