package marco.calculadora_obra.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

public class ComodoDTO {

    @NotBlank(message = "Nome do cômodo é obrigatório")
    private String nome;

    @Positive(message = "Largura deve ser positiva")
    private double largura;

    @Positive(message = "Comprimento deve ser positivo")
    private double comprimento;

    @Positive(message = "Altura deve ser positiva")
    private double altura;

    private List<String> arestaIds = new ArrayList<>();

    public ComodoDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getLargura() { return largura; }
    public void setLargura(double largura) { this.largura = largura; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public List<String> getArestaIds() { return arestaIds; }
    public void setArestaIds(List<String> arestaIds) { this.arestaIds = arestaIds; }
}