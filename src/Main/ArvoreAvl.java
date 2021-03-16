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
import javafx.scene.control.Tab;

class ArvoreAvl {

    NoAvl raiz;
    static String saida = new String();
    static long comparacoes = 0;

    int altura(NoAvl no) {
        if (no == null) {
            return 0;
        }

        return no.altura;
    }

    int maior(int a, int b) {
        return (a > b) ? a : b;
    }

    NoAvl rotacaoDir(NoAvl y) {
        NoAvl x = y.esq;
        NoAvl aux = x.dir;

        x.dir = y;
        y.esq = aux;

        y.altura = maior(altura(y.esq), altura(y.dir)) + 1;
        x.altura = maior(altura(x.esq), altura(x.dir)) + 1;

        return x;
    }

    NoAvl rotacaoEsq(NoAvl x) {
        NoAvl y = x.dir;
        NoAvl aux = y.esq;

        y.esq = x;
        x.dir = aux;

        x.altura = maior(altura(x.esq), altura(x.dir)) + 1;
        y.altura = maior(altura(y.esq), altura(y.dir)) + 1;

        return y;
    }

    int getFatorBalanceamento(NoAvl no) {
        if (no == null) {
            return 0;
        }
        return altura(no.esq) - altura(no.dir);
    }

    NoAvl busca(int codCidade, TabelaHash tabela) throws ParseException {
        return busca(this.raiz, codCidade, tabela);
    }

    NoAvl busca(NoAvl raiz, int codCidade, TabelaHash tabela) throws ParseException {
        comparacoes++;
        if (raiz == null) {
            return null;
        }
        comparacoes++;
        if (tabela.buscaHash(raiz.chave).getCodCidade() == codCidade) {
            return raiz;
        }
        comparacoes++;
        if (tabela.buscaHash(raiz.chave).getCodCidade() > codCidade) {
            return busca(raiz.esq, codCidade, tabela);
        } else {
            return busca(raiz.dir, codCidade, tabela);
        }
    }

    void inserir(int chave, int codCidade, Date data, TabelaHash tabelaHash) throws ParseException {
        this.raiz = this.inserir(this.raiz, chave, codCidade, data, tabelaHash);
    }

    NoAvl inserir(NoAvl no, int chave, int codCidade, Date data, TabelaHash tabelaHash) throws ParseException {
        if (no == null) {
            return (new NoAvl(chave));
        }

        if (codCidade < tabelaHash.buscaHash(no.chave).getCodCidade()) {
            no.esq = inserir(no.esq, chave, codCidade, data, tabelaHash);
        } else if (codCidade > tabelaHash.buscaHash(no.chave).getCodCidade()) {
            no.dir = inserir(no.dir, chave, codCidade, data, tabelaHash);
        } else if (data.before(tabelaHash.buscaHash(no.chave).getDtConfirmacao())) {
            no.esq = inserir(no.esq, chave, codCidade, data, tabelaHash);
        } else if (data.after(tabelaHash.buscaHash(no.chave).getDtConfirmacao())) {
            no.dir = inserir(no.dir, chave, codCidade, data, tabelaHash);
        } else {
            return no;
        }

        no.altura = 1 + maior(altura(no.esq), altura(no.dir));

        int fatorBalanceamento = getFatorBalanceamento(no);

        if (fatorBalanceamento > 1) {
            if (codCidade < tabelaHash.buscaHash(no.esq.chave).getCodCidade()) {
                return rotacaoDir(no);
            } else if (codCidade > tabelaHash.buscaHash(no.esq.chave).getCodCidade()) {
                no.esq = rotacaoEsq(no.esq);
                return rotacaoDir(no);
            } else {
                if (data.before(tabelaHash.buscaHash(no.esq.chave).getDtConfirmacao())) {
                    return rotacaoDir(no);
                } else if (data.after(tabelaHash.buscaHash(no.esq.chave).getDtConfirmacao())) {
                    no.esq = rotacaoEsq(no.esq);
                    return rotacaoDir(no);
                }
            }
        }

        if (fatorBalanceamento < -1) {
            if (codCidade > tabelaHash.buscaHash(no.dir.chave).getCodCidade()) {
                return rotacaoEsq(no);
            } else if (codCidade < tabelaHash.buscaHash(no.dir.chave).getCodCidade()) {
                no.dir = rotacaoDir(no.dir);
                return rotacaoEsq(no);
            } else {
                if (data.after(tabelaHash.buscaHash(no.dir.chave).getDtConfirmacao())) {
                    return rotacaoEsq(no);
                } else if (data.before(tabelaHash.buscaHash(no.dir.chave).getDtConfirmacao())) {
                    no.dir = rotacaoDir(no.dir);
                    return rotacaoEsq(no);
                }
            }
        }

        return no;

    }

