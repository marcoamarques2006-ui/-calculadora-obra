package marco.calculadora_obra.domain.service;

import marco.calculadora_obra.api.dto.QuantidadeTijolosRequestDTO;
import marco.calculadora_obra.api.dto.QuantidadeTijolosResponseDTO;

/**
 * Contrato para cálculo de quantidade de tijolos das paredes.
 * Princípio I: interface separada, não misturada com cálculo de concreto.
 * Princípio D: módulos de alto nível dependem desta abstração.
 */
public interface CalculoTijolosService {
    QuantidadeTijolosResponseDTO calcularQuantidadeTijolos(QuantidadeTijolosRequestDTO request);
}
