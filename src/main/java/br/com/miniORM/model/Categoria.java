package br.com.miniORM.model;

import br.com.miniORM.annotations.*;
import java.time.LocalDateTime;




@Table(name = "categorias")
public class Categoria {

    @Id
    private Integer id;

   

	private LocalDateTime dataHora;

    public Categoria(){}

    public Categoria(String descricao, LocalDateTime dataHora){
        this.descricao = descricao;
        this.dataHora = dataHora;
    }

    @Column(length = 500, nullable = false)
    private String descricao;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", dataHora=" + dataHora + ", descricao=" + descricao + "]";
	}

 
}