    void inOrderTransverse(int nivel) {
        inOrder(this.raiz, nivel);
    }

    void preOrderTransverse() {
        preOrder(this.raiz);
    }

    void posOrderTransverse(int nivel) {
        posOrder(this.raiz, nivel);
    }

    String posOrderTransverseString() {
        return posOrderString(this.raiz, " ");
    }

    void inOrder(NoAvl no, int nivel) {
        if (no == null) {
            return;
        }
        if (no.esq != null) {
            inOrder(no.esq, nivel + 1);
        }
        System.out.print(no.chave + " ");
        if (no.dir != null) {
            inOrder(no.dir, nivel + 1);
        }
    }

    private String posOrderString(NoAvl no, String str) {
        this.saida = str;
        if (no == null) {
            return this.saida += " ";
        }
        if (no.esq != null) {
            this.saida += " (";
            posOrderString(no.esq, this.saida);
            this.saida += " )";
        }
        if (no.dir != null) {
            this.saida += " (";
            posOrderString(no.dir, this.saida);
            this.saida += " )";
        }

        this.saida += no.chave + " ";
        return this.saida;
    }

    void preOrder(NoAvl no) {
        if (no == null) {
            return;
        }
        System.out.print(no.chave + " ");
        preOrder(no.esq);
        preOrder(no.dir);
    }

    void posOrder(NoAvl no, int nivel) {
        if (no == null) {
            return;
        }

        System.out.print("(");
        if (no.esq != null) {
            posOrder(no.esq, nivel + 1);
        }
        if (no.dir != null) {
            posOrder(no.dir, nivel + 1);
        }
        System.out.print(")");

        System.out.print(no.chave + " ");
//        String tab = new String();
//        tab = "";
//        for (int i = 0; i < nivel; i++) {
//            tab += "\t";
//        }
    }

    public void carregamentoAvl(ArrayList<DadosCovid.Entrada> entradas, TabelaHash tabela) throws ParseException {
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
        int nivel = 0;
        this.posOrderTransverse(nivel);
    }

    void Save(String file_name) throws IOException {
        File file = new File(file_name);
        file.createNewFile();

        FileWriter fw = new FileWriter(file);

        fw.write("Saída Arvore AVL\n");
        String row = this.posOrderTransverseString();

        fw.write(row);

        fw.flush();
        fw.close();
    }

    public int getTotalCasosCidade(int codCidade, TabelaHash tabela) throws ParseException {
        NoAvl no = this.busca(codCidade, tabela);
        return contaCasos(no, tabela, codCidade);
    }

    public int contaCasos(NoAvl no, TabelaHash tabela, int codCidade) throws ParseException {
        int casos = 0;
        comparacoes++;
        if (no == null) {
            return casos;
        }
        comparacoes++;
        if (tabela.buscaHash(no.chave).getCodCidade() != codCidade) {
            comparacoes++;
            if (no.esq != null) {
                casos += contaCasos(no.esq, tabela, codCidade);
            }
            comparacoes++;
            if (no.dir != null) {
                casos += contaCasos(no.dir, tabela, codCidade);
            }

            return casos;
        } else {
            comparacoes++;
            if (no.esq != null) {
                casos += contaCasos(no.esq, tabela, codCidade);
            }
            comparacoes++;
            if (no.dir != null) {
                casos += contaCasos(no.dir, tabela, codCidade);
            }
            return casos + tabela.buscaHash(no.chave).getNumeroCasosDiario();
        }
    }

    public long getComparacoes() {
        return this.comparacoes;
    }

}
