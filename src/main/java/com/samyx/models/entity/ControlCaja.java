package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Usuario;

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
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "control_cajas")
public class ControlCaja {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	@JsonBackReference
	private Usuario usuario;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_cerro_id")
	@JsonBackReference
	private Usuario usuarioCerro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emisor_id")
	private Emisor emisor;

	@NotNull
	@Column(name = "fecha_apertura")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaApertura;

	@Column(name = "fecha_cierre")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaCierre;

	@Column(name = "apertura_colones", precision = 18, scale = 5)
	private double aperturaColones = 0.0D;

	@Column(name = "apertura_dolares", precision = 18, scale = 5)
	private double aperturaDolares = 0.0D;

	@Column(name = "apertura_euros", precision = 18, scale = 5)
	private double aperturaEuros = 0.0D;

	@Column(name = "salidas_colones", precision = 18, scale = 5)
	private double salidasColones = 0.0D;

	@Column(name = "salidas_dolares", precision = 18, scale = 5)
	private double salidasDolares = 0.0D;

	@Column(name = "salidas_euros", precision = 18, scale = 5)
	private double salidasEuros = 0.0D;

	@Column(name = "entradas_colones", precision = 18, scale = 5)
	private double entradasColones = 0.0D;

	@Column(name = "entradas_dolares", precision = 18, scale = 5)
	private double entradasDolares = 0.0D;

	@Column(name = "entradas_euros", precision = 18, scale = 5)
	private double entradasEuros = 0.0D;

	@Column(name = "ventas_colones_efectivo", precision = 18, scale = 5)
	private double ventasColonesEfectivo = 0.0D;

	@Column(name = "ventas_dolares_efectivo", precision = 18, scale = 5)
	private double ventasDolaresEfectivo = 0.0D;

	@Column(name = "ventas_euros_efectivo", precision = 18, scale = 5)
	private double ventasEurosEfectivo = 0.0D;

	@Column(name = "ventas_colones_tarjeta", precision = 18, scale = 5)
	private double ventasColonesTarjeta = 0.0D;

	@Column(name = "ventas_dolares_tarjeta", precision = 18, scale = 5)
	private double ventasDolaresTarjeta = 0.0D;

	@Column(name = "ventas_eurostarjeta", precision = 18, scale = 5)
	private double ventasEurosTarjeta = 0.0D;

	@Column(name = "ventas_colones_credito", precision = 18, scale = 5)
	private double ventasColonesCredito = 0.0D;

	@Column(name = "ventas_dolares_credito", precision = 18, scale = 5)
	private double ventasDolaresCredito = 0.0D;

	@Column(name = "ventas_euros_credito", precision = 18, scale = 5)
	private double ventasEurosCredito = 0.0D;

	@Column(name = "ventas_colones_cheques", precision = 18, scale = 5)
	private double ventasColonesCheques = 0.0D;

	@Column(name = "ventas_dolares_cheques", precision = 18, scale = 5)
	private double ventasDolaresCheques = 0.0D;

	@Column(name = "ventas_euros_cheques", precision = 18, scale = 5)
	private double ventasEurosCheques = 0.0D;

	@Column(name = "ventas_colones_deposito", precision = 18, scale = 5)
	private double ventasColonesDeposito = 0.0D;

	@Column(name = "ventas_dolares_deposito", precision = 18, scale = 5)
	private double ventasDolaresDeposito = 0.0D;

	@Column(name = "ventas_euros_deposito", precision = 18, scale = 5)
	private double ventasEurosDeposito = 0.0D;

	@Column(name = "cxc_colones_efectivo", precision = 18, scale = 5)
	private double cxcColonesEfectivo = 0.0D;

	@Column(name = "cxc_dolares_efectivo", precision = 18, scale = 5)
	private double cxcDolaresEfectivo = 0.0D;

	@Column(name = "cxc_euros_efectivo", precision = 18, scale = 5)
	private double cxcEurosEfectivo = 0.0D;

	@Column(name = "cxc_colones_tarjeta", precision = 18, scale = 5)
	private double cxcColonesTarjeta = 0.0D;

	@Column(name = "cxc_dolares_tarjeta", precision = 18, scale = 5)
	private double cxcDolaresTarjeta = 0.0D;

	@Column(name = "cxc_euros_tarjeta", precision = 18, scale = 5)
	private double cxcEurosTarjeta = 0.0D;

