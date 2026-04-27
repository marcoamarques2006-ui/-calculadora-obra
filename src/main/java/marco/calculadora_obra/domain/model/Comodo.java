package marco.calculadora_obra.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um cômodo da planta baixa.
 * Um cômodo é formado por um conjunto de paredes (arestas).
 * Princípio S: responsável apenas pelos dados de um cômodo.
 */
public class Comodo {

    private String nome;
    private double largura;
    private double comprimento;
    private double altura;
    private List<Aresta> paredes = new ArrayList<>();

    public Comodo() {}

    public Comodo(String nome, double largura, double comprimento, double altura) {
        this.nome = nome;
        this.largura = largura;
        this.comprimento = comprimento;
        this.altura = altura;
    }

    public double getArea() {
        return largura * comprimento;
    }

    public double getVolume() {
        return largura * comprimento * altura;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getLargura() { return largura; }
    public void setLargura(double largura) { this.largura = largura; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public List<Aresta> getParedes() { return paredes; }
    public void setParedes(List<Aresta> paredes) { this.paredes = paredes; }
}
