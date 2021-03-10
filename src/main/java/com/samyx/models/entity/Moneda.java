package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_monedas")
public class Moneda {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 1)
	private String abreviatura;

	@Column(name = "simbolo_moneda", length = 5, unique = true)
	private String simboloMoneda;

	@Column(name = "descripcion_moneda", length = 50)
	private String descripcionMoneda;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getSimboloMoneda() {
		return this.simboloMoneda;
	}

	public void setSimboloMoneda(String simboloMoneda) {
		this.simboloMoneda = simboloMoneda;
	}

	public String getDescripcionMoneda() {
		return this.descripcionMoneda;
	}

	public void setDescripcionMoneda(String descripcionMoneda) {
		this.descripcionMoneda = descripcionMoneda;
	}
}
