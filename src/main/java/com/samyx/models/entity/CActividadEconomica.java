package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_actividades_economicas")
public class CActividadEconomica {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "codigo_actividad", length = 6)
	private String codigoActividad;

	@Column(name = "detalle_actividad", length = 355)
	private String detalleActividad;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoActividad() {
		return this.codigoActividad;
	}

	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	public String getDetalleActividad() {
		return this.detalleActividad;
	}

	public void setDetalleActividad(String detalleActividad) {
		this.detalleActividad = detalleActividad;
	}
}