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

/**
 *
 * @author Felipe
 */
public class DadosCovid {
        
    
    
        protected ArrayList<Entrada> entradas;
        String dataset;
    
    
        public static class Entrada {
        
        private String codigoEstado;
        private String codigoCidade;
        private String nomeCidade;
        private double latitude;
        private double longitude;
        private boolean capital;
        
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
        public DadosCovid(String dataset) throws IOException {
        this.dataset = dataset;
        this.entradas = new ArrayList<>();
        //QuadTree quad = new QuadTree();
        Carregamento();
        //PreProcessamento();
        //Save("brazil_covid19_cities_processado.csv");

    }

    private void Carregamento() throws IOException {
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(this.dataset))) {
            String row;
            br.readLine();
            while ((row = br.readLine()) != null) {
                Entrada entrada = new Entrada();
                String[] dados = row.split(",");    
                //quad.insert.setCodigoEstado(dados[0])
                entrada.setCodigoEstado(dados[0]);
                entrada.setCodigoCidade(dados[1]);
                entrada.setNomeCidade(dados[2]);
                entrada.setLatitude(Double.parseDouble(dados[3]));
                entrada.setLongitude(Double.parseDouble(dados[4]));
                this.entradas.add(entrada);
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
