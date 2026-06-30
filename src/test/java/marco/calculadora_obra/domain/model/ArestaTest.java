package marco.calculadora_obra.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArestaTest {

    private static final double DELTA = 1e-9;

    @Test
    void areaBruta_ehComprimentoVezesAltura() {
        Aresta a = new Aresta("P1", "V1", "V2", 6.0, 0.14, 2.8);
        assertEquals(16.8, a.getAreaBruta(), DELTA);
    }

    @Test
    void areaAberturas_zeroQuandoNaoTemAberturas() {
        Aresta a = new Aresta("P1", "V1", "V2", 6.0, 0.14, 2.8);
        assertEquals(0.0, a.getAreaAberturas(), DELTA);
    }

    @Test
    void areaAberturas_somaJanelaEPorta() {
        Aresta a = new Aresta("P1", "V1", "V2", 6.0, 0.14, 2.8);
        a.setTemJanela(true);
        a.setJanela(new Abertura(1.2, 1.5)); // 1.8
        a.setTemPorta(true);
        a.setPorta(new Abertura(2.1, 0.9));  // 1.89
        assertEquals(3.69, a.getAreaAberturas(), DELTA);
    }

    @Test
    void areaLiquida_descontaAberturas() {
        Aresta a = new Aresta("P1", "V1", "V2", 6.0, 0.14, 2.8);
        a.setTemPorta(true);
        a.setPorta(new Abertura(2.1, 0.9)); // 1.89
        assertEquals(16.8 - 1.89, a.getAreaLiquida(), DELTA);
    }

    @Test
    void areaLiquida_nuncaNegativa() {
        Aresta a = new Aresta("P1", "V1", "V2", 1.0, 0.14, 1.0); // bruta 1.0
        a.setTemPorta(true);
        a.setPorta(new Abertura(2.1, 0.9)); // 1.89 > bruta
        assertEquals(0.0, a.getAreaLiquida(), DELTA);
    }

    @Test
    void abertura_areaEhAlturaVezesComprimento() {
        assertEquals(1.8, new Abertura(1.2, 1.5).getArea(), DELTA);
    }
}
