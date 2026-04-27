package marco.calculadora_obra.api.dto;

import java.util.List;

public class QuantidadeTijolosResponseDTO {

    private int quantidadeTotalTijolos;
    private int quantidadeComPerda;
    private double areaTotalLiquida;
    private List<DetalheParedeDTO> detalhes;

    public QuantidadeTijolosResponseDTO() {}

    public QuantidadeTijolosResponseDTO(int quantidadeTotalTijolos,
                                        int quantidadeComPerda,
                                        double areaTotalLiquida,
                                        List<DetalheParedeDTO> detalhes) {
        this.quantidadeTotalTijolos = quantidadeTotalTijolos;
        this.quantidadeComPerda = quantidadeComPerda;
        this.areaTotalLiquida = areaTotalLiquida;
        this.detalhes = detalhes;
    }

    public int getQuantidadeTotalTijolos() { return quantidadeTotalTijolos; }
    public int getQuantidadeComPerda() { return quantidadeComPerda; }
    public double getAreaTotalLiquida() { return areaTotalLiquida; }
    public List<DetalheParedeDTO> getDetalhes() { return detalhes; }
}
