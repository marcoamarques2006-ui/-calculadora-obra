package marco.calculadora_obra.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class QuantidadeTijolosRequestDTO {

    private Long plantaBaixaId;

    @Valid
    private List<ArestaDTO> arestas;

    @Positive(message = "Altura do tijolo deve ser positiva")
    private double alturaTijolo;

    @Positive(message = "Comprimento do tijolo deve ser positivo")
    private double comprimentoTijolo;

    public QuantidadeTijolosRequestDTO() {}

    public Long getPlantaBaixaId() { return plantaBaixaId; }
    public void setPlantaBaixaId(Long plantaBaixaId) { this.plantaBaixaId = plantaBaixaId; }

    public List<ArestaDTO> getArestas() { return arestas; }
    public void setArestas(List<ArestaDTO> arestas) { this.arestas = arestas; }

    public double getAlturaTijolo() { return alturaTijolo; }
    public void setAlturaTijolo(double alturaTijolo) { this.alturaTijolo = alturaTijolo; }

    public double getComprimentoTijolo() { return comprimentoTijolo; }
    public void setComprimentoTijolo(double comprimentoTijolo) { this.comprimentoTijolo = comprimentoTijolo; }
}
