package marco.calculadora_obra.domain.model;

/**
 * Representa uma aresta do grafo G=(V,A) — ou seja, uma parede da planta baixa.
 * Princípio S: responsável apenas pelos dados e cálculos de área de uma parede.
 */
public class Aresta {

    private String id;
    private String origemId;
    private String destinoId;
    private double comprimento;
    private double espessura;
    private double alturaParede;

    private boolean temJanela;
    private boolean temPorta;
    private Abertura janela;
    private Abertura porta;

    public Aresta() {}

    public Aresta(String id, String origemId, String destinoId, double comprimento, double espessura, double alturaParede) {
        this.id = id;
        this.origemId = origemId;
        this.destinoId = destinoId;
        this.comprimento = comprimento;
        this.espessura = espessura;
        this.alturaParede = alturaParede;
    }

    /** Área bruta da parede (sem descontar aberturas) */
    public double getAreaBruta() {
        return comprimento * alturaParede;
    }

    /** Área total das aberturas (janelas + portas) */
    public double getAreaAberturas() {
        double area = 0;
        if (temJanela && janela != null) area += janela.getArea();
        if (temPorta  && porta  != null) area += porta.getArea();
        return area;
    }

    /** Área líquida (descontando aberturas) */
    public double getAreaLiquida() {
        return Math.max(0, getAreaBruta() - getAreaAberturas());
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrigemId() { return origemId; }
    public void setOrigemId(String origemId) { this.origemId = origemId; }

    public String getDestinoId() { return destinoId; }
    public void setDestinoId(String destinoId) { this.destinoId = destinoId; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getEspessura() { return espessura; }
    public void setEspessura(double espessura) { this.espessura = espessura; }

    public double getAlturaParede() { return alturaParede; }
    public void setAlturaParede(double alturaParede) { this.alturaParede = alturaParede; }

    public boolean isTemJanela() { return temJanela; }
    public void setTemJanela(boolean temJanela) { this.temJanela = temJanela; }

    public boolean isTemPorta() { return temPorta; }
    public void setTemPorta(boolean temPorta) { this.temPorta = temPorta; }

    public Abertura getJanela() { return janela; }
    public void setJanela(Abertura janela) { this.janela = janela; }

    public Abertura getPorta() { return porta; }
    public void setPorta(Abertura porta) { this.porta = porta; }
}
