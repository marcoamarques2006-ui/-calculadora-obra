package marco.calculadora_obra.web;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import marco.calculadora_obra.api.dto.AberturaDTO;
import marco.calculadora_obra.api.dto.ArestaDTO;
import marco.calculadora_obra.api.dto.OrcamentoRequestDTO;
import marco.calculadora_obra.api.dto.OrcamentoResponseDTO;
import marco.calculadora_obra.domain.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean (view-scoped) da tela de solicitação de orçamento.
 * Mantém o formulário dinâmico de paredes e chama o {@link OrcamentoService}.
 */
@Component
@Scope("view")
public class OrcamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient OrcamentoService orcamentoService;

    // Dados do solicitante / planta
    private String nomeUsuario;
    private String descricao;
    private final List<ParedeForm> paredes = new ArrayList<>();

    // Parâmetros de cálculo
    private double alturaViga = 0.40;
    private double alturaTijolo = 0.057;
    private double comprimentoTijolo = 0.19;

    // Preços e margem
    private double precoConcretoM3 = 450.0;
    private double precoTijolo = 1.20;
    private double margemLucroPercentual = 20.0;

    // Resultado
    private OrcamentoResponseDTO resultado;

    public OrcamentoBean() {
        adicionarParede(); // começa com uma parede
    }

    public void adicionarParede() {
        ParedeForm p = new ParedeForm();
        p.setNome("Parede " + (paredes.size() + 1));
        paredes.add(p);
    }

    public void removerParede(ParedeForm parede) {
        paredes.remove(parede);
    }

    public void gerar() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        try {
            OrcamentoRequestDTO request = montarRequest();
            resultado = orcamentoService.gerar(request);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Orçamento Nº " + resultado.getNumero() + " gerado com sucesso!", null));
        } catch (IllegalArgumentException ex) {
            resultado = null;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        } catch (Exception ex) {
            resultado = null;
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Erro ao gerar orçamento: " + ex.getMessage(), null));
        }
    }

    public void novo() {
        resultado = null;
        nomeUsuario = null;
        descricao = null;
        paredes.clear();
        adicionarParede();
    }

    private OrcamentoRequestDTO montarRequest() {
        OrcamentoRequestDTO request = new OrcamentoRequestDTO();
        request.setNomeUsuario(nomeUsuario);
        request.setDescricao(descricao);
        request.setAlturaViga(alturaViga);
        request.setAlturaTijolo(alturaTijolo);
        request.setComprimentoTijolo(comprimentoTijolo);
        request.setPrecoConcretoM3(precoConcretoM3);
        request.setPrecoTijolo(precoTijolo);
        request.setMargemLucroPercentual(margemLucroPercentual);

        List<ArestaDTO> arestas = new ArrayList<>();
        int i = 1;
        for (ParedeForm p : paredes) {
            ArestaDTO a = new ArestaDTO();
            a.setId("P" + i++);
            a.setComprimento(p.getComprimento());
            a.setEspessura(p.getEspessura());
            a.setAlturaParede(p.getAlturaParede());
            if (p.isTemJanela()) {
                a.setTemJanela(true);
                a.setJanela(new AberturaDTO(p.getJanelaAltura(), p.getJanelaComprimento()));
            }
            if (p.isTemPorta()) {
                a.setTemPorta(true);
                a.setPorta(new AberturaDTO(p.getPortaAltura(), p.getPortaComprimento()));
            }
            arestas.add(a);
        }
        request.setParedes(arestas);
        return request;
    }

    // getters/setters

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<ParedeForm> getParedes() { return paredes; }

    public double getAlturaViga() { return alturaViga; }
    public void setAlturaViga(double alturaViga) { this.alturaViga = alturaViga; }

    public double getAlturaTijolo() { return alturaTijolo; }
    public void setAlturaTijolo(double alturaTijolo) { this.alturaTijolo = alturaTijolo; }

    public double getComprimentoTijolo() { return comprimentoTijolo; }
    public void setComprimentoTijolo(double comprimentoTijolo) { this.comprimentoTijolo = comprimentoTijolo; }

    public double getPrecoConcretoM3() { return precoConcretoM3; }
    public void setPrecoConcretoM3(double precoConcretoM3) { this.precoConcretoM3 = precoConcretoM3; }

    public double getPrecoTijolo() { return precoTijolo; }
    public void setPrecoTijolo(double precoTijolo) { this.precoTijolo = precoTijolo; }

    public double getMargemLucroPercentual() { return margemLucroPercentual; }
    public void setMargemLucroPercentual(double margemLucroPercentual) { this.margemLucroPercentual = margemLucroPercentual; }

    public OrcamentoResponseDTO getResultado() { return resultado; }
}
