package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.service.CalculoTijolosService;
import marco.calculadora_obra.infrastructure.persistence.entity.PlantaBaixaEntity;
import marco.calculadora_obra.infrastructure.persistence.repository.ArestaRepository;
import marco.calculadora_obra.infrastructure.persistence.repository.PlantaBaixaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculoTijolosServiceImpl implements CalculoTijolosService {

    private static final double ESPESSURA_JUNTA = 0.01;
    private static final double FATOR_PERDA = 1.10;

    private final ArestaRepository arestaRepository;
    private final PlantaBaixaRepository plantaBaixaRepository;
    private final PlantaBaixaServiceImpl plantaBaixaServiceImpl;

    public CalculoTijolosServiceImpl(ArestaRepository arestaRepository,
                                      PlantaBaixaRepository plantaBaixaRepository,
                                      PlantaBaixaServiceImpl plantaBaixaServiceImpl) {
        this.arestaRepository = arestaRepository;
        this.plantaBaixaRepository = plantaBaixaRepository;
        this.plantaBaixaServiceImpl = plantaBaixaServiceImpl;
    }

    @Override
    public QuantidadeTijolosResponseDTO calcularQuantidadeTijolos(QuantidadeTijolosRequestDTO request) {
        List<ArestaDTO> arestas = resolverArestas(request.getPlantaBaixaId(), request.getArestas());

        double areaFaceTijolo =
            (request.getAlturaTijolo() + ESPESSURA_JUNTA) *
            (request.getComprimentoTijolo() + ESPESSURA_JUNTA);
        double tijolosPorM2 = 1.0 / areaFaceTijolo;

        List<DetalheParedeDTO> detalhes = new ArrayList<>();
        int totalTijolos = 0;
        double areaTotalLiquida = 0;

        for (ArestaDTO dto : arestas) {
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

    private List<ArestaDTO> resolverArestas(Long plantaBaixaId, List<ArestaDTO> arestasDirectas) {
        if (plantaBaixaId != null) {
            PlantaBaixaEntity planta = plantaBaixaRepository.findById(plantaBaixaId)
                    .orElseThrow(() -> new RuntimeException("Planta baixa não encontrada: " + plantaBaixaId));
            return planta.getArestas().stream()
                    .map(plantaBaixaServiceImpl::toArestaDTO)
                    .collect(Collectors.toList());
        }
        if (arestasDirectas == null || arestasDirectas.isEmpty()) {
            throw new IllegalArgumentException("Informe plantaBaixaId ou uma lista de arestas");
        }
        return arestasDirectas;
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