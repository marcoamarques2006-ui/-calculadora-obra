package marco.calculadora_obra.web;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import marco.calculadora_obra.api.dto.OrcamentoResponseDTO;
import marco.calculadora_obra.domain.exception.RecursoNaoEncontradoException;
import marco.calculadora_obra.domain.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean (view-scoped) da tela de consulta de orçamentos por número ou nome do usuário.
 */
@Component
@Scope("view")
public class ConsultaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient OrcamentoService orcamentoService;

    private Long numero;
    private String nomeUsuario;
    private List<OrcamentoResponseDTO> resultados = new ArrayList<>();
    private boolean pesquisou;

    public void buscarPorNumero() {
        pesquisou = true;
        resultados = new ArrayList<>();
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (numero == null) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe o número do orçamento.", null));
            return;
        }
        try {
            resultados.add(orcamentoService.buscarPorNumero(numero));
        } catch (RecursoNaoEncontradoException ex) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), null));
        }
    }

    public void buscarPorNome() {
        pesquisou = true;
        FacesContext ctx = FacesContext.getCurrentInstance();
        resultados = orcamentoService.buscarPorNomeUsuario(nomeUsuario);
        if (resultados.isEmpty()) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Nenhum orçamento encontrado para: " + nomeUsuario, null));
        }
    }

    public void listarTodos() {
        pesquisou = true;
        resultados = orcamentoService.listarTodos();
    }

    public Long getNumero() { return numero; }
    public void setNumero(Long numero) { this.numero = numero; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public List<OrcamentoResponseDTO> getResultados() { return resultados; }

    public boolean isPesquisou() { return pesquisou; }
}
