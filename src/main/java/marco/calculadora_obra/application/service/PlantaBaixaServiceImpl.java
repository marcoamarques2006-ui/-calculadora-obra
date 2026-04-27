package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.service.PlantaBaixaService;
import marco.calculadora_obra.infrastructure.persistence.entity.*;
import marco.calculadora_obra.infrastructure.persistence.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlantaBaixaServiceImpl implements PlantaBaixaService {

    private final PlantaBaixaRepository plantaBaixaRepository;
    private final ArestaRepository arestaRepository;

    public PlantaBaixaServiceImpl(PlantaBaixaRepository plantaBaixaRepository,
                                   ArestaRepository arestaRepository) {
        this.plantaBaixaRepository = plantaBaixaRepository;
        this.arestaRepository = arestaRepository;
    }

    @Override
    @Transactional
    public PlantaBaixaResponseDTO salvar(PlantaBaixaRequestDTO request) {
        PlantaBaixaEntity planta = new PlantaBaixaEntity();
        planta.setDescricao(request.getDescricao());

        // Salva vértices
        for (VerticeDTO vDto : request.getVertices()) {
            VerticeEntity v = new VerticeEntity();
            v.setVerticeId(vDto.getId());
            v.setX(vDto.getX());
            v.setY(vDto.getY());
            v.setPlantaBaixa(planta);
            planta.getVertices().add(v);
        }

        // Salva arestas (paredes), indexadas por arestaId para uso nos cômodos
        Map<String, ArestaEntity> arestasPorId = request.getArestas().stream()
                .collect(Collectors.toMap(ArestaDTO::getId, dto -> {
                    ArestaEntity e = toEntity(dto);
                    e.setPlantaBaixa(planta);
                    return e;
                }));
        planta.getArestas().addAll(arestasPorId.values());

        // Salva cômodos e associa suas paredes
        if (request.getComodos() != null) {
            for (ComodoDTO cDto : request.getComodos()) {
                ComodoEntity comodo = new ComodoEntity();
                comodo.setNome(cDto.getNome());
                comodo.setLargura(cDto.getLargura());
                comodo.setComprimento(cDto.getComprimento());
                comodo.setAltura(cDto.getAltura());
                comodo.setPlantaBaixa(planta);

                if (cDto.getArestaIds() != null) {
                    for (String arestaId : cDto.getArestaIds()) {
                        ArestaEntity aresta = arestasPorId.get(arestaId);
                        if (aresta != null) {
                            comodo.getParedes().add(aresta);
                        }
                    }
                }
                planta.getComodos().add(comodo);
            }
        }

        PlantaBaixaEntity salva = plantaBaixaRepository.save(planta);
        return toResponse(salva);
    }

    @Override
    public PlantaBaixaResponseDTO buscar(Long id) {
        PlantaBaixaEntity planta = plantaBaixaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Planta baixa não encontrada: " + id));
        return toResponse(planta);
    }

    private PlantaBaixaResponseDTO toResponse(PlantaBaixaEntity e) {
        PlantaBaixaResponseDTO dto = new PlantaBaixaResponseDTO();
        dto.setId(e.getPk());
        dto.setDescricao(e.getDescricao());
        dto.setTotalVertices(e.getVertices().size());
        dto.setTotalArestas(e.getArestas().size());
        dto.setTotalComodos(e.getComodos().size());

        dto.setVertices(e.getVertices().stream().map(v -> {
            VerticeDTO vDto = new VerticeDTO();
            vDto.setId(v.getVerticeId());
            vDto.setX(v.getX());
            vDto.setY(v.getY());
            return vDto;
        }).collect(Collectors.toList()));

        dto.setArestas(e.getArestas().stream().map(this::toArestaDTO).collect(Collectors.toList()));

        dto.setComodos(e.getComodos().stream().map(c -> {
            ComodoDTO cDto = new ComodoDTO();
            cDto.setNome(c.getNome());
            cDto.setLargura(c.getLargura());
            cDto.setComprimento(c.getComprimento());
            cDto.setAltura(c.getAltura());
            cDto.setArestaIds(c.getParedes().stream()
                    .map(ArestaEntity::getArestaId).collect(Collectors.toList()));
            return cDto;
        }).collect(Collectors.toList()));

        return dto;
    }

    public ArestaDTO toArestaDTO(ArestaEntity e) {
        ArestaDTO dto = new ArestaDTO();
        dto.setId(e.getArestaId());
        dto.setOrigemId(e.getOrigemId());
        dto.setDestinoId(e.getDestinoId());
        dto.setComprimento(e.getComprimento());
        dto.setEspessura(e.getEspessura());
        dto.setAlturaParede(e.getAlturaParede());
        dto.setTemJanela(e.isTemJanela());
        dto.setTemPorta(e.isTemPorta());

        if (e.isTemJanela() && e.getAlturaJanela() != null) {
            AberturaDTO j = new AberturaDTO();
            j.setAltura(e.getAlturaJanela());
            j.setComprimento(e.getComprimentoJanela());
            dto.setJanela(j);
        }
        if (e.isTemPorta() && e.getAlturaPorta() != null) {
            AberturaDTO p = new AberturaDTO();
            p.setAltura(e.getAlturaPorta());
            p.setComprimento(e.getComprimentoPorta());
            dto.setPorta(p);
        }
        return dto;
    }

    private ArestaEntity toEntity(ArestaDTO dto) {
        ArestaEntity e = new ArestaEntity();
        e.setArestaId(dto.getId());
        e.setOrigemId(dto.getOrigemId());
        e.setDestinoId(dto.getDestinoId());
        e.setComprimento(dto.getComprimento());
        e.setEspessura(dto.getEspessura());
        e.setAlturaParede(dto.getAlturaParede());
        e.setTemJanela(dto.isTemJanela());
        e.setTemPorta(dto.isTemPorta());

        if (dto.isTemJanela() && dto.getJanela() != null) {
            e.setAlturaJanela(dto.getJanela().getAltura());
            e.setComprimentoJanela(dto.getJanela().getComprimento());
        }
        if (dto.isTemPorta() && dto.getPorta() != null) {
            e.setAlturaPorta(dto.getPorta().getAltura());
            e.setComprimentoPorta(dto.getPorta().getComprimento());
        }
        return e;
    }
}