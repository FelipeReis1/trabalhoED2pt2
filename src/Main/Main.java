/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    static int numLimite = 20;
    static int numChaves, operacao;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
//        Leitura dos Dataset e Armazenamento em ArrayList para etapas 1 e 2 
        String caminhoCsvProcessado = "brazil_covid19_cities_processado.csv";
        String caminhoCsvCoordenadas = "brazil_cities_coordinates.csv";
        DadosCovid dadosCidade = new DadosCovid(caminhoCsvCoordenadas, 1);
        DadosCovid dadosCasos = new DadosCovid(caminhoCsvProcessado, 2);

        //        Etapa 4
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecione a operação desejada:\n");
        System.out.println("1 - Executar Trabalho; \n");
        System.out.println("2 - Inserção de cidades na quad tree; \n");
        System.out.println("3 - Inserção de registros na tabela hash; \n");
        System.out.println("4 - Inserção de chaves na árvore AVL; \n");
        System.out.println("5 - Inserção de chaves na árvore B; \n");

        operacao = scanner.nextInt();
        if (operacao != 1) {
            System.out.println("Digite o número de chaves: \n");
            numChaves = scanner.nextInt();
        }
        switch (operacao) {
            case 1:
//        Etapa 1
                QuadTree quadTree = new QuadTree();
                quadTree.carregamentoQuadTree(dadosCidade.getEntradasCidades());

//        Etapa 2
                TabelaHash tabelaHash = new TabelaHash();
                tabelaHash.carregamentoTabelaHash(dadosCasos.getEntradas());

//        Etapa 3
//                ArvoreAvl arvoreAvl = new ArvoreAvl();
//                ArvoreB arvoreB = new ArvoreB(20);
//                ArvoreB arvoreB200 = new ArvoreB(200);
//        Etapa 5
//            -------------TESTES------------------------------------------
//                arvoreAvl.carregamentoAvl(dadosCasos.getEntradas(), tabelaHash);
//                exibirResultadoArvoreB(arvoreB);
//                System.out.println("oi\n\n");
//                arvoreAvl.carregamentoAvl(dadosCasos.getEntradas(), tabelaHash);
//                System.out.println("Total casos da nessa Cidade é " + arvoreB.getTotalCasosCidade(313670, tabelaHash) );
//            -------------TESTES------------------------------------------
////                S1
                System.out.println("Digite o código da cidade:\n");
                int codCidade = scanner.nextInt();
//
////                S2
                double x0;
                double y0;
                double x1;
                double y1;

                System.out.println("Busca de Casos em Intervalo ");

                System.out.println("Digite Valor de Xo");
                x0 = Double.parseDouble(scanner.next());

                System.out.println("Digite Valor de Yo");
                y0 = Double.parseDouble(scanner.next());

                System.out.println("Digite Valor de X1");
                x1 = Double.parseDouble(scanner.next());

                System.out.println("Digite Valor de  Y1");
                y1 = Double.parseDouble(scanner.next());

                NoQuad pontoA = new NoQuad(x0, y0);
                NoQuad pontoB = new NoQuad(x1, y1);
                ArrayList<NoQuad> saida = quadTree.searchRange(pontoA, pontoB);

                int[] valores = new int[5];
                valores[0] = 10000;
                valores[1] = 50000;
                valores[2] = 100000;
                valores[3] = 500000;
                valores[4] = 1000000;

                String log = new String();

                System.out.println("Inserções árvore AVL");
                log += "Inserções árvore AVL\n\n";
                for (int n : valores) {
                    log += "Inserção de " + n + " valores\n\n";
                    double mediaTempoInsercao = 0;
                    double mediaTempoBuscas = 0;
                    double mediaComparacoes = 0;
                    double mediaTempoRegiao = 0;
                    for (int m = 0; m < 5; m++) {
                        ArvoreAvl arvoreAvlTeste = new ArvoreAvl();
                        double tempoBuscas, tempoInsercoes, tempoBuscasRegiao;

                        double startTimeIn = System.currentTimeMillis();
                        for (int id : tabelaHash.getIndicesAleatorios(n)) {
                            arvoreAvlTeste.inserir(id, tabelaHash.buscaHash(id).getCodCidade(), tabelaHash.buscaHash(id).getDtConfirmacao(), tabelaHash);
                        }
                        tempoInsercoes = System.currentTimeMillis() - startTimeIn;

                        log += "--- Rodada de Inserção " + m + "---\n";

                        double startTimeBu = System.currentTimeMillis();
                        log += "Busca da cidade " + codCidade + " na árvore AVL\n";
                        log += "Total casos da nessa Cidade é " + arvoreAvlTeste.getTotalCasosCidade(codCidade, tabelaHash) + "\n\n";
                        tempoBuscas = System.currentTimeMillis() - startTimeBu;

                        double startTimeS2 = System.currentTimeMillis();
                        log += "Busca de casos por região na árvore AVL\n";
                        int totalCasos = 0;
                        for (NoQuad no : saida) {
                            totalCasos += arvoreAvlTeste.getTotalCasosCidade(no.getCodigoCidade(), tabelaHash);
                        }
                        log += "Total casos da nessa Região é " + totalCasos + "\n\n";
                        tempoBuscasRegiao = System.currentTimeMillis() - startTimeS2;

                        log += "Tempo de Inserções: " + (float) tempoInsercoes + "ms\n";
                        log += "Tempo de Busca: " + (float) tempoBuscas + "ms\n";
                        log += "Tempo de Busca por região: " + (float) tempoBuscasRegiao + "ms\n";
                        log += "Número de Comparações: " + arvoreAvlTeste.getComparacoes() + "\n\n";
                        log += "\n";

                        mediaTempoInsercao += tempoInsercoes;
                        mediaTempoRegiao += tempoBuscasRegiao;
                        mediaTempoBuscas += tempoBuscas;
                        mediaComparacoes += arvoreAvlTeste.getComparacoes();

                    }
                    log += "Médias \n\n";

                    log += "Media de tempo de inserção " + mediaTempoInsercao / 5 + "\n";
                    log += "Media de tempo de busca " + mediaTempoBuscas / 5 + "\n";
                    log += "Media de tempo de busca por região " + mediaTempoRegiao / 5 + "\n";
                    log += "Media de comparações " + mediaComparacoes / 5 + "\n";
                    log += "\n\n";
                    log += "___________________________________________";
                    log += "\n\n";
                }
                System.out.println("Inserções árvore B - Ordem 20");
                log += "Inserções árvore B  - Ordem 20\n\n";
                for (int n : valores) {
                    log += "Inserção de " + n + " valores\n\n";
                    double mediaTempoInsercao = 0;
                    double mediaTempoBuscas = 0;
                    double mediaComparacoes = 0;
                    double mediaTempoRegiao = 0;
                    for (int m = 0; m < 5; m++) {
                        ArvoreB arvoreBTeste = new ArvoreB(20);
                        double tempoBuscas, tempoInsercoes, tempoBuscasRegiao;

                        double startTimeIn = System.currentTimeMillis();
                        for (int id : tabelaHash.getIndicesAleatorios(n)) {
                            arvoreBTeste.inserir(id, tabelaHash.buscaHash(id).getCodCidade(), tabelaHash.buscaHash(id).getDtConfirmacao(), tabelaHash);
                        }
                        tempoInsercoes = System.currentTimeMillis() - startTimeIn;

                        log += "--- Rodada de Inserção " + (m + 1) + "---\n";

                        double startTimeBu = System.currentTimeMillis();
                        log += "Busca da cidade " + codCidade + " na árvore B\n";
                        log += "Total casos da nessa Cidade é " + arvoreBTeste.getTotalCasosCidade(codCidade, tabelaHash) + "\n\n";
                        tempoBuscas = System.currentTimeMillis() - startTimeBu;

                        double startTimeS2 = System.currentTimeMillis();
                        log += "Busca de casos por região na árvore B\n";
                        int totalCasos = 0;
                        for (NoQuad no : saida) {
                            totalCasos += arvoreBTeste.getTotalCasosCidade(no.getCodigoCidade(), tabelaHash);
                        }
                        log += "Total casos da nessa Região é " + totalCasos + "\n\n";
                        tempoBuscasRegiao = System.currentTimeMillis() - startTimeS2;

                        log += "Tempo de Inserções: " + tempoInsercoes + "ms\n";
                        log += "Tempo de Busca: " + tempoBuscas + "ms\n";
                        log += "Tempo de Busca por região: " + tempoBuscasRegiao + "ms\n";
                        log += "Número de Comparações: " + arvoreBTeste.getComparacoes() + "\n\n";
                        log += "\n";

                        mediaTempoInsercao += tempoInsercoes;
                        mediaTempoRegiao += tempoBuscasRegiao;
                        mediaTempoBuscas += tempoBuscas;
                        mediaComparacoes += arvoreBTeste.getComparacoes();
                        log += "------------------------------------\n";

                    }
                    log += "Médias \n\n";

                    log += "Media de tempo de inserção " + mediaTempoInsercao / 5 + "\n";
                    log += "Media de tempo de busca " + mediaTempoBuscas / 5 + "\n";
                    log += "Media de tempo de busca por região " + mediaTempoRegiao / 5 + "\n";
                    log += "Media de comparações " + mediaComparacoes / 5 + "\n";
                    log += "\n\n";
                    log += "___________________________________________";
                    log += "\n\n";
                }

                System.out.println("Inserções árvore B - Ordem 200");
                log += "Inserções árvore B  - Ordem 200\n\n";
                for (int n : valores) {
                    log += "Inserção de " + n + " valores\n\n";
                    double mediaTempoInsercao = 0;
                    double mediaTempoBuscas = 0;
                    double mediaComparacoes = 0;
                    double mediaTempoRegiao = 0;
                    for (int m = 0; m < 5; m++) {
                        ArvoreB arvoreBTeste = new ArvoreB(200);
                        double tempoBuscas, tempoInsercoes, tempoBuscasRegiao;

                        double startTimeIn = System.currentTimeMillis();
                        for (int id : tabelaHash.getIndicesAleatorios(n)) {
                            arvoreBTeste.inserir(id, tabelaHash.buscaHash(id).getCodCidade(), tabelaHash.buscaHash(id).getDtConfirmacao(), tabelaHash);
                        }
                        tempoInsercoes = System.currentTimeMillis() - startTimeIn;

                        log += "--- Rodada de Inserção " + (m + 1) + "---\n";

                        double startTimeBu = System.currentTimeMillis();
                        log += "Busca da cidade " + codCidade + " na árvore B\n";
                        log += "Total casos da nessa Cidade é " + arvoreBTeste.getTotalCasosCidade(codCidade, tabelaHash) + "\n\n";
                        tempoBuscas = System.currentTimeMillis() - startTimeBu;

                        double startTimeS2 = System.currentTimeMillis();
                        log += "Busca de casos por região na árvore B\n";
                        int totalCasos = 0;
                        for (NoQuad no : saida) {
                            totalCasos += arvoreBTeste.getTotalCasosCidade(no.getCodigoCidade(), tabelaHash);
                        }
                        log += "Total casos da nessa Região é " + totalCasos + "\n\n";
                        tempoBuscasRegiao = System.currentTimeMillis() - startTimeS2;

                        log += "Tempo de Inserções: " + tempoInsercoes + "ms\n";
                        log += "Tempo de Busca: " + tempoBuscas + "ms\n";
                        log += "Tempo de Busca por região: " + tempoBuscasRegiao + "ms\n";
                        log += "Número de Comparações: " + arvoreBTeste.getComparacoes() + "\n\n";
                        log += "\n";

                        mediaTempoInsercao += tempoInsercoes;
                        mediaTempoRegiao += tempoBuscasRegiao;
                        mediaTempoBuscas += tempoBuscas;
                        mediaComparacoes += arvoreBTeste.getComparacoes();
                        log += "------------------------------------\n";

                    }
                    log += "Médias \n\n";

                    log += "Media de tempo de inserção " + mediaTempoInsercao / 5 + "\n";
                    log += "Media de tempo de busca " + mediaTempoBuscas / 5 + "\n";
                    log += "Media de tempo de busca por região " + mediaTempoRegiao / 5 + "\n";
                    log += "Media de comparações " + mediaComparacoes / 5 + "\n";
                    log += "\n\n";
                    log += "___________________________________________";
                    log += "\n\n";
                }
                salvarArquivo(log);
                break;

            case 2:
                ArrayList<DadosCovid.EntradaCidades> dadosQuad = dadosCidade.getEntradasCidades();
                Collections.shuffle(dadosQuad);
                ArrayList<DadosCovid.EntradaCidades> amostras = new ArrayList<DadosCovid.EntradaCidades>();
                for (int i = 0; i < numChaves; i++) {
                    amostras.add(dadosQuad.get(i));
                }

                QuadTree quadTeste = new QuadTree();
                quadTeste.carregamentoQuadTree(amostras);

                exibirResultadoQuadTree(quadTeste);

                System.out.println("\n\n");
                break;
            case 3:
                ArrayList<DadosCovid.Entrada> dadosHash = dadosCasos.getEntradas();

                Collections.shuffle(dadosHash);
                ArrayList<DadosCovid.Entrada> amostrasCasos = new ArrayList<DadosCovid.Entrada>();
                for (int i = 0; i < numChaves; i++) {
                    amostrasCasos.add(dadosHash.get(i));
                }

                TabelaHash tabelaHashTeste = new TabelaHash(numChaves);
                tabelaHashTeste.carregamentoTabelaHash(amostrasCasos);

                exibirResultadoTabelaHash(tabelaHashTeste);
                System.out.println("\n\n");
                break;

            case 4:
                ArvoreAvl arvoreAvlTeste = new ArvoreAvl();
                TabelaHash tabelaAux = new TabelaHash(numChaves);

                ArrayList<DadosCovid.Entrada> entradasAvl = dadosCasos.getEntradas();

                Collections.shuffle(entradasAvl);
                ArrayList<DadosCovid.Entrada> amostrasAvl = new ArrayList<DadosCovid.Entrada>();
                for (int i = 0; i < numChaves; i++) {
                    amostrasAvl.add(entradasAvl.get(i));
                }
                tabelaAux.carregamentoTabelaHash(amostrasAvl);
                arvoreAvlTeste.carregamentoAvl(amostrasAvl, tabelaAux);

                exibirResultadoArvoreAvl(arvoreAvlTeste);
                System.out.println("\n\n");
                break;
            case 5:
                ArvoreB arvoreBTeste = new ArvoreB(4);
                TabelaHash tabelaAuxB = new TabelaHash(numChaves);

                ArrayList<DadosCovid.Entrada> entradasB = dadosCasos.getEntradas();

                Collections.shuffle(entradasB);
                ArrayList<DadosCovid.Entrada> amostrasB = new ArrayList<DadosCovid.Entrada>();
                for (int i = 0; i < numChaves; i++) {
                    amostrasB.add(entradasB.get(i));
                }
                tabelaAuxB.carregamentoTabelaHash(amostrasB);
                arvoreBTeste.carregamentoB(amostrasB, tabelaAuxB);
                exibirResultadoArvoreB(arvoreBTeste);
                System.out.println("\n\n");

                break;
            default:
                System.out.println("Operação Não Existe!");
                break;

        }

    }

    public static void exibirResultadoQuadTree(QuadTree quad) throws IOException {
        if (numChaves <= numLimite) {
            quad.showQuad();
        } else {
            quad.Save("SaidaQuadTree.txt");
        }
    }

    public static void exibirResultadoTabelaHash(TabelaHash tabela) throws IOException {
        if (numChaves <= numLimite) {
            tabela.show();
        } else {
            tabela.Save("SaidaTabelaHash.txt");
        }
    }

    public static void exibirResultadoArvoreAvl(ArvoreAvl avl) throws IOException {
        if (numChaves <= numLimite) {
            avl.show();
        } else {
            avl.Save("SaidaArvoreAvl.txt");
        }
    }

    public static void exibirResultadoArvoreB(ArvoreB b) throws IOException {
        if (numChaves <= numLimite) {
            b.show();
        } else {
            b.Save("SaidaArvoreB.txt");
        }
    }

    public static void salvarArquivo(String saida) throws IOException {
        File file = new File("Saida.txt");
        file.createNewFile();

        FileWriter fw = new FileWriter(file);

        fw.write("Resultado dos experimentos\n\n");

        fw.write(saida);

        fw.flush();
        fw.close();
    }

}
