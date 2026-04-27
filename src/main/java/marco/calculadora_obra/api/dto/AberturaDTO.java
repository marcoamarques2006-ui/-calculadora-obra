package marco.calculadora_obra.api.dto;

import jakarta.validation.constraints.Positive;

public class AberturaDTO {

    @Positive(message = "Altura da abertura deve ser positiva")
    private double altura;

    @Positive(message = "Comprimento da abertura deve ser positivo")
    private double comprimento;

    public AberturaDTO() {}

    public AberturaDTO(double altura, double comprimento) {
        this.altura = altura;
        this.comprimento = comprimento;
    }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }
}
