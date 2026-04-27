package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.service.CalculoConcreteService;
import marco.calculadora_obra.infrastructure.persistence.entity.ArestaEntity;
import marco.calculadora_obra.infrastructure.persistence.repository.ArestaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do cálculo de volume de concreto para vigas baldrame.
 *
 * Fórmula: Volume = Largura (espessura da parede) × Altura (viga) × Comprimento (aresta)
 *
 * Princípio S: responsável apenas pelo cálculo de concreto.
 * Princípio O: novas estratégias de cálculo implementam a interface sem alterar esta classe.
 * Princípio L: pode substituir CalculoConcreteService sem quebrar o comportamento esperado.
 */
@Service
public class CalculoConcreteServiceImpl implements CalculoConcreteService {

    private final ArestaRepository arestaRepository;

    // Princípio D: injeção de dependência via construtor
    public CalculoConcreteServiceImpl(ArestaRepository arestaRepository) {
        this.arestaRepository = arestaRepository;
    }

    @Override
    public VolumeConcreteResponseDTO calcularVolumeConcreto(VolumeConcreteRequestDTO request) {
        List<DetalheVigaDTO> detalhes = new ArrayList<>();
        double volumeTotal = 0;

        for (ArestaDTO dto : request.getArestas()) {
            salvarAresta(dto);

            // Largura da viga  = espessura da parede
            // Comprimento da viga = comprimento da parede
            // Altura da viga = informada pelo usuário
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
            request.getArestas().size(),
            detalhes
        );
    }

    private void salvarAresta(ArestaDTO dto) {
        ArestaEntity entity = new ArestaEntity();
        entity.setArestaId(dto.getId());
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
