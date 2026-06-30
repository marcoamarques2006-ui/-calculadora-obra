package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.ArestaDTO;
import marco.calculadora_obra.api.dto.OrcamentoRequestDTO;
import marco.calculadora_obra.api.dto.OrcamentoResponseDTO;
import marco.calculadora_obra.domain.exception.RecursoNaoEncontradoException;
import marco.calculadora_obra.infrastructure.persistence.entity.OrcamentoEntity;
import marco.calculadora_obra.infrastructure.persistence.repository.OrcamentoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrcamentoServiceImplTest {

    private final OrcamentoRepository repository = Mockito.mock(OrcamentoRepository.class);
    // Usa as implementações reais de cálculo (não precisam de planta salva)
    private final CalculoConcreteServiceImpl concreto = new CalculoConcreteServiceImpl(null);
    private final CalculoTijolosServiceImpl tijolos = new CalculoTijolosServiceImpl(null);
    private final OrcamentoServiceImpl service =
        new OrcamentoServiceImpl(repository, concreto, tijolos);

    private OrcamentoRequestDTO requestBasico() {
        ArestaDTO parede = new ArestaDTO();
        parede.setId("P1");
        parede.setComprimento(6.0);
        parede.setEspessura(0.14);
        parede.setAlturaParede(2.8);

        OrcamentoRequestDTO req = new OrcamentoRequestDTO();
        req.setNomeUsuario("Marco");
        req.setDescricao("Casa teste");
        req.setParedes(new java.util.ArrayList<>(List.of(parede)));
        req.setAlturaViga(0.40);
        req.setAlturaTijolo(0.057);
        req.setComprimentoTijolo(0.19);
        req.setPrecoConcretoM3(450.0);
        req.setPrecoTijolo(1.20);
        req.setMargemLucroPercentual(20.0);
        return req;
    }

    @Test
    void gerar_calculaCustosEMargem_ePersiste() {
        Mockito.when(repository.save(Mockito.any(OrcamentoEntity.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        OrcamentoResponseDTO resp = service.gerar(requestBasico());

        // concreto: 0.14*0.40*6.0 = 0.336 m³ -> custo 0.336*450 = 151.20
        assertEquals(0.336, resp.getVolumeConcretoM3(), 1e-9);
        assertEquals(151.20, resp.getCustoConcreto(), 1e-9);

        // tijolos: área 16.8 -> 1254 tijolos -> com perda ceil(1254*1.10)=1380 -> custo 1380*1.20=1656.00
        assertEquals(1254, resp.getQuantidadeTijolos());
        assertEquals(1380, resp.getQuantidadeTijolosComPerda());
        assertEquals(1656.00, resp.getCustoTijolos(), 1e-9);

        // materiais = 151.20 + 1656.00 = 1807.20 ; com 20% = 2168.64
        assertEquals(1807.20, resp.getCustoTotalMateriais(), 1e-9);
        assertEquals(2168.64, resp.getValorFinalComMargem(), 1e-9);

        // 1 item persistido
        assertEquals(1, resp.getItens().size());
        Mockito.verify(repository).save(Mockito.any(OrcamentoEntity.class));
    }

    @Test
    void gerar_persisteNomeEItens() {
        Mockito.when(repository.save(Mockito.any(OrcamentoEntity.class)))
            .thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<OrcamentoEntity> captor = ArgumentCaptor.forClass(OrcamentoEntity.class);

        service.gerar(requestBasico());

        Mockito.verify(repository).save(captor.capture());
        OrcamentoEntity salvo = captor.getValue();
        assertEquals("Marco", salvo.getNomeUsuario());
        assertNotNull(salvo.getDataCriacao());
        assertEquals(1, salvo.getItens().size());
        assertSame(salvo, salvo.getItens().get(0).getOrcamento());
    }

    @Test
    void gerar_erroQuandoNomeVazio() {
        OrcamentoRequestDTO req = requestBasico();
        req.setNomeUsuario("  ");
        assertThrows(IllegalArgumentException.class, () -> service.gerar(req));
    }

    @Test
    void gerar_erroQuandoSemParedes() {
        OrcamentoRequestDTO req = requestBasico();
        req.setParedes(new java.util.ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> service.gerar(req));
    }

    @Test
    void buscarPorNumero_lancaQuandoNaoExiste() {
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorNumero(99L));
    }

    @Test
    void buscarPorNomeUsuario_delegaAoRepositorio() {
        Mockito.when(repository.findByNomeUsuarioContainingIgnoreCaseOrderByDataCriacaoDesc("Marco"))
            .thenReturn(List.of(new OrcamentoEntity()));
        assertEquals(1, service.buscarPorNomeUsuario("Marco").size());
    }
}