	@Column(name = "cxc_colones_deposito", precision = 18, scale = 5)
	private double cxcColonesDeposito = 0.0D;

	@Column(name = "cxc_dolares_deposito", precision = 18, scale = 5)
	private double cxcDolaresDeposito = 0.0D;

	@Column(name = "cxc_euros_deposito", precision = 18, scale = 5)
	private double cxcEurosDeposito = 0.0D;

	@Column(name = "cxc_colones_cheques", precision = 18, scale = 5)
	private double cxcColonesCheques = 0.0D;

	@Column(name = "cxc_dolares_cheques", precision = 18, scale = 5)
	private double cxcDolaresCheques = 0.0D;

	@Column(name = "cxc_euros_cheques", precision = 18, scale = 5)
	private double cxcEurosCheques = 0.0D;

	@Column(name = "cierre_colones", precision = 18, scale = 5)
	private double cierreColones = 0.0D;

	@Column(name = "cierre_dolares", precision = 18, scale = 5)
	private double cierreDolares = 0.0D;

	@Column(name = "cierre_euros", precision = 18, scale = 5)
	private double cierreEuros = 0.0D;

	@Column(name = "totalEfectivoColones", precision = 18, scale = 5)
	private Double totalEfectivoColones = Double.valueOf(0.0D);

	@Column(name = "totalEfectivoDolares", precision = 18, scale = 5)
	private Double totalEfectivoDolares = Double.valueOf(0.0D);

	@Column(name = "totalEfectivoEuros", precision = 18, scale = 5)
	private Double totalEfectivoEuros = Double.valueOf(0.0D);

	@Column(name = "totalTarjetaColones", precision = 18, scale = 5)
	private Double totalTarjetaColones = Double.valueOf(0.0D);

	@Column(name = "totalTarjetaDolares", precision = 18, scale = 5)
	private Double totalTarjetaDolares = Double.valueOf(0.0D);

	@Column(name = "totalTarjetaEuros", precision = 18, scale = 5)
	private Double totalTarjetaEuros = Double.valueOf(0.0D);

	@Column(name = "totalChequeColones", precision = 18, scale = 5)
	private Double totalChequeColones = Double.valueOf(0.0D);

	@Column(name = "totalChequeDolares", precision = 18, scale = 5)
	private Double totalChequeDolares = Double.valueOf(0.0D);

	@Column(name = "totalChequeEuros", precision = 18, scale = 5)
	private Double totalChequeEuros = Double.valueOf(0.0D);

	@Column(name = "totalDepositosColones", precision = 18, scale = 5)
	private Double totalDepositosColones = Double.valueOf(0.0D);

	@Column(name = "totalDepositosDolares", precision = 18, scale = 5)
	private Double totalDepositosDolares = Double.valueOf(0.0D);

	@Column(name = "totalDepositosEuros", precision = 18, scale = 5)
	private Double totalDepositosEuros = Double.valueOf(0.0D);

	@Column(name = "cierreTotalEfectivoColones", precision = 18, scale = 5)
	private Double cierreTotalEfectivoColones = Double.valueOf(0.0D);

	@Column(name = "cierreTotalEfectivoDolares", precision = 18, scale = 5)
	private Double cierreTotalEfectivoDolares = Double.valueOf(0.0D);

	@Column(name = "cierreTotalEfectivoEuros", precision = 18, scale = 5)
	private Double cierreTotalEfectivoEuros = Double.valueOf(0.0D);

	@Column(name = "cierreTotalTarjetaColones", precision = 18, scale = 5)
	private Double cierreTotalTarjetaColones = Double.valueOf(0.0D);

	@Column(name = "cierreTotalTarjetaDolares", precision = 18, scale = 5)
	private Double cierreTotalTarjetaDolares = Double.valueOf(0.0D);

	@Column(name = "cierreTotalTarjetaEuros", precision = 18, scale = 5)
	private Double cierreTotalTarjetaEuros = Double.valueOf(0.0D);

	@Column(name = "cierreTotalChequeColones", precision = 18, scale = 5)
	private Double cierreTotalChequeColones = Double.valueOf(0.0D);

	@Column(name = "cierreTotalChequeDolares", precision = 18, scale = 5)
	private Double cierreTotalChequeDolares = Double.valueOf(0.0D);

