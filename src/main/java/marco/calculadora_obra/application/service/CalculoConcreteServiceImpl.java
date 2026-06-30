package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.model.Abertura;
import marco.calculadora_obra.domain.model.Aresta;
import marco.calculadora_obra.domain.service.CalculoConcreteService;
import marco.calculadora_obra.domain.service.PlantaBaixaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculoConcreteServiceImpl implements CalculoConcreteService {

    private final PlantaBaixaService plantaBaixaService;

    public CalculoConcreteServiceImpl(PlantaBaixaService plantaBaixaService) {
        this.plantaBaixaService = plantaBaixaService;
    }

    @Override
    public VolumeConcretoResponseDTO calcularVolumeConcreto(VolumeConcretoRequestDTO request) {
        List<ArestaDTO> arestaDTOs = resolverArestas(request.getPlantaBaixaId(), request.getArestas());

        List<DetalheVigaDTO> detalhes = new ArrayList<>();
        double volumeTotal = 0;

        for (ArestaDTO dto : arestaDTOs) {
            Aresta aresta = toAresta(dto);

            // Volume da viga: largura (espessura da parede) × altura (informada) × comprimento
            double volume = aresta.getEspessura() * request.getAlturaViga() * aresta.getComprimento();
            volumeTotal += volume;

            detalhes.add(new DetalheVigaDTO(
                aresta.getId(),
                arredondar(aresta.getComprimento()),
                arredondar(aresta.getEspessura()),
                arredondar(request.getAlturaViga()),
                arredondar(volume)
            ));
        }

        return new VolumeConcretoResponseDTO(arredondar(volumeTotal), arestaDTOs.size(), detalhes);
    }

    private List<ArestaDTO> resolverArestas(Long plantaBaixaId, List<ArestaDTO> arestasDirectas) {
        if (plantaBaixaId != null) {
            return plantaBaixaService.listarArestas(plantaBaixaId);
        }
        if (arestasDirectas == null || arestasDirectas.isEmpty()) {
            throw new IllegalArgumentException("Informe plantaBaixaId ou uma lista de arestas");
        }
        return arestasDirectas;
    }

    private Aresta toAresta(ArestaDTO dto) {
        Aresta aresta = new Aresta(
            dto.getId(), dto.getOrigemId(), dto.getDestinoId(),
            dto.getComprimento(), dto.getEspessura(), dto.getAlturaParede()
        );
        if (dto.isTemJanela()) {
            if (dto.getJanela() == null) {
                throw new IllegalArgumentException(
                    "Aresta '" + dto.getId() + "' tem temJanela=true mas não informou as dimensões da janela");
            }
            aresta.setTemJanela(true);
            aresta.setJanela(new Abertura(dto.getJanela().getAltura(), dto.getJanela().getComprimento()));
        }
        if (dto.isTemPorta()) {
            if (dto.getPorta() == null) {
                throw new IllegalArgumentException(
                    "Aresta '" + dto.getId() + "' tem temPorta=true mas não informou as dimensões da porta");
            }
            aresta.setTemPorta(true);
            aresta.setPorta(new Abertura(dto.getPorta().getAltura(), dto.getPorta().getComprimento()));
        }
        return aresta;
    }

    private double arredondar(double valor) {
        return Math.round(valor * 1000.0) / 1000.0;
    }
}