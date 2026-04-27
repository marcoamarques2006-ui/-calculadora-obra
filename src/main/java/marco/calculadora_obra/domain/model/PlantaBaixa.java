package marco.calculadora_obra.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Grafo G=(V,A) representando a planta baixa da casa.
 * V = vértices (pilares nos encontros de paredes)
 * A = arestas (paredes com espessura, janelas e portas)
 */
public class PlantaBaixa {

    private Long id;
    private String descricao;
    private List<Vertice> vertices = new ArrayList<>();
    private List<Aresta> arestas = new ArrayList<>();
    private List<Comodo> comodos = new ArrayList<>();

    public PlantaBaixa() {}

    public PlantaBaixa(String descricao) {
        this.descricao = descricao;
    }

    /** Busca um vértice pelo seu identificador de negócio. */
    public Vertice buscarVertice(String verticeId) {
        return vertices.stream()
                .filter(v -> v.getId().equals(verticeId))
                .findFirst()
                .orElse(null);
    }

    /** Retorna todas as arestas (paredes) que tocam o vértice informado. */
    public List<Aresta> arestasDoVertice(String verticeId) {
        return arestas.stream()
                .filter(a -> verticeId.equals(a.getOrigemId()) || verticeId.equals(a.getDestinoId()))
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Vertice> getVertices() { return vertices; }
    public void setVertices(List<Vertice> vertices) { this.vertices = vertices; }

    public List<Aresta> getArestas() { return arestas; }
    public void setArestas(List<Aresta> arestas) { this.arestas = arestas; }

    public List<Comodo> getComodos() { return comodos; }
    public void setComodos(List<Comodo> comodos) { this.comodos = comodos; }
}