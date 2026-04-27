package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.service.CalculoTijolosService;
import marco.calculadora_obra.infrastructure.persistence.repository.ArestaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do cálculo de quantidade de tijolos por parede.
 *
 * Fórmula:
 *   Área líquida   = (comprimento × altura) - área de aberturas (janelas/portas)
 *   Tijolos por m² = 1 / ((alturaTijolo + 0.01) × (comprimentoTijolo + 0.01))
 *   Total           = ceil(área líquida × tijolos/m²) com 10% de perda
 *
 * Princípio S: responsável apenas pelo cálculo de tijolos.
 * Princípio I: implementa apenas CalculoTijolosService (não mistura com concreto).
 */
@Service
public class CalculoTijolosServiceImpl implements CalculoTijolosService {

    // 1 cm de junta de argamassa
    private static final double ESPESSURA_JUNTA = 0.01;
    // 10% de perda por quebra
    private static final double FATOR_PERDA = 1.10;

    private final ArestaRepository arestaRepository;

    public CalculoTijolosServiceImpl(ArestaRepository arestaRepository) {
        this.arestaRepository = arestaRepository;
    }

    @Override
    public QuantidadeTijolosResponseDTO calcularQuantidadeTijolos(QuantidadeTijolosRequestDTO request) {

        // Área da face visível do tijolo com junta de argamassa
        double areaFaceTijolo =
            (request.getAlturaTijolo() + ESPESSURA_JUNTA) *
            (request.getComprimentoTijolo() + ESPESSURA_JUNTA);

        double tijolosPorM2 = 1.0 / areaFaceTijolo;

        List<DetalheParedeDTO> detalhes = new ArrayList<>();
        int totalTijolos = 0;
        double areaTotalLiquida = 0;

        for (ArestaDTO dto : request.getArestas()) {
            double areaParede    = dto.getComprimento() * dto.getAlturaParede();
            double areaAberturas = calcularAreaAberturas(dto);
            double areaLiquida   = Math.max(0, areaParede - areaAberturas);

            int tijolosParede = (int) Math.ceil(areaLiquida * tijolosPorM2);
            totalTijolos    += tijolosParede;
            areaTotalLiquida += areaLiquida;

            detalhes.add(new DetalheParedeDTO(
                dto.getId(),
                arredondar(areaParede),
                arredondar(areaAberturas),
                arredondar(areaLiquida),
                tijolosParede
            ));
        }

        int totalComPerda = (int) Math.ceil(totalTijolos * FATOR_PERDA);

        return new QuantidadeTijolosResponseDTO(
            totalTijolos,
            totalComPerda,
            arredondar(areaTotalLiquida),
            detalhes
        );
    }

    private double calcularAreaAberturas(ArestaDTO dto) {
        double area = 0;
        if (dto.isTemJanela() && dto.getJanela() != null) {
            area += dto.getJanela().getAltura() * dto.getJanela().getComprimento();
        }
        if (dto.isTemPorta() && dto.getPorta() != null) {
            area += dto.getPorta().getAltura() * dto.getPorta().getComprimento();
        }
        return area;
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
