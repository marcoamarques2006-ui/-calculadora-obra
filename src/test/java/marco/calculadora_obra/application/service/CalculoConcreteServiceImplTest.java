package marco.calculadora_obra.application.service;

import marco.calculadora_obra.api.dto.AberturaDTO;
import marco.calculadora_obra.api.dto.ArestaDTO;
import marco.calculadora_obra.api.dto.VolumeConcretoRequestDTO;
import marco.calculadora_obra.api.dto.VolumeConcretoResponseDTO;
import marco.calculadora_obra.domain.service.PlantaBaixaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculoConcreteServiceImplTest {

    private final PlantaBaixaService plantaBaixaService = Mockito.mock(PlantaBaixaService.class);
    private final CalculoConcreteServiceImpl service = new CalculoConcreteServiceImpl(plantaBaixaService);

    private ArestaDTO aresta(String id, double comprimento, double espessura) {
        ArestaDTO dto = new ArestaDTO();
        dto.setId(id);
        dto.setComprimento(comprimento);
        dto.setEspessura(espessura);
        dto.setAlturaParede(2.8);
        return dto;
    }

    @Test
    void calcula_volume_porArestaDireta() {
        VolumeConcretoRequestDTO req = new VolumeConcretoRequestDTO();
        req.setArestas(List.of(aresta("P1", 6.0, 0.14)));
        req.setAlturaViga(0.40);

        VolumeConcretoResponseDTO resp = service.calcularVolumeConcreto(req);

        // 0.14 * 0.40 * 6.0 = 0.336
        assertEquals(0.336, resp.getVolumeTotalM3(), 1e-9);
        assertEquals(1, resp.getTotalArestas());
        assertEquals(1, resp.getDetalhes().size());
    }

    @Test
    void soma_volume_deVariasArestas() {
        VolumeConcretoRequestDTO req = new VolumeConcretoRequestDTO();
        req.setArestas(List.of(aresta("P1", 6.0, 0.14), aresta("P2", 4.0, 0.14)));
        req.setAlturaViga(0.40);

        VolumeConcretoResponseDTO resp = service.calcularVolumeConcreto(req);

        // 0.336 + 0.224 = 0.560
        assertEquals(0.560, resp.getVolumeTotalM3(), 1e-9);
        assertEquals(2, resp.getTotalArestas());
    }

    @Test
    void erro_quandoNaoInformaPlantaNemArestas() {
        VolumeConcretoRequestDTO req = new VolumeConcretoRequestDTO();
        req.setAlturaViga(0.40);
        assertThrows(IllegalArgumentException.class, () -> service.calcularVolumeConcreto(req));
    }

    @Test
    void erro_quandoTemJanelaSemDimensoes() {
        ArestaDTO a = aresta("P1", 6.0, 0.14);
        a.setTemJanela(true); // sem setJanela
        VolumeConcretoRequestDTO req = new VolumeConcretoRequestDTO();
        req.setArestas(List.of(a));
        req.setAlturaViga(0.40);
        assertThrows(IllegalArgumentException.class, () -> service.calcularVolumeConcreto(req));
    }

    @Test
    void usa_plantaQuandoIdInformado() {
        ArestaDTO a = aresta("P1", 10.0, 0.20);
        AberturaDTO ignorada = new AberturaDTO(1.0, 1.0); // não afeta volume
        a.setJanela(ignorada);
        Mockito.when(plantaBaixaService.listarArestas(1L)).thenReturn(List.of(a));

        VolumeConcretoRequestDTO req = new VolumeConcretoRequestDTO();
        req.setPlantaBaixaId(1L);
        req.setAlturaViga(0.50);

        VolumeConcretoResponseDTO resp = service.calcularVolumeConcreto(req);

        // 0.20 * 0.50 * 10.0 = 1.0
        assertEquals(1.0, resp.getVolumeTotalM3(), 1e-9);
    }
}
