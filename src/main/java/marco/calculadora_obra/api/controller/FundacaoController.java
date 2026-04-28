package marco.calculadora_obra.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import marco.calculadora_obra.api.dto.VolumeConcretoRequestDTO;
import marco.calculadora_obra.api.dto.VolumeConcretoResponseDTO;
import marco.calculadora_obra.domain.service.CalculoConcreteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        description = "Recebe uma planta salva (plantaBaixaId) ou lista direta de arestas, mais a altura da viga. " +
                      "Fórmula: V = espessura × alturaViga × comprimento por aresta."
    )
    public ResponseEntity<VolumeConcretoResponseDTO> calcularVolumeConcreto(
            @Valid @RequestBody VolumeConcretoRequestDTO request) {
        return ResponseEntity.ok(calculoConcreteService.calcularVolumeConcreto(request));
    }
}