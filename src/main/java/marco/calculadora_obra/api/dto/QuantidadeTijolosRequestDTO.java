package marco.calculadora_obra.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class QuantidadeTijolosRequestDTO {

    @NotEmpty(message = "A lista de arestas não pode ser vazia")
    @Valid
    private List<ArestaDTO> arestas;

    @Positive(message = "Altura do tijolo deve ser positiva")
    private double alturaTijolo;

    @Positive(message = "Largura do tijolo deve ser positiva")
    private double larguraTijolo;

    @Positive(message = "Comprimento do tijolo deve ser positivo")
    private double comprimentoTijolo;

    public QuantidadeTijolosRequestDTO() {}

    public List<ArestaDTO> getArestas() { return arestas; }
    public void setArestas(List<ArestaDTO> arestas) { this.arestas = arestas; }

    public double getAlturaTijolo() { return alturaTijolo; }
    public void setAlturaTijolo(double alturaTijolo) { this.alturaTijolo = alturaTijolo; }

    public double getLarguraTijolo() { return larguraTijolo; }
    public void setLarguraTijolo(double larguraTijolo) { this.larguraTijolo = larguraTijolo; }

    public double getComprimentoTijolo() { return comprimentoTijolo; }
    public void setComprimentoTijolo(double comprimentoTijolo) { this.comprimentoTijolo = comprimentoTijolo; }
}
