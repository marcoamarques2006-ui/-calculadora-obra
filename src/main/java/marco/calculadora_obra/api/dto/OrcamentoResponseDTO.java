package marco.calculadora_obra.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrcamentoResponseDTO {

    private Long numero;
    private String nomeUsuario;
    private String descricao;
    private LocalDateTime dataCriacao;

    private double volumeConcretoM3;
    private int quantidadeTijolos;
    private int quantidadeTijolosComPerda;
    private double areaTotalLiquida;

    private double custoConcreto;
    private double custoTijolos;
    private double custoTotalMateriais;
    private double margemLucroPercentual;
    private double valorFinalComMargem;

    private List<OrcamentoItemDTO> itens;

    public OrcamentoResponseDTO() {}

    public Long getNumero() { return numero; }
    public void setNumero(Long numero) { this.numero = numero; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public double getVolumeConcretoM3() { return volumeConcretoM3; }
    public void setVolumeConcretoM3(double volumeConcretoM3) { this.volumeConcretoM3 = volumeConcretoM3; }

    public int getQuantidadeTijolos() { return quantidadeTijolos; }
    public void setQuantidadeTijolos(int quantidadeTijolos) { this.quantidadeTijolos = quantidadeTijolos; }

    public int getQuantidadeTijolosComPerda() { return quantidadeTijolosComPerda; }
    public void setQuantidadeTijolosComPerda(int quantidadeTijolosComPerda) { this.quantidadeTijolosComPerda = quantidadeTijolosComPerda; }

    public double getAreaTotalLiquida() { return areaTotalLiquida; }
    public void setAreaTotalLiquida(double areaTotalLiquida) { this.areaTotalLiquida = areaTotalLiquida; }

    public double getCustoConcreto() { return custoConcreto; }
    public void setCustoConcreto(double custoConcreto) { this.custoConcreto = custoConcreto; }

    public double getCustoTijolos() { return custoTijolos; }
    public void setCustoTijolos(double custoTijolos) { this.custoTijolos = custoTijolos; }

    public double getCustoTotalMateriais() { return custoTotalMateriais; }
    public void setCustoTotalMateriais(double custoTotalMateriais) { this.custoTotalMateriais = custoTotalMateriais; }

    public double getMargemLucroPercentual() { return margemLucroPercentual; }
    public void setMargemLucroPercentual(double margemLucroPercentual) { this.margemLucroPercentual = margemLucroPercentual; }

    public double getValorFinalComMargem() { return valorFinalComMargem; }
    public void setValorFinalComMargem(double valorFinalComMargem) { this.valorFinalComMargem = valorFinalComMargem; }

    public List<OrcamentoItemDTO> getItens() { return itens; }
    public void setItens(List<OrcamentoItemDTO> itens) { this.itens = itens; }
}
