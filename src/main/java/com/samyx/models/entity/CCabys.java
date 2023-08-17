package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_cabys")
public class CCabys {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "codigo_hacienda", length = 13)
	private String codigoHacienda;

	@Column(name = "producto_servicio")
	private String productoServicio;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoHacienda() {
		return this.codigoHacienda;
	}

	public void setCodigoHacienda(String codigoHacienda) {
		this.codigoHacienda = codigoHacienda;
	}

	public String getProductoServicio() {
		return this.productoServicio;
	}

	public void setProductoServicio(String productoServicio) {
		this.productoServicio = productoServicio;
	}
}