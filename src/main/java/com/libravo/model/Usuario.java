package com.libravo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = true)
	private String lastName;
	
	@ManyToOne
	private Permissao permissao;
	
	@Column(nullable = false)
	private String imageUrl;

	public Usuario() {
		super();
	}

	public Usuario(String email, String firstName, String lastName, Permissao permissao) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.permissao = permissao;
	}

	public Usuario(String email, String firstName, Permissao permissao) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.permissao = permissao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
	
}