	@Column(name = "cierreTotalChequeEuros", precision = 18, scale = 5)
	private Double cierreTotalChequeEuros = Double.valueOf(0.0D);

	@Column(name = "cierreTotalDepositosColones", precision = 18, scale = 5)
	private Double cierreTotalDepositosColones = Double.valueOf(0.0D);

	@Column(name = "cierreTotalDepositosDolares", precision = 18, scale = 5)
	private Double cierreTotalDepositosDolares = Double.valueOf(0.0D);

	@Column(name = "cierreTotalDepositosEuros", precision = 18, scale = 5)
	private Double cierreTotalDepositosEuros = Double.valueOf(0.0D);

	@Column(name = "totalDevolucionesColones", precision = 18, scale = 5)
	private Double totalDevolucionesColones = Double.valueOf(0.0D);

	@Column(name = "totalDevolucionesDolares", precision = 18, scale = 5)
	private Double totalDevolucionesDolares = Double.valueOf(0.0D);

	@Column(name = "totalDevolucionesEuros", precision = 18, scale = 5)
	private Double totalDevolucionesEuros = Double.valueOf(0.0D);

	@Column(name = "estado_caja", length = 1)
	private String estadoCaja;

	@Column(length = 500)
	private String observaciones;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioCerro() {
		return this.usuarioCerro;
	}

