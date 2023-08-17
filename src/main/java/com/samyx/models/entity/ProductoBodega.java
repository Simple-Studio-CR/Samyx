package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.samyx.models.entity.Emisor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "productos_bodegas")
public class ProductoBodega {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emisor_id")
	@JsonBackReference
	private Emisor emisor;

	@Column(name = "nombre_bodega", length = 150, unique = true)
	private String nombreBodega;

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

	public String getNombreBodega() {
		return this.nombreBodega;
	}

	public void setNombreBodega(String nombreBodega) {
		this.nombreBodega = nombreBodega;
	}
}
