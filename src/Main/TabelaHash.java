/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class TabelaHash {

    private float colisoes;
    private int tamTabela;
    protected NoHash[] tabela, tabelaAux;
    private int tamMaximo = 4096;
    private ArrayList<Integer> indices = new ArrayList();

    public TabelaHash(double tamanho) {
        while (tamMaximo < tamanho) {
            tamMaximo *= 2;
        }
        int aux = (int) Math.round(tamanho * 1.5);
        Primos primo = new Primos();
        primo.sieve((int) (tamMaximo));

        for (int i = aux; i < tamMaximo; i++) {
            if (primo.prime[i]) {
                this.tamTabela = i;
                break;
            }
        }
        this.colisoes = 0;
        this.tabela = new NoHash[this.tamTabela];
        this.tabelaAux = new NoHash[this.tamTabela];
    }

    public TabelaHash() {
        this.colisoes = 0;
        this.tamTabela = 1860941;
        this.tabela = new NoHash[this.tamTabela];
        this.tabelaAux = new NoHash[this.tamTabela];
    }

    public void carregamentoTabelaHash(ArrayList<DadosCovid.Entrada> entradasHash) throws IOException, ParseException {
        for (DadosCovid.Entrada entrada : entradasHash) {
            NoHash no = new NoHash();
            no.setDtConfirmacao(entrada.getDtConfirmacao());
            no.setEstado(entrada.getEstado());
            no.setCidade(entrada.getCidade());
            no.setCodCidade(entrada.getCodCidade());
            no.setNumeroCasos(entrada.getNumeroCasos());
            no.setNumeroCasosDiario(entrada.getNumeroCasosDiario());
            no.setNumeroObitos(entrada.getNumeroObitos());
            this.inserir(no);
        }
//        System.out.println(colisoes / tamTabela);
    }

    public int[] getIndicesAleatorios(int numReg) {
        int[] arr = new int[numReg];
        ArrayList<Integer> indiceAux = indices;
        Collections.shuffle(indiceAux);
        for (int i = 0; i < numReg; i++) {
            arr[i] = indiceAux.get(i);
        }
        return arr;
    }

    public void show() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                System.out.print("Indice " + i + ": ");
                System.out.print(formato.format(tabela[i].getDtConfirmacao()) + " ");
                System.out.print(tabela[i].getEstado() + " ");
                System.out.print(tabela[i].getCidade() + " ");
                System.out.print(tabela[i].getCodCidade() + " ");
                System.out.print(tabela[i].getNumeroCasos() + " ");
                System.out.print(tabela[i].getNumeroCasosDiario() + " ");
                System.out.print(tabela[i].getNumeroObitos() + " ");
                System.out.print("\n");
            }
        }
    }

    public void Save(String file_name) throws IOException {
        File file = new File(file_name);
        file.createNewFile();

        FileWriter fw = new FileWriter(file);

        fw.write("Saída Tabela Hash\n");
        String row = new String();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                row += formato.format(tabela[i].getDtConfirmacao()) + " ";
                row += tabela[i].getEstado() + " ";
                row += tabela[i].getCidade() + " ";
                row += tabela[i].getCodCidade() + " ";
                row += tabela[i].getNumeroCasos() + " ";
                row += tabela[i].getNumeroCasosDiario() + " ";
                row += tabela[i].getNumeroObitos() + "\n";
            }
        }

        fw.write(row);

        fw.flush();
        fw.close();
    }

    public NoHash buscaHash(int chave) throws ParseException {
        return this.tabela[chave];
    }

    private void inserir(NoHash no) throws ParseException {

        int id = hash1(no) % this.tamTabela;
        if (this.tabela[id] == null) {
            this.tabela[id] = no;

        } else {
            int i = 1;
            id = (hash1(no) + i * i) % this.tamTabela;
            while (this.tabela[id] != null) {
                colisoes++;
                i++;
                id = (hash1(no) + i * i) % this.tamTabela;
            }
            this.tabela[id] = no;
        }
        indices.add(id);
    }

    public int getHash(NoHash no) throws ParseException {
        int id = hash1(no) % this.tamTabela;
        if (this.tabelaAux[id] == null) {
            this.tabelaAux[id] = no;

        } else {
            int i = 1;
            id = (hash1(no) + i * i) % this.tamTabela;
            while (this.tabelaAux[id] != null) {
                colisoes++;
                i++;
                id = (hash1(no) + i * i) % this.tamTabela;
            }
            this.tabelaAux[id] = no;

        }
        return id;
    }

    private int hash1(NoHash no) throws ParseException {
        int indice;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String[] data = formato.format(no.getDtConfirmacao()).split("-");
        indice = (no.getCodCidade() * (Integer.parseInt(data[2]) + Integer.parseInt(data[1]) + Integer.parseInt(data[0]))) % this.tamTabela;
        return indice;
    }

    private int hash2(NoHash no) throws ParseException {
        int indice;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String[] data = formato.format(no.getDtConfirmacao()).split("-");
        indice = (Integer.parseInt(data[2]) + Integer.parseInt(data[1]) + Integer.parseInt(data[0]));
        return indice;
    }

//    private void calcTamanho() throws IOException {
//        int i = 0;
//        try (BufferedReader br = new BufferedReader(new FileReader(this.dataset))) {
//            String row;
//            br.readLine();
//            while ((row = br.readLine()) != null) {
//                i++;
//            }
//            this.tamTabela = (int) (i * 1.3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
