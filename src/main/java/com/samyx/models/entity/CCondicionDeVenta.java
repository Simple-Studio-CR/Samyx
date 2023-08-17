package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_condicion_de_venta")
public class CCondicionDeVenta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "condicion_venta")
	private String condicionVenta;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCondicionVenta() {
		return this.condicionVenta;
	}

	public void setCondicionVenta(String condicionVenta) {
		this.condicionVenta = condicionVenta;
	}
}