	public void setUsuarioCerro(Usuario usuarioCerro) {
		this.usuarioCerro = usuarioCerro;
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public Date getFechaApertura() {
		return this.fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public double getAperturaColones() {
		return this.aperturaColones;
	}

	public void setAperturaColones(double aperturaColones) {
		this.aperturaColones = aperturaColones;
	}

	public double getAperturaDolares() {
		return this.aperturaDolares;
	}

	public void setAperturaDolares(double aperturaDolares) {
		this.aperturaDolares = aperturaDolares;
	}

	public double getAperturaEuros() {
		return this.aperturaEuros;
	}

	public void setAperturaEuros(double aperturaEuros) {
		this.aperturaEuros = aperturaEuros;
	}

	public double getSalidasColones() {
		return this.salidasColones;
	}

	public void setSalidasColones(double salidasColones) {
		this.salidasColones = salidasColones;
	}

	public double getSalidasDolares() {
		return this.salidasDolares;
	}

	public void setSalidasDolares(double salidasDolares) {
		this.salidasDolares = salidasDolares;
	}

	public double getSalidasEuros() {
		return this.salidasEuros;
	}

	public void setSalidasEuros(double salidasEuros) {
		this.salidasEuros = salidasEuros;
	}

	public double getEntradasColones() {
		return this.entradasColones;
	}

	public void setEntradasColones(double entradasColones) {
		this.entradasColones = entradasColones;
	}

	public double getEntradasDolares() {
		return this.entradasDolares;
	}

	public void setEntradasDolares(double entradasDolares) {
		this.entradasDolares = entradasDolares;
	}

	public double getEntradasEuros() {
		return this.entradasEuros;
	}

	public void setEntradasEuros(double entradasEuros) {
		this.entradasEuros = entradasEuros;
	}

	public double getVentasColonesEfectivo() {
		return this.ventasColonesEfectivo;
	}

	public void setVentasColonesEfectivo(double ventasColonesEfectivo) {
		this.ventasColonesEfectivo = ventasColonesEfectivo;
	}

	public double getVentasDolaresEfectivo() {
		return this.ventasDolaresEfectivo;
	}

	public void setVentasDolaresEfectivo(double ventasDolaresEfectivo) {
		this.ventasDolaresEfectivo = ventasDolaresEfectivo;
	}

	public double getVentasEurosEfectivo() {
		return this.ventasEurosEfectivo;
	}

	public void setVentasEurosEfectivo(double ventasEurosEfectivo) {
		this.ventasEurosEfectivo = ventasEurosEfectivo;
	}

	public double getVentasColonesTarjeta() {
		return this.ventasColonesTarjeta;
	}

	public void setVentasColonesTarjeta(double ventasColonesTarjeta) {
		this.ventasColonesTarjeta = ventasColonesTarjeta;
	}

	public double getVentasDolaresTarjeta() {
		return this.ventasDolaresTarjeta;
	}

	public void setVentasDolaresTarjeta(double ventasDolaresTarjeta) {
		this.ventasDolaresTarjeta = ventasDolaresTarjeta;
	}

	public double getVentasEurosTarjeta() {
		return this.ventasEurosTarjeta;
	}

	public void setVentasEurosTarjeta(double ventasEurosTarjeta) {
		this.ventasEurosTarjeta = ventasEurosTarjeta;
	}

	public double getVentasColonesCredito() {
		return this.ventasColonesCredito;
	}

	public void setVentasColonesCredito(double ventasColonesCredito) {
		this.ventasColonesCredito = ventasColonesCredito;
	}

	public double getVentasDolaresCredito() {
		return this.ventasDolaresCredito;
	}

	public void setVentasDolaresCredito(double ventasDolaresCredito) {
		this.ventasDolaresCredito = ventasDolaresCredito;
	}

	public double getVentasEurosCredito() {
		return this.ventasEurosCredito;
	}

	public void setVentasEurosCredito(double ventasEurosCredito) {
		this.ventasEurosCredito = ventasEurosCredito;
	}

	public double getVentasColonesCheques() {
		return this.ventasColonesCheques;
	}

	public void setVentasColonesCheques(double ventasColonesCheques) {
		this.ventasColonesCheques = ventasColonesCheques;
	}

	public double getVentasDolaresCheques() {
		return this.ventasDolaresCheques;
	}

	public void setVentasDolaresCheques(double ventasDolaresCheques) {
		this.ventasDolaresCheques = ventasDolaresCheques;
	}

	public double getVentasEurosCheques() {
		return this.ventasEurosCheques;
	}

	public void setVentasEurosCheques(double ventasEurosCheques) {
		this.ventasEurosCheques = ventasEurosCheques;
	}

	public double getVentasColonesDeposito() {
		return this.ventasColonesDeposito;
	}

	public void setVentasColonesDeposito(double ventasColonesDeposito) {
		this.ventasColonesDeposito = ventasColonesDeposito;
	}

	public double getVentasDolaresDeposito() {
		return this.ventasDolaresDeposito;
	}

	public void setVentasDolaresDeposito(double ventasDolaresDeposito) {
		this.ventasDolaresDeposito = ventasDolaresDeposito;
	}

	public double getVentasEurosDeposito() {
		return this.ventasEurosDeposito;
	}

	public void setVentasEurosDeposito(double ventasEurosDeposito) {
		this.ventasEurosDeposito = ventasEurosDeposito;
	}

	public double getCxcColonesEfectivo() {
		return this.cxcColonesEfectivo;
	}

	public void setCxcColonesEfectivo(double cxcColonesEfectivo) {
		this.cxcColonesEfectivo = cxcColonesEfectivo;
	}

	public double getCxcDolaresEfectivo() {
		return this.cxcDolaresEfectivo;
	}

	public void setCxcDolaresEfectivo(double cxcDolaresEfectivo) {
		this.cxcDolaresEfectivo = cxcDolaresEfectivo;
	}

	public double getCxcEurosEfectivo() {
		return this.cxcEurosEfectivo;
	}

	public void setCxcEurosEfectivo(double cxcEurosEfectivo) {
		this.cxcEurosEfectivo = cxcEurosEfectivo;
	}

	public double getCxcColonesTarjeta() {
		return this.cxcColonesTarjeta;
	}

	public void setCxcColonesTarjeta(double cxcColonesTarjeta) {
		this.cxcColonesTarjeta = cxcColonesTarjeta;
	}

	public double getCxcDolaresTarjeta() {
		return this.cxcDolaresTarjeta;
	}

	public void setCxcDolaresTarjeta(double cxcDolaresTarjeta) {
		this.cxcDolaresTarjeta = cxcDolaresTarjeta;
	}

	public double getCxcEurosTarjeta() {
		return this.cxcEurosTarjeta;
	}

	public void setCxcEurosTarjeta(double cxcEurosTarjeta) {
		this.cxcEurosTarjeta = cxcEurosTarjeta;
	}

	public double getCxcColonesDeposito() {
		return this.cxcColonesDeposito;
	}

	public void setCxcColonesDeposito(double cxcColonesDeposito) {
		this.cxcColonesDeposito = cxcColonesDeposito;
	}

	public double getCxcDolaresDeposito() {
		return this.cxcDolaresDeposito;
	}

	public void setCxcDolaresDeposito(double cxcDolaresDeposito) {
		this.cxcDolaresDeposito = cxcDolaresDeposito;
	}

	public double getCxcEurosDeposito() {
		return this.cxcEurosDeposito;
	}

	public void setCxcEurosDeposito(double cxcEurosDeposito) {
		this.cxcEurosDeposito = cxcEurosDeposito;
	}

	public double getCxcColonesCheques() {
		return this.cxcColonesCheques;
	}

	public void setCxcColonesCheques(double cxcColonesCheques) {
		this.cxcColonesCheques = cxcColonesCheques;
	}

	public double getCxcDolaresCheques() {
		return this.cxcDolaresCheques;
	}

	public void setCxcDolaresCheques(double cxcDolaresCheques) {
		this.cxcDolaresCheques = cxcDolaresCheques;
	}

	public double getCxcEurosCheques() {
		return this.cxcEurosCheques;
	}

	public void setCxcEurosCheques(double cxcEurosCheques) {
		this.cxcEurosCheques = cxcEurosCheques;
	}

	public double getCierreColones() {
		return this.cierreColones;
	}

	public void setCierreColones(double cierreColones) {
		this.cierreColones = cierreColones;
	}

	public double getCierreDolares() {
		return this.cierreDolares;
	}

	public void setCierreDolares(double cierreDolares) {
		this.cierreDolares = cierreDolares;
	}

	public double getCierreEuros() {
		return this.cierreEuros;
	}

	public void setCierreEuros(double cierreEuros) {
		this.cierreEuros = cierreEuros;
	}

	public Double getTotalEfectivoColones() {
		return this.totalEfectivoColones;
	}

	public void setTotalEfectivoColones(Double totalEfectivoColones) {
		this.totalEfectivoColones = totalEfectivoColones;
	}

	public Double getTotalEfectivoDolares() {
		return this.totalEfectivoDolares;
	}

	public void setTotalEfectivoDolares(Double totalEfectivoDolares) {
		this.totalEfectivoDolares = totalEfectivoDolares;
	}

	public Double getTotalEfectivoEuros() {
		return this.totalEfectivoEuros;
	}

	public void setTotalEfectivoEuros(Double totalEfectivoEuros) {
		this.totalEfectivoEuros = totalEfectivoEuros;
	}

	public Double getTotalTarjetaColones() {
		return this.totalTarjetaColones;
	}

	public void setTotalTarjetaColones(Double totalTarjetaColones) {
		this.totalTarjetaColones = totalTarjetaColones;
	}

	public Double getTotalTarjetaDolares() {
		return this.totalTarjetaDolares;
	}

	public void setTotalTarjetaDolares(Double totalTarjetaDolares) {
		this.totalTarjetaDolares = totalTarjetaDolares;
	}

	public Double getTotalTarjetaEuros() {
		return this.totalTarjetaEuros;
	}

	public void setTotalTarjetaEuros(Double totalTarjetaEuros) {
		this.totalTarjetaEuros = totalTarjetaEuros;
	}

	public Double getTotalChequeColones() {
		return this.totalChequeColones;
	}

	public void setTotalChequeColones(Double totalChequeColones) {
		this.totalChequeColones = totalChequeColones;
	}

	public Double getTotalChequeDolares() {
		return this.totalChequeDolares;
	}

	public void setTotalChequeDolares(Double totalChequeDolares) {
		this.totalChequeDolares = totalChequeDolares;
	}

	public Double getTotalChequeEuros() {
		return this.totalChequeEuros;
	}

	public void setTotalChequeEuros(Double totalChequeEuros) {
		this.totalChequeEuros = totalChequeEuros;
	}

	public Double getTotalDepositosColones() {
		return this.totalDepositosColones;
	}

	public void setTotalDepositosColones(Double totalDepositosColones) {
		this.totalDepositosColones = totalDepositosColones;
	}

	public Double getTotalDepositosDolares() {
		return this.totalDepositosDolares;
	}

	public void setTotalDepositosDolares(Double totalDepositosDolares) {
		this.totalDepositosDolares = totalDepositosDolares;
	}

	public Double getTotalDepositosEuros() {
		return this.totalDepositosEuros;
	}

	public void setTotalDepositosEuros(Double totalDepositosEuros) {
		this.totalDepositosEuros = totalDepositosEuros;
	}

	public Double getCierreTotalEfectivoColones() {
		return this.cierreTotalEfectivoColones;
	}

	public void setCierreTotalEfectivoColones(Double cierreTotalEfectivoColones) {
		this.cierreTotalEfectivoColones = cierreTotalEfectivoColones;
	}

	public Double getCierreTotalEfectivoDolares() {
		return this.cierreTotalEfectivoDolares;
	}

	public void setCierreTotalEfectivoDolares(Double cierreTotalEfectivoDolares) {
		this.cierreTotalEfectivoDolares = cierreTotalEfectivoDolares;
	}

	public Double getCierreTotalEfectivoEuros() {
		return this.cierreTotalEfectivoEuros;
	}

	public void setCierreTotalEfectivoEuros(Double cierreTotalEfectivoEuros) {
		this.cierreTotalEfectivoEuros = cierreTotalEfectivoEuros;
	}

	public Double getCierreTotalTarjetaColones() {
		return this.cierreTotalTarjetaColones;
	}

	public void setCierreTotalTarjetaColones(Double cierreTotalTarjetaColones) {
		this.cierreTotalTarjetaColones = cierreTotalTarjetaColones;
	}

	public Double getCierreTotalTarjetaDolares() {
		return this.cierreTotalTarjetaDolares;
	}

	public void setCierreTotalTarjetaDolares(Double cierreTotalTarjetaDolares) {
		this.cierreTotalTarjetaDolares = cierreTotalTarjetaDolares;
	}

	public Double getCierreTotalTarjetaEuros() {
		return this.cierreTotalTarjetaEuros;
	}

	public void setCierreTotalTarjetaEuros(Double cierreTotalTarjetaEuros) {
		this.cierreTotalTarjetaEuros = cierreTotalTarjetaEuros;
	}

	public Double getCierreTotalChequeColones() {
		return this.cierreTotalChequeColones;
	}

	public void setCierreTotalChequeColones(Double cierreTotalChequeColones) {
		this.cierreTotalChequeColones = cierreTotalChequeColones;
	}

	public Double getCierreTotalChequeDolares() {
		return this.cierreTotalChequeDolares;
	}

	public void setCierreTotalChequeDolares(Double cierreTotalChequeDolares) {
		this.cierreTotalChequeDolares = cierreTotalChequeDolares;
	}

	public Double getCierreTotalChequeEuros() {
		return this.cierreTotalChequeEuros;
	}

	public void setCierreTotalChequeEuros(Double cierreTotalChequeEuros) {
		this.cierreTotalChequeEuros = cierreTotalChequeEuros;
	}

	public Double getCierreTotalDepositosColones() {
		return this.cierreTotalDepositosColones;
	}

	public void setCierreTotalDepositosColones(Double cierreTotalDepositosColones) {
		this.cierreTotalDepositosColones = cierreTotalDepositosColones;
	}

	public Double getCierreTotalDepositosDolares() {
		return this.cierreTotalDepositosDolares;
	}

	public void setCierreTotalDepositosDolares(Double cierreTotalDepositosDolares) {
		this.cierreTotalDepositosDolares = cierreTotalDepositosDolares;
	}

	public Double getCierreTotalDepositosEuros() {
		return this.cierreTotalDepositosEuros;
	}

	public void setCierreTotalDepositosEuros(Double cierreTotalDepositosEuros) {
		this.cierreTotalDepositosEuros = cierreTotalDepositosEuros;
	}

	public String getEstadoCaja() {
		return this.estadoCaja;
	}

	public void setEstadoCaja(String estadoCaja) {
		this.estadoCaja = estadoCaja;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Double getTotalDevolucionesColones() {
		return this.totalDevolucionesColones;
	}

	public void setTotalDevolucionesColones(Double totalDevolucionesColones) {
		this.totalDevolucionesColones = totalDevolucionesColones;
	}

	public Double getTotalDevolucionesDolares() {
		return this.totalDevolucionesDolares;
	}

	public void setTotalDevolucionesDolares(Double totalDevolucionesDolares) {
		this.totalDevolucionesDolares = totalDevolucionesDolares;
	}

	public Double getTotalDevolucionesEuros() {
		return this.totalDevolucionesEuros;
	}

	public void setTotalDevolucionesEuros(Double totalDevolucionesEuros) {
		this.totalDevolucionesEuros = totalDevolucionesEuros;
	}
}
