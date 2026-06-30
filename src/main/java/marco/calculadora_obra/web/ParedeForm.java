package marco.calculadora_obra.web;

import java.io.Serializable;

/**
 * Linha do formulário JSF que representa uma parede da planta informada pelo usuário.
 * As dimensões de janela/porta só são consideradas quando o respectivo checkbox está marcado.
 */
public class ParedeForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private double comprimento;
    private double espessura = 0.14;
    private double alturaParede = 2.8;

    private boolean temJanela;
    private double janelaAltura = 1.2;
    private double janelaComprimento = 1.5;

    private boolean temPorta;
    private double portaAltura = 2.1;
    private double portaComprimento = 0.9;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getEspessura() { return espessura; }
    public void setEspessura(double espessura) { this.espessura = espessura; }

    public double getAlturaParede() { return alturaParede; }
    public void setAlturaParede(double alturaParede) { this.alturaParede = alturaParede; }

    public boolean isTemJanela() { return temJanela; }
    public void setTemJanela(boolean temJanela) { this.temJanela = temJanela; }

    public double getJanelaAltura() { return janelaAltura; }
    public void setJanelaAltura(double janelaAltura) { this.janelaAltura = janelaAltura; }

    public double getJanelaComprimento() { return janelaComprimento; }
    public void setJanelaComprimento(double janelaComprimento) { this.janelaComprimento = janelaComprimento; }

    public boolean isTemPorta() { return temPorta; }
    public void setTemPorta(boolean temPorta) { this.temPorta = temPorta; }

    public double getPortaAltura() { return portaAltura; }
    public void setPortaAltura(double portaAltura) { this.portaAltura = portaAltura; }

    public double getPortaComprimento() { return portaComprimento; }
    public void setPortaComprimento(double portaComprimento) { this.portaComprimento = portaComprimento; }
}
