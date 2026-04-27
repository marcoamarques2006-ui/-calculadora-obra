package marco.calculadora_obra.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import marco.calculadora_obra.api.dto.QuantidadeTijolosRequestDTO;
import marco.calculadora_obra.api.dto.QuantidadeTijolosResponseDTO;
import marco.calculadora_obra.domain.service.CalculoTijolosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para cálculos de paredes (quantidade de tijolos).
 * Princípio D: depende da interface CalculoTijolosService, não da implementação concreta.
 */
@RestController
@RequestMapping("/api/paredes")
@Tag(name = "Paredes", description = "Cálculo de quantidade de tijolos por parede")
public class ParedeController {

    private final CalculoTijolosService calculoTijolosService;

    public ParedeController(CalculoTijolosService calculoTijolosService) {
        this.calculoTijolosService = calculoTijolosService;
    }

    @PostMapping("/quantidade-tijolos")
    @Operation(
        summary = "Calcular quantidade de tijolos",
        description = "Recebe lista de arestas e dimensões do tijolo. " +
                      "Desconta áreas de janelas e portas. Inclui 10% de perda por quebra."
    )
    public ResponseEntity<QuantidadeTijolosResponseDTO> calcularQuantidadeTijolos(
            @Valid @RequestBody QuantidadeTijolosRequestDTO request) {

        return ResponseEntity.ok(calculoTijolosService.calcularQuantidadeTijolos(request));
    }
}
