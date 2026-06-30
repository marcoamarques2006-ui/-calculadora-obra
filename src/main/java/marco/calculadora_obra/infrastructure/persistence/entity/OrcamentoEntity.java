package marco.calculadora_obra.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Orçamento calculado e persistido.
 * O {@code pk} é o "número do orçamento" usado para busca, junto com o nome do usuário.
 */
@Entity
@Table(name = "orcamentos", indexes = {
    @Index(name = "idx_orcamento_nome_usuario", columnList = "nomeUsuario")
})
public class OrcamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numero;

    @Column(nullable = false)
    private String nomeUsuario;

    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    // Parâmetros usados no cálculo
    private double alturaViga;
    private double alturaTijolo;
    private double comprimentoTijolo;
    private double precoConcretoM3;
    private double precoTijolo;
    private double margemLucroPercentual;

    // Resultados dos cálculos de engenharia
    private double volumeConcretoM3;
    private int quantidadeTijolos;
    private int quantidadeTijolosComPerda;
    private double areaTotalLiquida;

    // Composição de custos do orçamento
    private double custoConcreto;
    private double custoTijolos;
    private double custoTotalMateriais;
    private double valorFinalComMargem;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoItemEntity> itens = new ArrayList<>();

    public OrcamentoEntity() {}

    public Long getNumero() { return numero; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

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

    public double getValorFinalComMargem() { return valorFinalComMargem; }
    public void setValorFinalComMargem(double valorFinalComMargem) { this.valorFinalComMargem = valorFinalComMargem; }

    public List<OrcamentoItemEntity> getItens() { return itens; }
    public void setItens(List<OrcamentoItemEntity> itens) { this.itens = itens; }

    public void addItem(OrcamentoItemEntity item) {
        item.setOrcamento(this);
        this.itens.add(item);
    }
}
