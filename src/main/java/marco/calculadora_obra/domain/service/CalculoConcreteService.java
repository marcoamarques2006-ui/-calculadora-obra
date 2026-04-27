package marco.calculadora_obra.domain.service;

import marco.calculadora_obra.api.dto.VolumeConcreteRequestDTO;
import marco.calculadora_obra.api.dto.VolumeConcreteResponseDTO;

/**
 * Contrato para cálculo de volume de concreto das vigas baldrame.
 * Princípio I: interface específica e focada apenas neste cálculo.
 * Princípio D: módulos de alto nível dependem desta abstração.
 */
public interface CalculoConcreteService {
    VolumeConcreteResponseDTO calcularVolumeConcreto(VolumeConcreteRequestDTO request);
}
