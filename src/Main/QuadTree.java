/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class QuadTree {

    static int count = 0;
    static String saida = new String();
    static long comparacoes = 0;

    QuadTree[] quads;

    Boundary bound;

    NoQuad node;

    QuadTree() {
        quads = new QuadTree[4];
        for (int i = 0; i < 4; i++) {
            quads[i] = null;
        }
        node = null;
        bound = new Boundary(new NoQuad((double) -35.0000, (double) -75.0000), new NoQuad((double) 6.000, (double) -30.0000));
    }

    QuadTree(Boundary b) {
        quads = new QuadTree[4];
        for (int i = 0; i < 4; i++) {
            quads[i] = null;
        }
        node = null;
        bound = b;
    }

    QuadTree(NoQuad inferior, NoQuad superior) {
        quads = new QuadTree[4];
        for (int i = 0; i < 4; i++) {
            quads[i] = null;
        }
        node = null;
        bound = new Boundary(inferior, superior);
    }

    public void carregamentoQuadTree(ArrayList<DadosCovid.EntradaCidades> entradasQuad) throws IOException {
        for (DadosCovid.EntradaCidades entrada : entradasQuad) {
            NoQuad ponto = new NoQuad(entrada.getLatitude(), entrada.getLongitude());
            ponto.setCodigoEstado(entrada.getCodigoEstado());
            ponto.setCodigoCidade(Integer.parseInt(entrada.getCodigoCidade()));
            ponto.setNomeCidade(entrada.getNomeCidade());
            ponto.setLatitude(entrada.getLatitude());
            ponto.setLongitude(entrada.getLongitude());
            ponto.setCapital(entrada.isCapital());
            this.insert(ponto);
        }
    }

    public void insert(NoQuad p) {
        if (p == null) {
            return;
        }
        // o nó atual não pode conter o ponto
        if (!bound.check(p)) {
            return;
        }

        // se a área do quad é unitária, não pode ser mais subdividido
        if (node == null) {
            node = p;
            return;
        }

        int quadrante = bound.findQuadrant(p);
        if (quads[quadrante] == null) {
            quads[quadrante] = new QuadTree(bound.generateNewBoundary(quadrante));
        }
        quads[quadrante].insert(p);

    }

    public NoQuad search(NoQuad p) {
        if (!bound.check(p)) {
            return null;
        }

        if ((node != null) && (p.x == node.x) && (p.y == node.y)) {
            return node;
        }

        int quadrante = bound.findQuadrant(p);
        if (quads[quadrante] == null) {
            return null;
        }
        return quads[quadrante].search(p);
    }

    public void showQuad() {
        System.out.println(this.getSaida(""));
    }

    public String getSaida(String str) {
        this.saida = str;

        if (this.node != null) {
            this.saida += this.node.getNomeCidade();
        }
        this.saida += "(";
        for (QuadTree subQuad : quads) {
            if (subQuad != null) {
                subQuad.getSaida(saida);
            } else {
                this.saida += " |null| ";
            }
        }
        this.saida += ")";

        return this.saida;
    }

    public void Save(String file_name) throws IOException {
        File file = new File(file_name);
        file.createNewFile();

        FileWriter fw = new FileWriter(file);

        fw.write("Saída QuadTree\n");
        String row = this.getSaida("");

        fw.write(row);

        fw.flush();
        fw.close();
    }

    public ArrayList<NoQuad> searchRange(NoQuad inferior, NoQuad superior) {
        ArrayList<NoQuad> nos = new ArrayList();
        Boundary b = new Boundary(inferior, superior);

        addComparacoes(4);
        if (!this.bound.intersect(b)) {
            return nos;
        }

        addComparacoes(4);
        if (b.check(this.node)) {
            nos.add(this.node);
        }

        for (QuadTree no : this.quads) {
            if (no != null) {
                nos.addAll(no.searchRange(inferior, superior));
            }
        }

        return nos;

    }

    public static long getComparacoes() {
        return comparacoes;
    }

    public static void addComparacoes(int i) {
        comparacoes += i;
    }

}

class Boundary {

    NoQuad superior;
    NoQuad inferior;

    public Boundary() {
    }

    public Boundary(NoQuad inferior, NoQuad superior) {
        this.superior = superior;
        this.inferior = inferior;
    }

    public Boolean intersect(Boundary b) {
        double w1, w2, h1, h2;

        w1 = this.superior.x - this.inferior.x;
        h1 = this.superior.y - this.inferior.y;

        w2 = b.superior.x - b.inferior.x;
        h2 = b.superior.y - b.inferior.y;

        if (this.inferior.x < b.inferior.x + w2
                && this.inferior.x + w1 > b.inferior.x
                && this.inferior.y < b.inferior.y + h2
                && this.inferior.y + h1 > b.inferior.y) {
            return true;
        }
        return false;

    }

    public Boolean check(NoQuad p) {
        return (p.x >= inferior.x
                && p.x <= superior.x
                && p.y >= inferior.y
                && p.y <= superior.y);
    }

    public Boolean isUnitary() {
        return (Math.abs(inferior.x - superior.x) <= 1)
                && (Math.abs(superior.y - inferior.y) <= 1);
    }

    public int findQuadrant(NoQuad p) {
        if ((superior.x + inferior.x) / 2 >= p.x) {
            if ((superior.y + inferior.y) / 2 >= p.y) {
                return 0;
            } else {
                return 2;
            }
        } else {
            if ((superior.y + inferior.y) / 2 >= p.y) {
                return 1;
            } else {
                return 3;
            }
        }
    }

    public Boundary generateNewBoundary(int quadrant) {
        switch (quadrant) {
            // primeiro quadrante
            case 0:
                return new Boundary(new NoQuad(inferior.x, inferior.y),
                        new NoQuad((superior.x + inferior.x) / 2,
                                (superior.y + inferior.y) / 2));
            // segundo quadrante
            case 1:
                return new Boundary(new NoQuad((superior.x + inferior.x) / 2, inferior.y),
                        new NoQuad(superior.x,
                                (superior.y + inferior.y) / 2));
            // terceiro quadrante
            case 2:
                return new Boundary(new NoQuad(inferior.x, (superior.y + inferior.y) / 2),
                        new NoQuad((superior.x + inferior.x) / 2, superior.y));
            // quarto quadrante
            case 3:
                return new Boundary(new NoQuad((superior.x + inferior.x) / 2, (superior.y + inferior.y) / 2),
                        new NoQuad(superior.x, superior.y));
            default:
                break;
        };
        return null;
    }
}
