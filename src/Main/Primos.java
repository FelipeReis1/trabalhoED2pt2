/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;


public class Primos {

    int[] primos;
    boolean[] prime;

    public Primos() {
        primos = new int[0];
    }

    public int[] getPrimos() {
        return primos;
    }

    public void setPrimos(int[] primos) {
        this.primos = primos;
    }

    public void sieve(int tam) {
        prime = new boolean[tam];
        for (int i = 0; i < tam; i++) {
            prime[i] = true;
        }

        for (int _p = 2; _p * _p < tam; _p++) {
            // if prime[p] is not changed, then it is a prime 
            if (prime[_p] == true) {
                // update all multiples of p 
                for (int i = _p * _p; i < tam; i += _p) {
                    prime[i] = false;
                }
            }
        }
    }

}
