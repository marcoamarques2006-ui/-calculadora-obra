package marco.calculadora_obra.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import marco.calculadora_obra.api.dto.VolumeConcreteRequestDTO;
import marco.calculadora_obra.api.dto.VolumeConcreteResponseDTO;
import marco.calculadora_obra.domain.service.CalculoConcreteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para cálculos de fundação (viga baldrame).
 * Princípio D: depende da interface CalculoConcreteService, não da implementação concreta.
 */
@RestController
@RequestMapping("/api/fundacao")
@Tag(name = "Fundação", description = "Cálculo de volume de concreto para vigas baldrame")
public class FundacaoController {

    private final CalculoConcreteService calculoConcreteService;

    public FundacaoController(CalculoConcreteService calculoConcreteService) {
        this.calculoConcreteService = calculoConcreteService;
    }

    @PostMapping("/volume-concreto")
    @Operation(
        summary = "Calcular volume de concreto",
        description = "Recebe lista de arestas (paredes) e altura da viga baldrame. " +
                      "Fórmula: V = espessura × alturaViga × comprimento por aresta."
    )
    public ResponseEntity<VolumeConcreteResponseDTO> calcularVolumeConcreto(
            @Valid @RequestBody VolumeConcreteRequestDTO request) {

        return ResponseEntity.ok(calculoConcreteService.calcularVolumeConcreto(request));
    }
}
