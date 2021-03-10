package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.samyx.models.entity.CCodigosTarifasIva;
import com.samyx.models.entity.CImpuesto;
import com.samyx.models.entity.Producto;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "c_producto_impuestos", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "producto_id", "impuesto_id" }) })
public class CProductoImpuesto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "impuesto_id")
	private CImpuesto impuesto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigos_tarifas_iva_id")
	private CCodigosTarifasIva codigosTarifasIva;

	private Double porcentaje;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Producto producto;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CImpuesto getImpuesto() {
		return this.impuesto;
	}

	public void setImpuesto(CImpuesto impuesto) {
		this.impuesto = impuesto;
	}

	public Double getPorcentaje() {
		return this.porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public CCodigosTarifasIva getCodigosTarifasIva() {
		return this.codigosTarifasIva;
	}

	public void setCodigosTarifasIva(CCodigosTarifasIva codigosTarifasIva) {
		this.codigosTarifasIva = codigosTarifasIva;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}