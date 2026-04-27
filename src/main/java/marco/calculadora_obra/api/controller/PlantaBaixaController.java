package marco.calculadora_obra.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import marco.calculadora_obra.api.dto.PlantaBaixaRequestDTO;
import marco.calculadora_obra.api.dto.PlantaBaixaResponseDTO;
import marco.calculadora_obra.domain.service.PlantaBaixaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planta-baixa")
@Tag(name = "Planta Baixa", description = "Modelagem da planta como grafo G=(V,A)")
public class PlantaBaixaController {

    private final PlantaBaixaService plantaBaixaService;

    public PlantaBaixaController(PlantaBaixaService plantaBaixaService) {
        this.plantaBaixaService = plantaBaixaService;
    }

    @PostMapping
    @Operation(
        summary = "Salvar planta baixa",
        description = "Recebe o grafo G=(V,A) completo: vértices (pilares), arestas (paredes) e cômodos. " +
                      "Persiste tudo no banco e retorna o ID gerado para uso nos cálculos."
    )
    public ResponseEntity<PlantaBaixaResponseDTO> salvar(
            @Valid @RequestBody PlantaBaixaRequestDTO request) {
        return ResponseEntity.ok(plantaBaixaService.salvar(request));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar planta baixa",
        description = "Retorna a planta baixa salva com todos os vértices, arestas e cômodos."
    )
    public ResponseEntity<PlantaBaixaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(plantaBaixaService.buscar(id));
    }
}