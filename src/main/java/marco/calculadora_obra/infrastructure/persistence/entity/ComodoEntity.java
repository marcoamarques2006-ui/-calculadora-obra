package marco.calculadora_obra.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comodos")
public class ComodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(nullable = false)
    private String nome;

    private double largura;
    private double comprimento;
    private double altura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planta_baixa_pk")
    private PlantaBaixaEntity plantaBaixa;

    @ManyToMany
    @JoinTable(
        name = "comodo_arestas",
        joinColumns = @JoinColumn(name = "comodo_pk"),
        inverseJoinColumns = @JoinColumn(name = "aresta_pk")
    )
    private List<ArestaEntity> paredes = new ArrayList<>();

    public ComodoEntity() {}

    public Long getPk() { return pk; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getLargura() { return largura; }
    public void setLargura(double largura) { this.largura = largura; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public PlantaBaixaEntity getPlantaBaixa() { return plantaBaixa; }
    public void setPlantaBaixa(PlantaBaixaEntity plantaBaixa) { this.plantaBaixa = plantaBaixa; }

    public List<ArestaEntity> getParedes() { return paredes; }
    public void setParedes(List<ArestaEntity> paredes) { this.paredes = paredes; }
}