package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_tipos_de_productos")
public class CTipoProducto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_codigo_producto_servicio", length = 50)
	private String tipoCodigoProductoServicio;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoCodigoProductoServicio() {
		return this.tipoCodigoProductoServicio;
	}

	public void setTipoCodigoProductoServicio(String tipoCodigoProductoServicio) {
		this.tipoCodigoProductoServicio = tipoCodigoProductoServicio;
	}
}