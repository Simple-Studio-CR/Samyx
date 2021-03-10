package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samyx.models.entity.CMedioDePago;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.FEFacturasCXC;
import com.samyx.models.entity.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "fe_factura_registro_pagos_cxc")
public class FEFacturaRegistroPagosCXC {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numero_cxc")
	private Long numeroCXC;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "factura_cxc_id")
	private FEFacturasCXC facturaCXC;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "emisor_id")
	@JsonIgnore
	private Emisor emisor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "cliente_id")
	@JsonIgnore
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "medio_de_pago")
	private CMedioDePago medioDePago;

	@NotNull
	@Column(name = "fecha_registro_pago")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaRegistroAbondo;

	@Column(name = "fecha_pago_abono")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fechaPagoAbondo;

	@Column(name = "monto_abondo", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private double montoAbono;

	@Column(name = "monto_actual", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private double montoActual;

	@Column(name = "numero_tarjeta", length = 4)
	private String numeroTarjeta;

	@Column(name = "numero_autorizacion", length = 50)
	private String numeroAutorizacion;

	@Column(name = "num_documento", length = 100)
	private String numDocumento;

	@Column(length = 255)
	private String observacion;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroCXC() {
		return this.numeroCXC;
	}

	public void setNumeroCXC(Long numeroCXC) {
		this.numeroCXC = numeroCXC;
	}

	public FEFacturasCXC getFacturaCXC() {
		return this.facturaCXC;
	}

	public void setFacturaCXC(FEFacturasCXC facturaCXC) {
		this.facturaCXC = facturaCXC;
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFechaRegistroAbondo() {
		return this.fechaRegistroAbondo;
	}

	public void setFechaRegistroAbondo(Date fechaRegistroAbondo) {
		this.fechaRegistroAbondo = fechaRegistroAbondo;
	}

	public Date getFechaPagoAbondo() {
		return this.fechaPagoAbondo;
	}

	public void setFechaPagoAbondo(Date fechaPagoAbondo) {
		this.fechaPagoAbondo = fechaPagoAbondo;
	}

	public double getMontoAbono() {
		return this.montoAbono;
	}

	public void setMontoAbono(double montoAbono) {
		this.montoAbono = montoAbono;
	}

	public double getMontoActual() {
		return this.montoActual;
	}

	public void setMontoActual(double montoActual) {
		this.montoActual = montoActual;
	}

	public CMedioDePago getMedioDePago() {
		return this.medioDePago;
	}

	public void setMedioDePago(CMedioDePago medioDePago) {
		this.medioDePago = medioDePago;
	}

	public String getNumeroTarjeta() {
		return this.numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getNumeroAutorizacion() {
		return this.numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public String getNumDocumento() {
		return this.numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public String getFechaAbono() {
		String r = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		r = sdf.format(this.fechaRegistroAbondo);
		return r;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
}
