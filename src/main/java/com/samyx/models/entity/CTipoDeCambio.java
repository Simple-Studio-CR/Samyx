package com.samyx.models.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import com.samyx.models.entity.Moneda;

@Entity
@Table(name = "c_tipo_de_cambio", uniqueConstraints = { @UniqueConstraint(columnNames = { "moneda_id", "fecha" }) })
public class CTipoDeCambio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moneda_id")
	private Moneda moneda;

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fecha;

	@Column(name = "tipo_de_cambio")
	private Double tipoDeCambio;

	@Column(name = "tipo_cambio_dolar_euro", precision = 18, scale = 5)
	private Double tipoCambioDolarEuro;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getTipoDeCambio() {
		return this.tipoDeCambio;
	}

	public void setTipoDeCambio(Double tipoDeCambio) {
		this.tipoDeCambio = tipoDeCambio;
	}

	public Double getTipoCambioDolarEuro() {
		return this.tipoCambioDolarEuro;
	}

	public void setTipoCambioDolarEuro(Double tipoCambioDolarEuro) {
		this.tipoCambioDolarEuro = tipoCambioDolarEuro;
	}
}