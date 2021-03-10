package com.samyx.models.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.samyx.models.entity.CCodigoReferencia;

@Entity
@Table(name = "fe_facturas_referencias")
public class FEFacturaReferencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 2)
	private String tipoDoc;

	@Column(length = 50)
	private String numero;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "codigo_de_referencia_id")
	private CCodigoReferencia codigoReferencia;

	@Column(length = 25)
	private String fechaEmision;

	@Column(length = 180)
	private String razon;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoDoc() {
		return this.tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public CCodigoReferencia getCodigoReferencia() {
		return this.codigoReferencia;
	}

	public void setCodigoReferencia(CCodigoReferencia codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}

	public String getRazon() {
		return this.razon;
	}

	public void setRazon(String razon) {
		this.razon = razon;
	}
}
