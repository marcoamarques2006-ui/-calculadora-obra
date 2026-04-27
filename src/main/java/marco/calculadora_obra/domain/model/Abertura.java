package marco.calculadora_obra.domain.model;

/**
 * Representa uma abertura (janela ou porta) em uma parede.
 * Princípio S: responsável apenas por armazenar dados de uma abertura.
 */
public class Abertura {

    private double altura;
    private double comprimento;

    public Abertura() {}

    public Abertura(double altura, double comprimento) {
        this.altura = altura;
        this.comprimento = comprimento;
    }

    public double getArea() {
        return altura * comprimento;
    }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }
}
