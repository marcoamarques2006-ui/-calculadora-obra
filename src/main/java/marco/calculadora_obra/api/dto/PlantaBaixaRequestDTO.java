package marco.calculadora_obra.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class PlantaBaixaRequestDTO {

    private String descricao;

    @NotEmpty(message = "A planta deve ter ao menos um vértice")
    @Valid
    private List<VerticeDTO> vertices;

    @NotEmpty(message = "A planta deve ter ao menos uma aresta (parede)")
    @Valid
    private List<ArestaDTO> arestas;

    @Valid
    private List<ComodoDTO> comodos;

    public PlantaBaixaRequestDTO() {}

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<VerticeDTO> getVertices() { return vertices; }
    public void setVertices(List<VerticeDTO> vertices) { this.vertices = vertices; }

    public List<ArestaDTO> getArestas() { return arestas; }
    public void setArestas(List<ArestaDTO> arestas) { this.arestas = arestas; }

    public List<ComodoDTO> getComodos() { return comodos; }
    public void setComodos(List<ComodoDTO> comodos) { this.comodos = comodos; }
}