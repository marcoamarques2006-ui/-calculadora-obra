package marco.calculadora_obra.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vertices")
public class VerticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(nullable = false)
    private String verticeId;

    private double x;
    private double y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planta_baixa_pk")
    private PlantaBaixaEntity plantaBaixa;

    public VerticeEntity() {}

    public Long getPk() { return pk; }

    public String getVerticeId() { return verticeId; }
    public void setVerticeId(String verticeId) { this.verticeId = verticeId; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public PlantaBaixaEntity getPlantaBaixa() { return plantaBaixa; }
    public void setPlantaBaixa(PlantaBaixaEntity plantaBaixa) { this.plantaBaixa = plantaBaixa; }
}