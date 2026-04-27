package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.service.CalculoConcreteService;
import marco.calculadora_obra.infrastructure.persistence.entity.ArestaEntity;
import marco.calculadora_obra.infrastructure.persistence.entity.PlantaBaixaEntity;
import marco.calculadora_obra.infrastructure.persistence.repository.ArestaRepository;
import marco.calculadora_obra.infrastructure.persistence.repository.PlantaBaixaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculoConcreteServiceImpl implements CalculoConcreteService {

    private final ArestaRepository arestaRepository;
    private final PlantaBaixaRepository plantaBaixaRepository;
    private final PlantaBaixaServiceImpl plantaBaixaServiceImpl;

    public CalculoConcreteServiceImpl(ArestaRepository arestaRepository,
                                       PlantaBaixaRepository plantaBaixaRepository,
                                       PlantaBaixaServiceImpl plantaBaixaServiceImpl) {
        this.arestaRepository = arestaRepository;
        this.plantaBaixaRepository = plantaBaixaRepository;
        this.plantaBaixaServiceImpl = plantaBaixaServiceImpl;
    }

    @Override
    public VolumeConcreteResponseDTO calcularVolumeConcreto(VolumeConcreteRequestDTO request) {
        List<ArestaDTO> arestas = resolverArestas(request.getPlantaBaixaId(), request.getArestas());

        List<DetalheVigaDTO> detalhes = new ArrayList<>();
        double volumeTotal = 0;

        for (ArestaDTO dto : arestas) {
            if (request.getPlantaBaixaId() == null) {
                salvarAresta(dto);
            }

            double volume = dto.getEspessura() * request.getAlturaViga() * dto.getComprimento();
            volumeTotal += volume;

            detalhes.add(new DetalheVigaDTO(
                dto.getId(),
                arredondar(dto.getComprimento()),
                arredondar(dto.getEspessura()),
                arredondar(request.getAlturaViga()),
                arredondar(volume)
            ));
        }

        return new VolumeConcreteResponseDTO(
            arredondar(volumeTotal),
            arestas.size(),
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

    private void salvarAresta(ArestaDTO dto) {
        ArestaEntity entity = new ArestaEntity();
        entity.setArestaId(dto.getId());
        entity.setOrigemId(dto.getOrigemId());
        entity.setDestinoId(dto.getDestinoId());
        entity.setComprimento(dto.getComprimento());
        entity.setEspessura(dto.getEspessura());
        entity.setAlturaParede(dto.getAlturaParede());
        entity.setTemJanela(dto.isTemJanela());
        entity.setTemPorta(dto.isTemPorta());

        if (dto.isTemJanela() && dto.getJanela() != null) {
            entity.setAlturaJanela(dto.getJanela().getAltura());
            entity.setComprimentoJanela(dto.getJanela().getComprimento());
        }
        if (dto.isTemPorta() && dto.getPorta() != null) {
            entity.setAlturaPorta(dto.getPorta().getAltura());
            entity.setComprimentoPorta(dto.getPorta().getComprimento());
        }
        arestaRepository.save(entity);
    }

    private double arredondar(double valor) {
        return Math.round(valor * 1000.0) / 1000.0;
    }
}