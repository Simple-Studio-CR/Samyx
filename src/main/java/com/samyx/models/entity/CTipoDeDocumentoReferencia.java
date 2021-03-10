package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_tipo_de_documento_referencia")
public class CTipoDeDocumentoReferencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "documento_referencia", length = 150)
	private String documentoReferencia;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocumentoReferencia() {
		return this.documentoReferencia;
	}

	public void setDocumentoReferencia(String documentoReferencia) {
		this.documentoReferencia = documentoReferencia;
	}
}
