package marco.calculadora_obra.domain.service;

import marco.calculadora_obra.api.dto.OrcamentoRequestDTO;
import marco.calculadora_obra.api.dto.OrcamentoResponseDTO;

import java.util.List;

public interface OrcamentoService {

    /** Gera, calcula e persiste um novo orçamento, retornando o número gerado e os totais. */
    OrcamentoResponseDTO gerar(OrcamentoRequestDTO request);

    /** Busca um orçamento pelo número (identificador). */
    OrcamentoResponseDTO buscarPorNumero(Long numero);

    /** Busca orçamentos pelo nome do usuário (parcial, ignora maiúsculas/minúsculas). */
    List<OrcamentoResponseDTO> buscarPorNomeUsuario(String nomeUsuario);

    /** Lista todos os orçamentos. */
    List<OrcamentoResponseDTO> listarTodos();
}
