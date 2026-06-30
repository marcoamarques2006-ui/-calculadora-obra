package marco.calculadora_obra.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import marco.calculadora_obra.api.dto.OrcamentoRequestDTO;
import marco.calculadora_obra.api.dto.OrcamentoResponseDTO;
import marco.calculadora_obra.domain.service.OrcamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orcamentos")
@Tag(name = "Orçamentos", description = "Geração, persistência e consulta de orçamentos de obra")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @PostMapping
    @Operation(summary = "Gerar orçamento",
        description = "Recebe a planta (paredes), parâmetros de cálculo e preços, calcula os materiais " +
                      "e o valor final com margem, persiste e retorna o número do orçamento.")
    public ResponseEntity<OrcamentoResponseDTO> gerar(@Valid @RequestBody OrcamentoRequestDTO request) {
        return ResponseEntity.ok(orcamentoService.gerar(request));
    }

    @GetMapping("/{numero}")
    @Operation(summary = "Buscar orçamento por número")
    public ResponseEntity<OrcamentoResponseDTO> buscarPorNumero(@PathVariable Long numero) {
        return ResponseEntity.ok(orcamentoService.buscarPorNumero(numero));
    }

    @GetMapping
    @Operation(summary = "Listar/buscar orçamentos",
        description = "Sem parâmetro lista todos; com 'nome' filtra pelo nome do usuário.")
    public ResponseEntity<List<OrcamentoResponseDTO>> buscar(
            @RequestParam(name = "nome", required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(orcamentoService.buscarPorNomeUsuario(nome));
        }
        return ResponseEntity.ok(orcamentoService.listarTodos());
    }
}
