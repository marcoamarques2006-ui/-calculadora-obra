package marco.calculadora_obra.api.dto;

import java.util.List;

public class VolumeConcretoResponseDTO {

    private double volumeTotalM3;
    private int totalArestas;
    private List<DetalheVigaDTO> detalhes;

    public VolumeConcretoResponseDTO() {}

    public VolumeConcretoResponseDTO(double volumeTotalM3,
                                     int totalArestas,
                                     List<DetalheVigaDTO> detalhes) {
        this.volumeTotalM3 = volumeTotalM3;
        this.totalArestas = totalArestas;
        this.detalhes = detalhes;
    }

    public double getVolumeTotalM3() { return volumeTotalM3; }
    public int getTotalArestas() { return totalArestas; }
    public List<DetalheVigaDTO> getDetalhes() { return detalhes; }
}