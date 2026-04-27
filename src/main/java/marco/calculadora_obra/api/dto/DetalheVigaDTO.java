package marco.calculadora_obra.api.dto;

public class DetalheVigaDTO {

    private String arestaId;
    private double comprimento;
    private double largura;
    private double altura;
    private double volume;

    public DetalheVigaDTO() {}

    public DetalheVigaDTO(String arestaId, double comprimento,
                          double largura, double altura, double volume) {
        this.arestaId = arestaId;
        this.comprimento = comprimento;
        this.largura = largura;
        this.altura = altura;
        this.volume = volume;
    }

    public String getArestaId() { return arestaId; }
    public double getComprimento() { return comprimento; }
    public double getLargura() { return largura; }
    public double getAltura() { return altura; }
    public double getVolume() { return volume; }
}
