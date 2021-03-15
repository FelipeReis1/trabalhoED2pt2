/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;


public class NoAvl {

    int chave;
    int altura;
    NoAvl esq;
    NoAvl dir;

    NoAvl(int d) {
        chave = d;
        altura = 1;
    }
}
