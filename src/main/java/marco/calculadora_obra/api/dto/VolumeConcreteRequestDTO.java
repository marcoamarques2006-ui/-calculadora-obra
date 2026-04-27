package marco.calculadora_obra.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class VolumeConcreteRequestDTO {

    @NotEmpty(message = "A lista de arestas não pode ser vazia")
    @Valid
    private List<ArestaDTO> arestas;

    @Positive(message = "Altura da viga baldrame deve ser positiva")
    private double alturaViga;

    public VolumeConcreteRequestDTO() {}

    public List<ArestaDTO> getArestas() { return arestas; }
    public void setArestas(List<ArestaDTO> arestas) { this.arestas = arestas; }

    public double getAlturaViga() { return alturaViga; }
    public void setAlturaViga(double alturaViga) { this.alturaViga = alturaViga; }
}
