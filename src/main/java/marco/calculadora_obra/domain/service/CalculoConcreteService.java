package marco.calculadora_obra.domain.service;

import marco.calculadora_obra.api.dto.VolumeConcretoRequestDTO;
import marco.calculadora_obra.api.dto.VolumeConcretoResponseDTO;

public interface CalculoConcreteService {
    VolumeConcretoResponseDTO calcularVolumeConcreto(VolumeConcretoRequestDTO request);
}