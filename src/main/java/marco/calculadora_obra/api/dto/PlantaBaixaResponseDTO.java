package marco.calculadora_obra.api.dto;

import java.util.List;

public class PlantaBaixaResponseDTO {

    private Long id;
    private String descricao;
    private int totalVertices;
    private int totalArestas;
    private int totalComodos;
    private List<VerticeDTO> vertices;
    private List<ArestaDTO> arestas;
    private List<ComodoDTO> comodos;

    public PlantaBaixaResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getTotalVertices() { return totalVertices; }
    public void setTotalVertices(int totalVertices) { this.totalVertices = totalVertices; }

    public int getTotalArestas() { return totalArestas; }
    public void setTotalArestas(int totalArestas) { this.totalArestas = totalArestas; }

    public int getTotalComodos() { return totalComodos; }
    public void setTotalComodos(int totalComodos) { this.totalComodos = totalComodos; }

    public List<VerticeDTO> getVertices() { return vertices; }
    public void setVertices(List<VerticeDTO> vertices) { this.vertices = vertices; }

    public List<ArestaDTO> getArestas() { return arestas; }
    public void setArestas(List<ArestaDTO> arestas) { this.arestas = arestas; }

    public List<ComodoDTO> getComodos() { return comodos; }
    public void setComodos(List<ComodoDTO> comodos) { this.comodos = comodos; }
}