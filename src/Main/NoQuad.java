/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;


public class NoQuad {
//state_code,

    double x, y;
    private String codigoEstado;
    private int codigoCidade;
    private String nomeCidade;
    private double latitude;
    private double longitude;
    private boolean capital;

    public NoQuad() {
    }

    public NoQuad(String codigoEstado, int codigoCidade, String nomeCidade, double latitude, double longitude, boolean capital) {
        this.x = latitude;
        this.y = longitude;
        this.codigoEstado = codigoEstado;
        this.codigoCidade = codigoCidade;
        this.nomeCidade = nomeCidade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capital = capital;
    }

    public NoQuad(double x, double y) {
        this.x = x;
        this.y = y;
        this.codigoEstado = null;
        this.codigoCidade = 0;
        this.nomeCidade = null;
        this.latitude = 0;
        this.longitude = 0;
        this.capital = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public int getCodigoCidade() {
        return codigoCidade;
    }

    public void setCodigoCidade(int codigoCidade) {
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
