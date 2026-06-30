package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.*;
import marco.calculadora_obra.domain.exception.RecursoNaoEncontradoException;
import marco.calculadora_obra.domain.service.CalculoConcreteService;
import marco.calculadora_obra.domain.service.CalculoTijolosService;
import marco.calculadora_obra.domain.service.OrcamentoService;
import marco.calculadora_obra.infrastructure.persistence.entity.OrcamentoEntity;
import marco.calculadora_obra.infrastructure.persistence.entity.OrcamentoItemEntity;
import marco.calculadora_obra.infrastructure.persistence.repository.OrcamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrcamentoServiceImpl implements OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final CalculoConcreteService calculoConcreteService;
    private final CalculoTijolosService calculoTijolosService;

    public OrcamentoServiceImpl(OrcamentoRepository orcamentoRepository,
                                CalculoConcreteService calculoConcreteService,
                                CalculoTijolosService calculoTijolosService) {
        this.orcamentoRepository = orcamentoRepository;
        this.calculoConcreteService = calculoConcreteService;
        this.calculoTijolosService = calculoTijolosService;
    }

    @Override
    @Transactional
    public OrcamentoResponseDTO gerar(OrcamentoRequestDTO request) {
        validar(request);
        garantirIds(request.getParedes());

        // Reutiliza os serviços de cálculo já existentes (DRY)
        VolumeConcretoResponseDTO concreto = calculoConcreteService.calcularVolumeConcreto(
            montarRequisicaoConcreto(request));
        QuantidadeTijolosResponseDTO tijolos = calculoTijolosService.calcularQuantidadeTijolos(
            montarRequisicaoTijolos(request));

        // Composição de custos
        double custoConcreto = arredondar2(concreto.getVolumeTotalM3() * request.getPrecoConcretoM3());
        double custoTijolos  = arredondar2(tijolos.getQuantidadeComPerda() * request.getPrecoTijolo());
        double custoMateriais = arredondar2(custoConcreto + custoTijolos);
        double valorFinal = arredondar2(custoMateriais * (1 + request.getMargemLucroPercentual() / 100.0));

        OrcamentoEntity entity = new OrcamentoEntity();
        entity.setNomeUsuario(request.getNomeUsuario());
        entity.setDescricao(request.getDescricao());
        entity.setDataCriacao(LocalDateTime.now());
        entity.setAlturaViga(request.getAlturaViga());
        entity.setAlturaTijolo(request.getAlturaTijolo());
        entity.setComprimentoTijolo(request.getComprimentoTijolo());
        entity.setPrecoConcretoM3(request.getPrecoConcretoM3());
        entity.setPrecoTijolo(request.getPrecoTijolo());
        entity.setMargemLucroPercentual(request.getMargemLucroPercentual());
        entity.setVolumeConcretoM3(concreto.getVolumeTotalM3());
        entity.setQuantidadeTijolos(tijolos.getQuantidadeTotalTijolos());
        entity.setQuantidadeTijolosComPerda(tijolos.getQuantidadeComPerda());
        entity.setAreaTotalLiquida(tijolos.getAreaTotalLiquida());
        entity.setCustoConcreto(custoConcreto);
        entity.setCustoTijolos(custoTijolos);
        entity.setCustoTotalMateriais(custoMateriais);
        entity.setValorFinalComMargem(valorFinal);

        // Itens por parede combinando volume (concreto) e área/tijolos (paredes)
        Map<String, DetalheVigaDTO> volumePorParede = concreto.getDetalhes().stream()
            .collect(Collectors.toMap(DetalheVigaDTO::getArestaId, d -> d, (a, b) -> a));
        Map<String, DetalheParedeDTO> paredePorId = tijolos.getDetalhes().stream()
            .collect(Collectors.toMap(DetalheParedeDTO::getArestaId, d -> d, (a, b) -> a));

        for (ArestaDTO parede : request.getParedes()) {
            DetalheVigaDTO dv = volumePorParede.get(parede.getId());
            DetalheParedeDTO dp = paredePorId.get(parede.getId());

            OrcamentoItemEntity item = new OrcamentoItemEntity();
            item.setParedeId(parede.getId());
            item.setComprimento(parede.getComprimento());
            item.setEspessura(parede.getEspessura());
            item.setAlturaParede(parede.getAlturaParede());
            item.setAreaLiquida(dp != null ? dp.getAreaLiquida() : 0);
            item.setVolumeConcreto(dv != null ? dv.getVolume() : 0);
            item.setQuantidadeTijolos(dp != null ? dp.getQuantidadeTijolos() : 0);
            entity.addItem(item);
        }

        return toResponse(orcamentoRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public OrcamentoResponseDTO buscarPorNumero(Long numero) {
        return orcamentoRepository.findById(numero)
            .map(this::toResponse)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado: " + numero));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrcamentoResponseDTO> buscarPorNomeUsuario(String nomeUsuario) {
        return orcamentoRepository
            .findByNomeUsuarioContainingIgnoreCaseOrderByDataCriacaoDesc(
                nomeUsuario == null ? "" : nomeUsuario)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrcamentoResponseDTO> listarTodos() {
        return orcamentoRepository.findAll().stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    // ---------- helpers ----------

    private void validar(OrcamentoRequestDTO request) {
        if (request.getNomeUsuario() == null || request.getNomeUsuario().isBlank()) {
            throw new IllegalArgumentException("Nome do usuário é obrigatório");
        }
        if (request.getParedes() == null || request.getParedes().isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos uma parede da planta");
        }
        if (request.getAlturaViga() <= 0) {
            throw new IllegalArgumentException("Altura da viga baldrame deve ser positiva");
        }
        if (request.getAlturaTijolo() <= 0 || request.getComprimentoTijolo() <= 0) {
            throw new IllegalArgumentException("Dimensões do tijolo devem ser positivas");
        }
        if (request.getPrecoConcretoM3() < 0 || request.getPrecoTijolo() < 0
                || request.getMargemLucroPercentual() < 0) {
            throw new IllegalArgumentException("Preços e margem não podem ser negativos");
        }
    }

    /** Garante que cada parede tenha um id único (o formulário web pode não informar). */
    private void garantirIds(List<ArestaDTO> paredes) {
        int i = 1;
        for (ArestaDTO p : paredes) {
            if (p.getId() == null || p.getId().isBlank()) {
                p.setId("P" + i);
            }
            i++;
        }
    }

    private VolumeConcretoRequestDTO montarRequisicaoConcreto(OrcamentoRequestDTO request) {
        VolumeConcretoRequestDTO dto = new VolumeConcretoRequestDTO();
        dto.setArestas(request.getParedes());
        dto.setAlturaViga(request.getAlturaViga());
        return dto;
    }

    private QuantidadeTijolosRequestDTO montarRequisicaoTijolos(OrcamentoRequestDTO request) {
        QuantidadeTijolosRequestDTO dto = new QuantidadeTijolosRequestDTO();
        dto.setArestas(request.getParedes());
        dto.setAlturaTijolo(request.getAlturaTijolo());
        dto.setComprimentoTijolo(request.getComprimentoTijolo());
        return dto;
    }

    private OrcamentoResponseDTO toResponse(OrcamentoEntity e) {
        OrcamentoResponseDTO dto = new OrcamentoResponseDTO();
        dto.setNumero(e.getNumero());
        dto.setNomeUsuario(e.getNomeUsuario());
        dto.setDescricao(e.getDescricao());
        dto.setDataCriacao(e.getDataCriacao());
        dto.setVolumeConcretoM3(e.getVolumeConcretoM3());
        dto.setQuantidadeTijolos(e.getQuantidadeTijolos());
        dto.setQuantidadeTijolosComPerda(e.getQuantidadeTijolosComPerda());
        dto.setAreaTotalLiquida(e.getAreaTotalLiquida());
        dto.setCustoConcreto(e.getCustoConcreto());
        dto.setCustoTijolos(e.getCustoTijolos());
        dto.setCustoTotalMateriais(e.getCustoTotalMateriais());
        dto.setMargemLucroPercentual(e.getMargemLucroPercentual());
        dto.setValorFinalComMargem(e.getValorFinalComMargem());

        dto.setItens(e.getItens().stream().map(it -> {
            OrcamentoItemDTO i = new OrcamentoItemDTO();
            i.setParedeId(it.getParedeId());
            i.setComprimento(it.getComprimento());
            i.setEspessura(it.getEspessura());
            i.setAlturaParede(it.getAlturaParede());
            i.setAreaLiquida(it.getAreaLiquida());
            i.setVolumeConcreto(it.getVolumeConcreto());
            i.setQuantidadeTijolos(it.getQuantidadeTijolos());
            return i;
        }).collect(Collectors.toList()));

        return dto;
    }

    private double arredondar2(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
