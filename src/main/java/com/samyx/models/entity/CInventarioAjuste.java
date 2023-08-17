package com.samyx.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import com.samyx.models.entity.CInventarioAjusteItems;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Usuario;

@Entity
@Table(name = "c_inventario_movimientos_ajustes")
public class CInventarioAjuste {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long numeroAjuste = Long.valueOf(Long.parseLong("0"));

	@Column(name = "tipo_movimiento", length = 1)
	private String tipoMovimiento;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "emisor_id")
	private Emisor emisor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "user_id")
	private Usuario usuario;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "cinventario_movimiento_id")
	private List<CInventarioAjusteItems> items;

	@NotNull
	@Column(name = "fecha_emision_fe")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaEmisionFe;

	@NotNull
	@Column(name = "fecha_ingreso_sistema")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaIngresoSistema;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "moneda_id")
	private Moneda moneda;

	@Column(precision = 5, scale = 2)
	private Double tipoCambio;

	@Column(length = 255)
	private String observaciones;

	@Column(name = "total_serv_gravados", precision = 18, scale = 5)
	private double totalServGravados;

	@Column(name = "total_serv_exentos", precision = 18, scale = 5)
	private double totalServExentos;

	@Column(name = "total_serv_exonerado", precision = 18, scale = 5)
	private double totalServExonerado;

	@Column(name = "total_merc_gravadas", precision = 18, scale = 5)
	private double totalMercGravadas;

	@Column(name = "total_merc_exentas", precision = 18, scale = 5)
	private double totalMercExentas;

	@Column(name = "total_merc_exonerada", precision = 18, scale = 5)
	private double totalMercExonerada;

	@Column(name = "total_gravados", precision = 18, scale = 5)
	private double totalGravados;

	@Column(name = "total_exentos", precision = 18, scale = 5)
	private double totalExentos;

	@Column(name = "total_exonerado", precision = 18, scale = 5)
	private double totalExonerado;

	@Column(name = "total_ventas", precision = 18, scale = 5)
	private double totalVentas;

	@Column(name = "total_descuentos", precision = 18, scale = 5)
	private double totalDescuentos;

	@Column(name = "total_venta_neta", precision = 18, scale = 5)
	private double totalVentaNeta;

	@Column(name = "total_imp", precision = 18, scale = 5)
	private double totalImp;

	@Column(name = "total_iva_devuelto", precision = 18, scale = 5)
	private double totalIVADevuelto;

	@Column(name = "total_otros_cargos", length = 20)
	private double totalOtrosCargos;

	@Column(name = "total_comprobante", precision = 18, scale = 5)
	private double totalComprobante;

	public CInventarioAjuste() {
		this.items = new ArrayList<>();
	}

	public void addCInventarioAjusteItems(CInventarioAjusteItems item) {
		this.items.add(item);
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroAjuste() {
		return this.numeroAjuste;
	}

	public void setNumeroAjuste(Long numeroAjuste) {
		this.numeroAjuste = numeroAjuste;
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<CInventarioAjusteItems> getItems() {
		return this.items;
	}

	public void setItems(List<CInventarioAjusteItems> items) {
		this.items = items;
	}

	public String getTipoMovimiento() {
		return this.tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Date getFechaEmisionFe() {
		return this.fechaEmisionFe;
	}

	public void setFechaEmisionFe(Date fechaEmisionFe) {
		this.fechaEmisionFe = fechaEmisionFe;
	}

	public Date getFechaIngresoSistema() {
		return this.fechaIngresoSistema;
	}

	public void setFechaIngresoSistema(Date fechaIngresoSistema) {
		this.fechaIngresoSistema = fechaIngresoSistema;
	}

	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public Double getTipoCambio() {
		return this.tipoCambio;
	}

	public void setTipoCambio(Double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public double getTotalServGravados() {
		return this.totalServGravados;
	}

	public void setTotalServGravados(double totalServGravados) {
		this.totalServGravados = totalServGravados;
	}

	public double getTotalServExentos() {
		return this.totalServExentos;
	}

	public void setTotalServExentos(double totalServExentos) {
		this.totalServExentos = totalServExentos;
	}

	public double getTotalServExonerado() {
		return this.totalServExonerado;
	}

	public void setTotalServExonerado(double totalServExonerado) {
		this.totalServExonerado = totalServExonerado;
	}

	public double getTotalMercGravadas() {
		return this.totalMercGravadas;
	}

	public void setTotalMercGravadas(double totalMercGravadas) {
		this.totalMercGravadas = totalMercGravadas;
	}

	public double getTotalMercExentas() {
		return this.totalMercExentas;
	}

	public void setTotalMercExentas(double totalMercExentas) {
		this.totalMercExentas = totalMercExentas;
	}

	public double getTotalMercExonerada() {
		return this.totalMercExonerada;
	}

	public void setTotalMercExonerada(double totalMercExonerada) {
		this.totalMercExonerada = totalMercExonerada;
	}

	public double getTotalGravados() {
		return this.totalGravados;
	}

	public void setTotalGravados(double totalGravados) {
		this.totalGravados = totalGravados;
	}

	public double getTotalExentos() {
		return this.totalExentos;
	}

	public void setTotalExentos(double totalExentos) {
		this.totalExentos = totalExentos;
	}

	public double getTotalExonerado() {
		return this.totalExonerado;
	}

	public void setTotalExonerado(double totalExonerado) {
		this.totalExonerado = totalExonerado;
	}

	public double getTotalVentas() {
		return this.totalVentas;
	}

	public void setTotalVentas(double totalVentas) {
		this.totalVentas = totalVentas;
	}

	public double getTotalDescuentos() {
		return this.totalDescuentos;
	}

	public void setTotalDescuentos(double totalDescuentos) {
		this.totalDescuentos = totalDescuentos;
	}

	public double getTotalVentaNeta() {
		return this.totalVentaNeta;
	}

	public void setTotalVentaNeta(double totalVentaNeta) {
		this.totalVentaNeta = totalVentaNeta;
	}

	public double getTotalImp() {
		return this.totalImp;
	}

	public void setTotalImp(double totalImp) {
		this.totalImp = totalImp;
	}

	public double getTotalIVADevuelto() {
		return this.totalIVADevuelto;
	}

	public void setTotalIVADevuelto(double totalIVADevuelto) {
		this.totalIVADevuelto = totalIVADevuelto;
	}

	public double getTotalOtrosCargos() {
		return this.totalOtrosCargos;
	}

	public void setTotalOtrosCargos(double totalOtrosCargos) {
		this.totalOtrosCargos = totalOtrosCargos;
	}

	public double getTotalComprobante() {
		return this.totalComprobante;
	}

	public void setTotalComprobante(double totalComprobante) {
		this.totalComprobante = totalComprobante;
	}

	public String getTipoMovimientoFinal() {
		String resp = "";
		if (this.tipoMovimiento.equalsIgnoreCase("E")) {
			resp = "Entrada de producto";
		} else {
			resp = "Salida de producto";
		}
		return resp;
	}
}
