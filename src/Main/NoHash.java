/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.Date;

public class NoHash {

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
