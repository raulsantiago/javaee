package br.com.casadocodigo.loja.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

@Entity
public class Livro {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	// Bean validation
	
	@NotBlank	// Valida já vazio e espeços em branco
	private String titulo;
	
	// Texto grande @Lob não funciona com postgres tem que gerar manual
	@Length(min=10) // Número mínimo de caracteres que o campo pode ter
	@NotBlank
	private String descricao;
	
	@DecimalMin("20") // Valor decimal mínimo
	private BigDecimal preco;
	
	@Min(50) // Valor inteiro mínimo
	private Integer numeroPaginas;	
	
	@Temporal(TemporalType.DATE)
	private Calendar dataPublicacao; 
	
	// Esse new ArrayList<>() ajuda e salvar dados vazio no banco e nao nulo
	@ManyToMany
	@Size(min=1) // número mínimo de elementos na lista
    @NotNull // A lista não pode ser nula
	private List<Autor> autores = new ArrayList<>();	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getNumeroPaginas() {
		return numeroPaginas;
	}

	public void setNumeroPaginas(Integer numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}
	
	public Calendar getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Calendar dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	@Override
	public String toString() {
		return "Livro [id=" + id + ", titulo=" + titulo + ", descricao=" + descricao + ", preco=" + preco
				+ ", numeroPaginas=" + numeroPaginas + ", dataPublicacao=" + dataPublicacao + ", autores=" + autores
				+ "]";
	}
}
