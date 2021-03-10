package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_codigos_de_referencias")
public class CCodigoReferencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "descripcion_referencia", length = 70)
	private String descripcionReferencia;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcionReferencia() {
		return this.descripcionReferencia;
	}

	public void setDescripcionReferencia(String descripcionReferencia) {
		this.descripcionReferencia = descripcionReferencia;
	}
}
