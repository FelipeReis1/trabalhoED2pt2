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
import java.util.Date;
import javax.xml.transform.Source;

public class ArvoreB {

    int grau_min = 2;
    int n = 0;
    boolean folha = true;
    ArvoreB raiz;
    ArvoreB[] arrayFilhos;
    NoB[] arrayChaves;
    static String saida = new String();
    static long comparacoes = 0;

    public ArvoreB(int grau_min) {
        this.grau_min = grau_min;
        this.raiz = null;
        this.arrayChaves = new NoB[2 * this.grau_min - 1];
        this.arrayFilhos = new ArvoreB[2 * this.grau_min];
        for (int i = 0; i < 2 * this.grau_min - 1; i++) {
            this.arrayChaves[i] = null;
        }
        for (int i = 0; i < 2 * this.grau_min; i++) {
            this.arrayFilhos[i] = null;
        }
    }

    public void split(ArvoreB raiz, int i) {
        ArvoreB y = raiz.arrayFilhos[i], z = new ArvoreB(grau_min);
        z.folha = y.folha;
        z.n = grau_min - 1;

        for (int j = 0; j < (grau_min - 1); j++) {
            z.arrayChaves[j] = y.arrayChaves[j + grau_min];
        }

        if (!y.folha) {
            for (int j = 0; j < grau_min; j++) {
                z.arrayFilhos[j] = y.arrayFilhos[j + grau_min];
            }
        }

        y.n = grau_min - 1;
        for (int j = raiz.n; j >= i + 1; j--) {
            raiz.arrayFilhos[j + 1] = raiz.arrayFilhos[j];
        }

        raiz.arrayFilhos[i + 1] = z;
        for (int j = raiz.n - 1; j >= i; j--) {
            raiz.arrayChaves[j + 1] = raiz.arrayChaves[j];
        }

        raiz.arrayChaves[i] = y.arrayChaves[grau_min - 1];
        raiz.n++;
    }

    public void inserir(int chave, int codCidade, Date data, TabelaHash tabela) throws ParseException {
        NoB novo_no = new NoB(chave, codCidade, data);
        this.inserir(novo_no, tabela);
    }

    public void inserir(NoB novo_no, TabelaHash tabela) throws ParseException {
        if (this.raiz == null) {
            this.raiz = new ArvoreB(grau_min);
            this.raiz.folha = true;
            this.raiz.arrayChaves[0] = novo_no;
            this.raiz.n = 1;
            return;
        } else if (this.raiz.n == (2 * grau_min - 1)) {
            ArvoreB nova_raiz = new ArvoreB(grau_min);
            nova_raiz.folha = false;
            nova_raiz.n = 0;
            nova_raiz.arrayFilhos[0] = this.raiz;
            split(nova_raiz, 0);
            int i = 0;
            if (tabela.buscaHash(nova_raiz.arrayChaves[0].chave).getCodCidade() == novo_no.codCidade) {
                if (tabela.buscaHash(nova_raiz.arrayChaves[0].chave).getDtConfirmacao().before(novo_no.data)) {
                    i++;
                }
            } else {
                if (tabela.buscaHash(nova_raiz.arrayChaves[0].chave).getCodCidade() < novo_no.codCidade) {
                    i++;
                }
            }

            inserirNaoCheio(nova_raiz.arrayFilhos[i], novo_no, tabela);
            this.raiz = nova_raiz;
        } else {
            inserirNaoCheio(this.raiz, novo_no, tabela);
        }
    }

    public boolean comparaChavesMenor(ArvoreB raiz, NoB no, int i, TabelaHash tabela) throws ParseException {
        if (no.codCidade < tabela.buscaHash(raiz.arrayChaves[i].chave).getCodCidade()) {
            return true;
        } else if (no.codCidade == tabela.buscaHash(raiz.arrayChaves[i].chave).getCodCidade()) {
            if (no.data.before(tabela.buscaHash(raiz.arrayChaves[i].chave).getDtConfirmacao())) {
                return true;
            }
        }
        return false;
    }

    public boolean comparaChavesMaior(ArvoreB raiz, NoB no, int i, TabelaHash tabela) throws ParseException {
        if (no.codCidade > tabela.buscaHash(raiz.arrayChaves[i].chave).getCodCidade()) {
            return true;
        } else if (no.codCidade == tabela.buscaHash(raiz.arrayChaves[i].chave).getCodCidade()) {
            if (no.data.after(tabela.buscaHash(raiz.arrayChaves[i].chave).getDtConfirmacao())) {
                return true;
            }
        }
        return false;
    }

    public void inserirNaoCheio(ArvoreB raiz, NoB no, TabelaHash tabela) throws ParseException {
        int i = raiz.n - 1;
        if (raiz.folha) {
            for (; (i >= 0) && comparaChavesMenor(raiz, no, i, tabela); i--) {
                raiz.arrayChaves[i + 1] = raiz.arrayChaves[i];
            }
            raiz.arrayChaves[i + 1] = no;
            raiz.n++;
        } else {
            while ((i >= 0) && comparaChavesMenor(raiz, no, i, tabela)) {
                i--;
            }
            i++;
            if ((raiz.arrayFilhos[i] != null) && raiz.arrayFilhos[i].n == 2 * grau_min - 1) {
                split(raiz, i);
                if (comparaChavesMaior(raiz, no, i, tabela)) {
                    i++;
                }
            } else if (raiz.arrayFilhos[i] == null) {
                raiz.arrayFilhos[i] = new ArvoreB(grau_min);
            }
            inserirNaoCheio(raiz.arrayFilhos[i], no, tabela);
        }
    }

