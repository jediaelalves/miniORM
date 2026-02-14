package br.com.miniORM.model;

import br.com.miniORM.annotations.*;

@Table(name = "pessoas")
public class Pessoa {

    @Id
    private Integer id;

    @Column(length = 150, nullable = false)
    private String nome;

    @Column(length = 20)
    private String telefone;

    @Column(length = 150)
    private String email;

    public Pessoa(){}

    public Pessoa(String nome, String telefone, String email){
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + "]";
	}

    
    
}
