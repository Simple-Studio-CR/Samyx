package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "c_tipo_documento_de_exoneracion_o_autorizacion", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "exoneracion_o_autorizacion" }) })
public class CTipoDocumentoExoneracionOAutorizacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "exoneracion_o_autorizacion", length = 50)
	private String exoneracionOAutorizacion;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExoneracionOAutorizacion() {
		return this.exoneracionOAutorizacion;
	}

	public void setExoneracionOAutorizacion(String exoneracionOAutorizacion) {
		this.exoneracionOAutorizacion = exoneracionOAutorizacion;
	}
}
