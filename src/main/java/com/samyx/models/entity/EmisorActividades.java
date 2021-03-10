package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.samyx.models.entity.Emisor;

@Entity
@Table(name = "emisores_actividades", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "emisor_id", "codigo_actividad_emisor" }) })
public class EmisorActividades {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Emisor emisor;

	@Column(name = "codigo_actividad_emisor", length = 6)
	private String codigoActividadEmisor;

	@Column(name = "detalle_actividad", length = 100)
	private String detalleActividad;

	@Column(length = 50)
	private String estado;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public String getCodigoActividadEmisor() {
		return this.codigoActividadEmisor;
	}

	public void setCodigoActividadEmisor(String codigoActividadEmisor) {
		this.codigoActividadEmisor = codigoActividadEmisor;
	}

	public String getDetalleActividad() {
		return this.detalleActividad;
	}

	public void setDetalleActividad(String detalleActividad) {
		this.detalleActividad = detalleActividad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String toString() {
		return "EmisorActividades [id=" + this.id + ", emisor=" + this.emisor + ", codigoActividad="
				+ this.codigoActividadEmisor + ", detalleActividad=" + this.detalleActividad + ", estado=" + this.estado
				+ "]";
	}
}
