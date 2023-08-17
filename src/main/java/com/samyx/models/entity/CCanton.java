package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.samyx.models.entity.CProvincia;

@Entity
@Table(name = "c_cantones", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "provincia_id", "canton", "numero_canton" }) })
public class CCanton {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provincia_id")
	private CProvincia provincia;

	@Column(length = 3)
	private String numero_canton;

	@Column(length = 50)
	private String canton;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CProvincia getProvincia() {
		return this.provincia;
	}

	public void setProvincia(CProvincia provincia) {
		this.provincia = provincia;
	}

	public String getNumero_canton() {
		return this.numero_canton;
	}

	public void setNumero_canton(String numero_canton) {
		this.numero_canton = numero_canton;
	}

	public String getCanton() {
		return this.canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}
}