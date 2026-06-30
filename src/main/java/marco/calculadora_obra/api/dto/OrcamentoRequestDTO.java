package marco.calculadora_obra.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Solicitação de orçamento: nome do usuário, planta (lista de paredes) e parâmetros de
 * cálculo e de preço para gerar o valor final com margem de lucro.
 */
public class OrcamentoRequestDTO {

    @NotBlank(message = "Nome do usuário é obrigatório")
    private String nomeUsuario;

    private String descricao;

    @NotEmpty(message = "Informe ao menos uma parede da planta")
    @Valid
    private List<ArestaDTO> paredes;

    @Positive(message = "Altura da viga baldrame deve ser positiva")
    private double alturaViga;

    @Positive(message = "Altura do tijolo deve ser positiva")
    private double alturaTijolo;

    @Positive(message = "Comprimento do tijolo deve ser positivo")
    private double comprimentoTijolo;

    @PositiveOrZero(message = "Preço do m³ de concreto não pode ser negativo")
    private double precoConcretoM3;

    @PositiveOrZero(message = "Preço do tijolo não pode ser negativo")
    private double precoTijolo;

    @PositiveOrZero(message = "Margem de lucro não pode ser negativa")
    private double margemLucroPercentual;

    public OrcamentoRequestDTO() {}

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<ArestaDTO> getParedes() { return paredes; }
    public void setParedes(List<ArestaDTO> paredes) { this.paredes = paredes; }

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
}
