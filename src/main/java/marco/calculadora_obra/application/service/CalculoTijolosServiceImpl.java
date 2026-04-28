package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.model.Abertura;
import marco.calculadora_obra.domain.model.Aresta;
import marco.calculadora_obra.domain.service.CalculoTijolosService;
import marco.calculadora_obra.domain.service.PlantaBaixaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculoTijolosServiceImpl implements CalculoTijolosService {

    private static final double ESPESSURA_JUNTA = 0.01;
    private static final double FATOR_PERDA = 1.10;

    private final PlantaBaixaService plantaBaixaService;

    public CalculoTijolosServiceImpl(PlantaBaixaService plantaBaixaService) {
        this.plantaBaixaService = plantaBaixaService;
    }

    @Override
    public QuantidadeTijolosResponseDTO calcularQuantidadeTijolos(QuantidadeTijolosRequestDTO request) {
        List<ArestaDTO> arestaDTOs = resolverArestas(request.getPlantaBaixaId(), request.getArestas());

        double areaFaceTijolo =
            (request.getAlturaTijolo() + ESPESSURA_JUNTA) *
            (request.getComprimentoTijolo() + ESPESSURA_JUNTA);
        double tijolosPorM2 = 1.0 / areaFaceTijolo;

        List<DetalheParedeDTO> detalhes = new ArrayList<>();
        int totalTijolos = 0;
        double areaTotalLiquida = 0;

        for (ArestaDTO dto : arestaDTOs) {
            Aresta aresta = toAresta(dto);

            // Usa os métodos do domínio para calcular áreas
            double areaParede    = aresta.getAreaBruta();
            double areaAberturas = aresta.getAreaAberturas();
            double areaLiquida   = aresta.getAreaLiquida();

            int tijolosParede = (int) Math.ceil(areaLiquida * tijolosPorM2);
            totalTijolos     += tijolosParede;
            areaTotalLiquida += areaLiquida;

            detalhes.add(new DetalheParedeDTO(
                aresta.getId(),
                arredondar(areaParede),
                arredondar(areaAberturas),
                arredondar(areaLiquida),
                tijolosParede
            ));
        }

        int totalComPerda = (int) Math.ceil(totalTijolos * FATOR_PERDA);

        return new QuantidadeTijolosResponseDTO(
            totalTijolos, totalComPerda, arredondar(areaTotalLiquida), detalhes
        );
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
        if (dto.isTemJanela() && dto.getJanela() != null) {
            aresta.setTemJanela(true);
            aresta.setJanela(new Abertura(dto.getJanela().getAltura(), dto.getJanela().getComprimento()));
        }
        if (dto.isTemPorta() && dto.getPorta() != null) {
            aresta.setTemPorta(true);
            aresta.setPorta(new Abertura(dto.getPorta().getAltura(), dto.getPorta().getComprimento()));
        }
        return aresta;
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}