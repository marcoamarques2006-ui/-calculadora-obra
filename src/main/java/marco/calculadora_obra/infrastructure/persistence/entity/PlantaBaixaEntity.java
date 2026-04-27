package marco.calculadora_obra.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plantas_baixas")
public class PlantaBaixaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    private String descricao;

    @OneToMany(mappedBy = "plantaBaixa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VerticeEntity> vertices = new ArrayList<>();

    @OneToMany(mappedBy = "plantaBaixa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArestaEntity> arestas = new ArrayList<>();

    @OneToMany(mappedBy = "plantaBaixa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComodoEntity> comodos = new ArrayList<>();

    public PlantaBaixaEntity() {}

    public Long getPk() { return pk; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<VerticeEntity> getVertices() { return vertices; }
    public void setVertices(List<VerticeEntity> vertices) { this.vertices = vertices; }

    public List<ArestaEntity> getArestas() { return arestas; }
    public void setArestas(List<ArestaEntity> arestas) { this.arestas = arestas; }

    public List<ComodoEntity> getComodos() { return comodos; }
    public void setComodos(List<ComodoEntity> comodos) { this.comodos = comodos; }
}