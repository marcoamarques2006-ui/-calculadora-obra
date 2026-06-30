package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.AberturaDTO;
import marco.calculadora_obra.api.dto.ArestaDTO;
import marco.calculadora_obra.api.dto.QuantidadeTijolosRequestDTO;
import marco.calculadora_obra.api.dto.QuantidadeTijolosResponseDTO;
import marco.calculadora_obra.domain.service.PlantaBaixaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculoTijolosServiceImplTest {

    private final PlantaBaixaService plantaBaixaService = Mockito.mock(PlantaBaixaService.class);
    private final CalculoTijolosServiceImpl service = new CalculoTijolosServiceImpl(plantaBaixaService);

    private ArestaDTO aresta(String id, double comprimento, double alturaParede) {
        ArestaDTO dto = new ArestaDTO();
        dto.setId(id);
        dto.setComprimento(comprimento);
        dto.setEspessura(0.14);
        dto.setAlturaParede(alturaParede);
        return dto;
    }

    private QuantidadeTijolosRequestDTO request(List<ArestaDTO> arestas) {
        QuantidadeTijolosRequestDTO req = new QuantidadeTijolosRequestDTO();
        req.setArestas(arestas);
        req.setAlturaTijolo(0.057);
        req.setComprimentoTijolo(0.19);
        return req;
    }

    /** tijolos/m² = 1 / ((0.057+0.01)*(0.19+0.01)) = 1 / (0.067*0.20) = 74.6268... */
    @Test
    void calcula_tijolos_paredeSemAberturas() {
        // area liquida = 6.0 * 2.8 = 16.8 ; 16.8 * 74.6268 = 1253.7 -> ceil 1254
        QuantidadeTijolosResponseDTO resp = service.calcularQuantidadeTijolos(
            request(List.of(aresta("P1", 6.0, 2.8))));

        assertEquals(1254, resp.getQuantidadeTotalTijolos());
        assertEquals(16.8, resp.getAreaTotalLiquida(), 1e-9);
    }

    @Test
    void aplica_fatorDePerdaDe10Porcento() {
        QuantidadeTijolosResponseDTO resp = service.calcularQuantidadeTijolos(
            request(List.of(aresta("P1", 6.0, 2.8))));

        // ceil(1254 * 1.10) = ceil(1379.4) = 1380
        assertEquals(1380, resp.getQuantidadeComPerda());
    }

    @Test
    void desconta_aberturas_daAreaLiquida() {
        ArestaDTO a = aresta("P1", 6.0, 2.8);
        a.setTemPorta(true);
        a.setPorta(new AberturaDTO(2.1, 0.9)); // 1.89
        QuantidadeTijolosResponseDTO resp = service.calcularQuantidadeTijolos(request(List.of(a)));

        assertEquals(16.8 - 1.89, resp.getAreaTotalLiquida(), 1e-9);
    }

    @Test
    void erro_quandoNaoInformaPlantaNemArestas() {
        assertThrows(IllegalArgumentException.class,
            () -> service.calcularQuantidadeTijolos(request(List.of())));
    }

    @Test
    void erro_quandoTemPortaSemDimensoes() {
        ArestaDTO a = aresta("P1", 6.0, 2.8);
        a.setTemPorta(true); // sem setPorta
        assertThrows(IllegalArgumentException.class,
            () -> service.calcularQuantidadeTijolos(request(List.of(a))));
    }

    @Test
    void usa_plantaQuandoIdInformado() {
        Mockito.when(plantaBaixaService.listarArestas(1L))
            .thenReturn(List.of(aresta("P1", 6.0, 2.8)));

        QuantidadeTijolosRequestDTO req = new QuantidadeTijolosRequestDTO();
        req.setPlantaBaixaId(1L);
        req.setAlturaTijolo(0.057);
        req.setComprimentoTijolo(0.19);

        QuantidadeTijolosResponseDTO resp = service.calcularQuantidadeTijolos(req);
        assertEquals(1254, resp.getQuantidadeTotalTijolos());
    }
}
