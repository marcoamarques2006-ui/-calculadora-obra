package marco.calculadora_obra.api.dto;

public class DetalheParedeDTO {

    private String arestaId;
    private double areaParede;
    private double areaAberturas;
    private double areaLiquida;
    private int quantidadeTijolos;

    public DetalheParedeDTO() {}

    public DetalheParedeDTO(String arestaId, double areaParede,
                            double areaAberturas, double areaLiquida,
                            int quantidadeTijolos) {
        this.arestaId = arestaId;
        this.areaParede = areaParede;
        this.areaAberturas = areaAberturas;
        this.areaLiquida = areaLiquida;
        this.quantidadeTijolos = quantidadeTijolos;
    }

    public String getArestaId() { return arestaId; }
    public double getAreaParede() { return areaParede; }
    public double getAreaAberturas() { return areaAberturas; }
    public double getAreaLiquida() { return areaLiquida; }
    public int getQuantidadeTijolos() { return quantidadeTijolos; }
}
