package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ManipuladorArquivo {
  
  public static PGM leitorPGM(String path) throws IOException {
    BufferedReader buffRead = new BufferedReader(new FileReader(path));
    PGM imagem;
    String[] valores;
    ArrayList<String> linhas = new ArrayList<>();
    String linha;
    while (true) {
      linha = buffRead.readLine();
      if (linha != null) {
        valores = linha.split("\\s");
        if (!valores[0].equals("#")) {
          linhas.addAll(Arrays.asList(valores));
        }
      } else
        break;
    }
    buffRead.close();
    
    int numColunas, numLinhas, maxval;
    String tipo;
    tipo = linhas.get(0);
    numColunas = Integer.parseInt(linhas.get(1));
    numLinhas = Integer.parseInt(linhas.get(2));
    maxval = Integer.parseInt(linhas.get(3));
    int[][] result = new int[numLinhas][numColunas];
    int Linha = 0, coluna = 0;
    
    for (int i = 4; i < linhas.size(); i++) {
      if (coluna != numColunas)
        result[Linha][coluna++] = Integer.parseInt(linhas.get(i));
      else {
        coluna = 1;
        result[++Linha][0] = Integer.parseInt(linhas.get(i));
      }
    }
    
    imagem = new PGM(tipo, numColunas, numLinhas, maxval, result);
    return imagem;
  }
  
  public static void escritorPGM(String path, PGM imagem) throws IOException {
    ArrayList<String> lista = new ArrayList<>();
    
    lista.add(imagem.tipo);
    lista.add(imagem.largura + " " + imagem.altura);
    lista.add(Integer.toString(imagem.maxval));
    
    for (int i = 0; i < imagem.altura; i++)
      for (int j = 0; j < imagem.largura; j++)
        lista.add(Integer.toString(imagem.matriz[i][j]));
    
    BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
    String linha;
    for (String s : lista) {
      linha = s;
      buffWrite.append(linha).append("\n");
    }
    buffWrite.close();
  }
  
  public static PPM leitorPPM(String path) throws IOException {
    BufferedReader buffRead = new BufferedReader(new FileReader(path));
    ArrayList<String> lista;
    lista = new ArrayList<>();
    String linha;
    String[] coisas;
    while (true) {
      linha = buffRead.readLine();
      if (linha != null) {
        if (!linha.contains("#")) {
          coisas = linha.split("\\s");
          lista.addAll(Arrays.asList(coisas));
        }
      } else
        break;
    }
    
    String tipo = "";
    int largura = 0, altura = 0, maxval = 0;
    int[][][] valores = new int[0][0][0];
    if (lista.size() != 0) {
      tipo = lista.get(0);
      largura = Integer.parseInt(lista.get(1));
      altura = Integer.parseInt(lista.get(2));
      maxval = Integer.parseInt(lista.get(3));
      valores = new int[altura][largura][3];
      int linhas = 4;
//      for (int i = 4; i < lista.size(); i++) {
//        if (coluna != largura) {
//          valores[linhas][coluna][0] = Integer.parseInt(lista.get(i++));
//          valores[linhas][coluna][1] = Integer.parseInt(lista.get(i++));
//          valores[linhas][coluna++][2] = Integer.parseInt(lista.get(i++));
//        } else {
//          coluna = 0;
//          valores[++linhas][coluna][0] = Integer.parseInt(lista.get(i++));
//          valores[linhas][coluna][1] = Integer.parseInt(lista.get(i++));
//          valores[linhas][coluna++][2] = Integer.parseInt(lista.get(i++));
//        }
//      }
      for (int i = 0; i < altura; i++) {
        for (int j = 0; j < largura; j++) {
          valores[i][j][0] = Integer.parseInt(lista.get(linhas++));
          valores[i][j][1] = Integer.parseInt(lista.get(linhas++));
          valores[i][j][2] = Integer.parseInt(lista.get(linhas++));
        }
      }
    }
    return new PPM(tipo, largura, altura, maxval, valores);
  }
  
  public static void escritorPPM(String path, PPM imagem) throws IOException {
    ArrayList<String> lista = new ArrayList<>();
    
    lista.add(imagem.tipo);
    lista.add(imagem.largura + " " + imagem.altura);
    lista.add(Integer.toString(imagem.maxval));
    
    for (int i = 0; i < imagem.altura; i++) {
      for (int j = 0; j < imagem.largura; j++) {
        lista.add(Integer.toString(imagem.valores[i][j][0]));
        lista.add(Integer.toString(imagem.valores[i][j][1]));
        lista.add(Integer.toString(imagem.valores[i][j][2]));
      }
    }
    
    BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
    String linha;
    for (String s : lista) {
      linha = s;
      buffWrite.append(linha).append("\n");
    }
    buffWrite.close();
  }
  
  public static ArrayList<String> leitorOp(String path) throws IOException {
    BufferedReader buffRead = new BufferedReader(new FileReader(path));
    ArrayList<String> linhas = new ArrayList<>();
    String linha = "";
    while (true) {
      linha = buffRead.readLine();
      if (linha != null)
        linhas.add(linha);
      else
        break;
    }
    return linhas;
  }
}