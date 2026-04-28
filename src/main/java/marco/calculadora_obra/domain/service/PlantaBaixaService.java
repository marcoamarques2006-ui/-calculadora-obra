package marco.calculadora_obra.domain.service;

import marco.calculadora_obra.api.dto.ArestaDTO;
import marco.calculadora_obra.api.dto.PlantaBaixaRequestDTO;
import marco.calculadora_obra.api.dto.PlantaBaixaResponseDTO;

import java.util.List;

public interface PlantaBaixaService {
    PlantaBaixaResponseDTO salvar(PlantaBaixaRequestDTO request);
    PlantaBaixaResponseDTO buscar(Long id);
    List<ArestaDTO> listarArestas(Long plantaBaixaId);
}