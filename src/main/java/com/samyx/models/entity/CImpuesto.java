package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_impuestos")
public class CImpuesto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "detalle_impuesto", unique = true)
	private String detalleImpuesto;

	public String getLabelImpuesto() {
		String res = "" + getId();
		if (getId().longValue() < 9L)
			res = "0" + res;
		return res;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDetalleImpuesto() {
		return this.detalleImpuesto;
	}

	public void setDetalleImpuesto(String detalleImpuesto) {
		this.detalleImpuesto = detalleImpuesto;
	}
}