    public ArvoreB busca(int codCidade, TabelaHash tabela) throws ParseException {
        return busca(this.raiz, codCidade, tabela);
    }

    public ArvoreB busca(ArvoreB raiz, int codCidade, TabelaHash tabela) throws ParseException {
        if (raiz == null) {
            return null;
        }
        int i = 0;
        addComparacoes(2);
        while ((i < raiz.n) && codCidade > tabela.buscaHash(raiz.arrayChaves[i].chave).getCodCidade()) {
            addComparacoes(2);
            i++;
        }

        addComparacoes(2);
        if ((i < raiz.n) && (raiz.arrayChaves[i] != null) && codCidade == tabela.buscaHash(raiz.arrayChaves[i].chave).getCodCidade()) {
            return raiz;
        }
        return busca(raiz.arrayFilhos[i], codCidade, tabela);
    }

    public void traverse() {
        inOrder(this.raiz);
    }

    public String transverseString() {
        return inOrderString(this.raiz, "");
    }

    public void inOrder(ArvoreB raiz) {
        if (raiz == null) {
            return;
        }
        int i;
        System.out.print("(");
        for (i = 0; i < raiz.n; i++) {
            if (raiz.arrayFilhos[i] != null) {
                inOrder(raiz.arrayFilhos[i]);
            }
            if ((raiz.arrayChaves[i] != null)) {
                System.out.print(raiz.arrayChaves[i].chave + " ");
            }
        }
        if (raiz.arrayFilhos[i] != null) {
            inOrder(raiz.arrayFilhos[i]);
        }
        System.out.print(")");
    }

    public String inOrderString(ArvoreB raiz, String str) {
        this.saida += str;
        if (raiz == null) {
            this.saida += " ";
        }
        int i;
        for (i = 0; i < raiz.n; i++) {
            if (raiz.arrayFilhos[i] != null) {
                inOrderString(raiz.arrayFilhos[i], this.saida);
            }
            if ((raiz.arrayChaves[i] != null)) {
                this.saida += raiz.arrayChaves[i].chave + " ";
            }
        }
        if (raiz.arrayFilhos[i] != null) {
            inOrderString(raiz.arrayFilhos[i], this.saida);
        }
        return this.saida;
    }

    public void carregamentoB(ArrayList<DadosCovid.Entrada> entradas, TabelaHash tabela) throws ParseException {

        for (DadosCovid.Entrada entrada : entradas) {
            NoHash no = new NoHash();

            no.setDtConfirmacao(entrada.getDtConfirmacao());
            no.setEstado(entrada.getEstado());
            no.setCidade(entrada.getCidade());
            no.setCodCidade(entrada.getCodCidade());
            no.setNumeroCasos(entrada.getNumeroCasos());
            no.setNumeroCasosDiario(entrada.getNumeroCasosDiario());
            no.setNumeroObitos(entrada.getNumeroObitos());

            int id = tabela.getHash(no);

            this.inserir(id, entrada.getCodCidade(), entrada.getDtConfirmacao(), tabela);

        }
    }

    void show() {
        this.traverse();
    }

    void Save(String file_name) throws IOException {
        File file = new File(file_name);
        file.createNewFile();

        FileWriter fw = new FileWriter(file);

        fw.write("Saída Arvore B\n");
        String row = this.transverseString();

        fw.write(row);

        fw.flush();
        fw.close();

    }

    int getTotalCasosCidade(int codCidade, TabelaHash tabela) throws ParseException {
        ArvoreB no = this.busca(codCidade, tabela);
        return contaCasos(no, codCidade, tabela);
    }

    int contaCasos(ArvoreB raiz, int codCidade, TabelaHash tabela) throws ParseException {
        int casos = 0;
        addComparacoes(1);
        if (raiz == null) {
            return casos;
        }

        int i;

        for (i = 0; i < raiz.n; i++) {
            addComparacoes(1);
            if (raiz.arrayFilhos[i] != null) {
                casos += contaCasos(raiz.arrayFilhos[i], codCidade, tabela);
            }
        }
        addComparacoes(1);
        if (raiz.arrayFilhos[i] != null) {
            casos += contaCasos(raiz.arrayFilhos[i], codCidade, tabela);
        }

        for (i = 0; i < raiz.n; i++) {
            addComparacoes(1);
            if (raiz.arrayChaves[i] != null) {
                addComparacoes(1);
                if (tabela.buscaHash(raiz.arrayChaves[i].chave).getCodCidade() == codCidade) {
                    casos += tabela.buscaHash(raiz.arrayChaves[i].chave).getNumeroCasosDiario();
                }

            }
        }

        return casos;
    }

    public static long getComparacoes() {
        return comparacoes;
    }

    public static void setComparacoes(long comparacoes) {
        ArvoreB.comparacoes = comparacoes;
    }

    public static void addComparacoes(int i) {
        ArvoreB.comparacoes += i;
    }

}
