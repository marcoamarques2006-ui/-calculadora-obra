package marco.calculadora_obra.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidade JPA para persistência de arestas (paredes) no banco de dados.
 * Princípio S: responsável apenas pelo mapeamento ORM da aresta.
 */
@Entity
@Table(name = "arestas")
public class ArestaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(nullable = false)
    private String arestaId;

    private double comprimento;
    private double espessura;
    private double alturaParede;
    private boolean temJanela;
    private boolean temPorta;

    private Double alturaJanela;
    private Double comprimentoJanela;
    private Double alturaPorta;
    private Double comprimentoPorta;

    public ArestaEntity() {}

    public Long getPk() { return pk; }

    public String getArestaId() { return arestaId; }
    public void setArestaId(String arestaId) { this.arestaId = arestaId; }

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

    public Double getAlturaJanela() { return alturaJanela; }
    public void setAlturaJanela(Double alturaJanela) { this.alturaJanela = alturaJanela; }

    public Double getComprimentoJanela() { return comprimentoJanela; }
    public void setComprimentoJanela(Double comprimentoJanela) { this.comprimentoJanela = comprimentoJanela; }

    public Double getAlturaPorta() { return alturaPorta; }
    public void setAlturaPorta(Double alturaPorta) { this.alturaPorta = alturaPorta; }

    public Double getComprimentoPorta() { return comprimentoPorta; }
    public void setComprimentoPorta(Double comprimentoPorta) { this.comprimentoPorta = comprimentoPorta; }
}
