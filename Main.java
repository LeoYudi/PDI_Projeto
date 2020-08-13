package com.company;

import java.io.IOException;
import java.util.Scanner;

import static com.company.ManipuladorArquivo.escritorPGM;
import static com.company.ManipuladorArquivo.escritorPPM;

public class Main {
  
  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    
    System.out.println("Digite o nome do arquivo: ");
    String path = scanner.next();
    if (path.contains(".pgm")) {
      PGM imagem = ManipuladorArquivo.leitorPGM(path);
      PGM result = new PGM(imagem.tipo, imagem.largura, imagem.altura, imagem.maxval, imagem.matriz);
      switch (operacaoPGM()) {
        case 1:
          System.out.println("\n Quanto gostaria de escurecer (numero>0)?");
          result.escurecer(scanner.nextInt());
          break;
        case 2:
          System.out.println("\n Quanto gostaria de clarear (numero>0)?");
          result.clarear(scanner.nextInt());
          break;
        case 3:
          result.negativo();
          break;
        case 4:
          result = result.girar90();
          break;
        case 5:
          result.flipHorizontal();
          break;
        case 6:
          int inicio, fim, valor, total;
          System.out.println("\nDigite o inicio do intervalo:");
          inicio = scanner.nextInt();
          System.out.println("\nDigite o fim do intervalo:");
          fim = scanner.nextInt();
          System.out.println("\nDigite o valor a ser recebido pelo pixel caso dentro do intervalo:");
          valor = scanner.nextInt();
          System.out.println("\nDigite se os pixels fora do intervalo vão ser mantidos ou alterados para preto (0 para manter e 1 para alterar):");
          total = scanner.nextInt();
          result.fatiamento(inicio, fim, valor, total != 0);
          break;
        case 7:
          float gama, constante;
          System.out.println("\n Digite o valor de gama:");
          gama = scanner.nextFloat();
          System.out.println("\n Digite o valor da constante:");
          constante = scanner.nextFloat();
          result.gama(gama, constante);
          break;
        case 8:
          result.equaliza();
          break;
        case 9:
          System.out.println("\n Digite o valor da constante: ");
          constante = scanner.nextFloat();
          result = result.laplace(constante);
          break;
        case 10:
          System.out.println("\n Digite o valor da constante: ");
          constante = scanner.nextFloat();
          result = result.outroFiltro(constante);
          break;
        case 11:
          int n;
          System.out.println("\n Digite o valor da dimensão do filtro: ");
          n = scanner.nextInt();
          result = result.filtroMedia(n, scanner);
          break;
      }
      System.out.println("\n Resultado foi salvo no arquivo 'result.pgm'");
      escritorPGM("result.pgm", result);
    } else if (path.contains(".ppm")) {
      PPM imagem = ManipuladorArquivo.leitorPPM(path);
      switch (operacaoPPM()) {
        case 1:
          PGM[] rgb;
          rgb = imagem.toPGM();
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
          imagem.toPPM(r, g, b);
          escritorPPM(path, imagem);
          System.out.println("\n Resultado foi salvo no arquivo '" + path + "'");
          break;
        
        case 3:
          PGM[] cmy;
          cmy = imagem.toCMY();
          escritorPGM("c.pgm", cmy[0]);
          escritorPGM("m.pgm", cmy[1]);
          escritorPGM("y.pgm", cmy[2]);
          System.out.println("\n Resultados foram salvos nos arquivos c.pgm, m.pgm e y.pgm");
          break;
      }
    }
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
    System.out.println("11 - Filtro Média\n");
    System.out.println("Qual operação gostaria de realizar com a imagem (1-11)? ");
    
    Scanner scanner = new Scanner(System.in);
    
    return scanner.nextInt();
  }
  
  public static int operacaoPPM() {
    System.out.println("1 - PPM para PGM");
    System.out.println("2 - PGM para PPM (certifique-se que os arquivos r.pgm, g.pgm e b.pgm já estão salvos na pasta raiz do projeto e setados com os valores desejados)");
    System.out.println("3 - RGB para CMY\n");
    System.out.println("Qual operação gostaria de realizar (1-3)?");
    Scanner scanner = new Scanner(System.in);
    
    return scanner.nextInt();
  }
  
  public static boolean verificaRGB(PGM r, PGM g, PGM b) {
    if (r.altura == g.altura && r.altura == b.altura) {
      if (r.largura == g.largura && r.largura == b.largura) {
        return r.maxval == g.maxval && r.maxval == b.maxval;
      }
    }
    return false;
  }
  
}
