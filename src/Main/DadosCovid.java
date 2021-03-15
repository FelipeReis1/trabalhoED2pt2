/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;


public class DadosCovid {

    protected ArrayList<EntradaCidades> entradasCidades;
    protected ArrayList<Entrada> entradas;
    String dataset;

    public DadosCovid(String dataset, int tipoDataset) throws IOException {
        this.dataset = dataset;
        switch (tipoDataset) {
            case 1:
                this.entradasCidades = new ArrayList<>();
                carregamentoCidades();
                break;
            case 2:
                this.entradas = new ArrayList<>();
                carregamentoCasos();
                break;
            default:
                break;
        }
    }

    private void carregamentoCidades() throws IOException {
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(this.dataset))) {
            String row;
            br.readLine();
            while ((row = br.readLine()) != null) {
                EntradaCidades entrada = new EntradaCidades();
                String[] dados = row.split(",");
                entrada.setCodigoEstado(dados[0]);
                entrada.setCodigoCidade(dados[1]);
                entrada.setNomeCidade(dados[2]);
                entrada.setLatitude(Double.parseDouble(dados[3]));
                entrada.setLongitude(Double.parseDouble(dados[4]));
                entrada.setCapital(Boolean.parseBoolean(dados[5]));
                this.entradasCidades.add(entrada);
                i++;
            }

        }
    }
    
       private void carregamentoCasos() throws IOException {
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(this.dataset))) {
            String row;
            br.readLine();
            while ((row = br.readLine()) != null) {
                Entrada entrada = new Entrada();
                String[] dados = row.split(",");
                entrada.setDtConfirmacao(new SimpleDateFormat("yyyy-MM-dd").parse(dados[0]));
                entrada.setEstado(dados[1]);
                entrada.setCidade(dados[2]);
                entrada.setCodCidade((int) Float.parseFloat(dados[3]));
                entrada.setNumeroCasos(Integer.parseInt(dados[4]));
                entrada.setNumeroCasosDiario(Integer.parseInt(dados[5]));
                entrada.setNumeroObitos(Integer.parseInt(dados[6]));
                this.entradas.add(entrada);
                i++;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setEntradasCidades(ArrayList<EntradaCidades> entradas) {
        this.entradasCidades = entradas;
    }

    public ArrayList<EntradaCidades> getEntradasCidades() {
        return entradasCidades;
    }

    public void setEntradas(ArrayList<Entrada> entradas) {
        this.entradas = entradas;
    }

    public ArrayList<Entrada> getEntradas() {
        return entradas;
    }

    public static class Entrada {

        private String cidade;
        private String estado;
        private int cod_cidade;
        private int numeroObitos;
        private int numeroCasos;
        private int numeroCasosDiario;
        private Date dt_confirmacao;

        public int getNumeroCasosDiario() {
            return numeroCasosDiario;
        }

        public void setNumeroCasosDiario(int numeroCasosDiario) {
            this.numeroCasosDiario = numeroCasosDiario;
        }

        public String getCidade() {
            return cidade;
        }

        public void setCidade(String cidade) {
            this.cidade = cidade;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public Date getDtConfirmacao() {
            return dt_confirmacao;
        }

        public void setDtConfirmacao(Date data) {
            this.dt_confirmacao = data;
        }

        public int getNumeroObitos() {
            return numeroObitos;
        }

        public void setNumeroObitos(int numeroObitos) {
            this.numeroObitos = numeroObitos;
        }

        public int getNumeroCasos() {
            return numeroCasos;
        }

        public void setNumeroCasos(int numeroCasos) {
            this.numeroCasos = numeroCasos;
        }

        public int getCodCidade() {
            return cod_cidade;
        }

        public void setCodCidade(int cod_cidade) {
            this.cod_cidade = cod_cidade;
        }
    }

    public static class EntradaCidades {

        private String codigoEstado;
        private String codigoCidade;
        private String nomeCidade;
        private double latitude;
        private double longitude;
        private boolean capital;

        public EntradaCidades(String codigoEstado, String codigoCidade, String nomeCidade, double latitude, double longitude, boolean capital) {
            this.codigoEstado = codigoEstado;
            this.codigoCidade = codigoCidade;
            this.nomeCidade = nomeCidade;
            this.latitude = latitude;
            this.longitude = longitude;
            this.capital = capital;

        }

        public EntradaCidades() {
            this.codigoEstado = null;
            this.codigoCidade = null;
            this.nomeCidade = null;
            this.latitude = 0;
            this.longitude = 0;
            this.capital = false;
        }

        public String getCodigoEstado() {
            return codigoEstado;
        }

        public void setCodigoEstado(String codigoEstado) {
            this.codigoEstado = codigoEstado;
        }

        public String getCodigoCidade() {
            return codigoCidade;
        }

        public void setCodigoCidade(String codigoCidade) {
            this.codigoCidade = codigoCidade;
        }

        public String getNomeCidade() {
            return nomeCidade;
        }

        public void setNomeCidade(String nomeCidade) {
            this.nomeCidade = nomeCidade;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public boolean isCapital() {
            return capital;
        }

        public void setCapital(boolean capital) {
            this.capital = capital;
        }
    }

}
