package marco.calculadora_obra.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * Detalhe por parede (aresta) dentro de um orçamento: área, volume de concreto e tijolos.
 */
@Entity
@Table(name = "orcamento_itens")
public class OrcamentoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    private String paredeId;
    private double comprimento;
    private double espessura;
    private double alturaParede;
    private double areaLiquida;
    private double volumeConcreto;
    private int quantidadeTijolos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_numero")
    private OrcamentoEntity orcamento;

    public OrcamentoItemEntity() {}

    public Long getPk() { return pk; }

    public String getParedeId() { return paredeId; }
    public void setParedeId(String paredeId) { this.paredeId = paredeId; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getEspessura() { return espessura; }
    public void setEspessura(double espessura) { this.espessura = espessura; }

    public double getAlturaParede() { return alturaParede; }
    public void setAlturaParede(double alturaParede) { this.alturaParede = alturaParede; }

    public double getAreaLiquida() { return areaLiquida; }
    public void setAreaLiquida(double areaLiquida) { this.areaLiquida = areaLiquida; }

    public double getVolumeConcreto() { return volumeConcreto; }
    public void setVolumeConcreto(double volumeConcreto) { this.volumeConcreto = volumeConcreto; }

    public int getQuantidadeTijolos() { return quantidadeTijolos; }
    public void setQuantidadeTijolos(int quantidadeTijolos) { this.quantidadeTijolos = quantidadeTijolos; }

    public OrcamentoEntity getOrcamento() { return orcamento; }
    public void setOrcamento(OrcamentoEntity orcamento) { this.orcamento = orcamento; }
}
