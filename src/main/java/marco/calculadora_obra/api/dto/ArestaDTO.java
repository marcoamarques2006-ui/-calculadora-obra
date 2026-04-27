package marco.calculadora_obra.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ArestaDTO {

    @NotBlank(message = "ID da aresta é obrigatório")
    private String id;

    private String origemId;
    private String destinoId;

    @Positive(message = "Comprimento deve ser positivo")
    private double comprimento;

    @Positive(message = "Espessura deve ser positiva")
    private double espessura;

    @Positive(message = "Altura da parede deve ser positiva")
    private double alturaParede;

    private boolean temJanela;
    private boolean temPorta;

    @Valid
    private AberturaDTO janela;

    @Valid
    private AberturaDTO porta;

    public ArestaDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrigemId() { return origemId; }
    public void setOrigemId(String origemId) { this.origemId = origemId; }

    public String getDestinoId() { return destinoId; }
    public void setDestinoId(String destinoId) { this.destinoId = destinoId; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getEspessura() { return espessura; }
    public void setEspessura(double espessura) { this.espessura = espessura; }

    public double getAlturaParede() { return alturaParede; }
    public void setAlturaParede(double alturaParede) { this.alturaParede = alturaParede; }

    public boolean isTemJanela() { return temJanela; }
    public void setTemJanela(boolean temJanela) { this.temJanela = temJanela; }

    public boolean isTemPorta() { return temPorta; }
    public void setTemPorta(boolean temPorta) { this.temPorta = temPorta; }

    public AberturaDTO getJanela() { return janela; }
    public void setJanela(AberturaDTO janela) { this.janela = janela; }

    public AberturaDTO getPorta() { return porta; }
    public void setPorta(AberturaDTO porta) { this.porta = porta; }
}
