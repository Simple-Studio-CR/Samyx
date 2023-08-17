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

import com.samyx.models.entity.CImpuesto;

@Entity
@Table(name = "c_inventario_movimientos_impuestos")
public class CInventarioMovimientoItemsImpuesto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "impuesto_id")
	private CImpuesto impuesto;

	@Column(precision = 20, scale = 3)
	private Double tarifa;

	@Column(precision = 20, scale = 3)
	private Double monto;

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

	public Double getTarifa() {
		return this.tarifa;
	}

	public void setTarifa(Double tarifa) {
		this.tarifa = tarifa;
	}

	public Double getMonto() {
		return this.monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}
}
