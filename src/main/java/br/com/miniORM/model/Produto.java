package br.com.miniORM.model;

import br.com.miniORM.annotations.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "produtos")
public class Produto {

    @Id
    private Integer id;

    @Column(length = 13)
    private String codigoBarras; // EAN-13 ou ID da balança

    @Column(length = 100, nullable = false)
    private String nome;

    private BigDecimal preco;  // Preço por UN ou por KG
    private BigDecimal estoque; // Suporta peso (ex: 0.500 kg)
    private Boolean ativo;
    private Boolean pesavel; // True = item passa pela balança
    private UnidadeMedida unidadeMedida;
    private LocalDateTime dataCriacao;
    private LocalDate validade;

    // Relacionamento ManyToOne com Categoria
    @Column(name = "categoria_id")
    private Categoria categoria;

    // ================= ENUMS =================
    public enum UnidadeMedida {
        KG,  // Quilo
        UN,  // Unidade
        L,   // Litro
        G    // Grama
    }

    // ================= CONSTRUTORES =================
    public Produto() {}

    public Produto(String codigoBarras, String nome, BigDecimal preco, BigDecimal estoque,
                   Boolean ativo, Boolean pesavel, UnidadeMedida unidadeMedida,
                   LocalDateTime dataCriacao, LocalDate validade, Categoria categoria) {
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = ativo;
        this.pesavel = pesavel;
        this.unidadeMedida = unidadeMedida;
        this.dataCriacao = dataCriacao;
        this.validade = validade;
        this.categoria = categoria;
    }

    // ================= MÉTODOS DE NEGÓCIO =================
    /**
     * Calcula o valor total com base na quantidade/peso
     */
    public BigDecimal calcularTotal(double quantidade) {
        if (preco == null) return BigDecimal.ZERO;
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }

    /**
     * Calcula preço com desconto percentual
     */
    public BigDecimal calcularPrecoComDesconto(double percentual) {
        if (preco == null) return BigDecimal.ZERO;
        BigDecimal desconto = preco.multiply(BigDecimal.valueOf(percentual / 100.0));
        return preco.subtract(desconto);
    }

    // ================= GETTERS E SETTERS =================
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public BigDecimal getEstoque() { return estoque; }
    public void setEstoque(BigDecimal estoque) { this.estoque = estoque; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Boolean getPesavel() { return pesavel; }
    public void setPesavel(Boolean pesavel) { this.pesavel = pesavel; }

    public UnidadeMedida getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(UnidadeMedida unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    // ================= toString =================
    @Override
    public String toString() {
        return "Produto [id=" + id +
                ", codigoBarras=" + codigoBarras +
                ", nome=" + nome +
                ", preco=" + preco +
                ", estoque=" + estoque +
                ", ativo=" + ativo +
                ", pesavel=" + pesavel +
                ", unidadeMedida=" + unidadeMedida +
                ", dataCriacao=" + dataCriacao +
                ", validade=" + validade +
                ", categoria=" + categoria +
                "]";
    }
}
