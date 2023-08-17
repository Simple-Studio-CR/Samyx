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

import com.samyx.models.entity.Emisor;

@Entity
@Table(name = "historico_otros_sistemas")
public class HistoricoOtrosSistemas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emisor_id")
	private Emisor emisor;

	@Column(length = 20)
	private String consecutivo;

	@Column(length = 50)
	private String clave;

	@Column(length = 20)
	private String factura;

	@Column(length = 6, name = "actividad_economica")
	private String actividadEconomica;

	@Column(name = "tipo_documento", length = 50)
	private String tipoDocumento;

	@Column(name = "fecha_emision", length = 50)
	private String fechaEmision;

	@Column(name = "nombre_receptor", length = 160)
	private String nombreReceptor;

	@Column(name = "tipo_cedula_receptor", length = 2)
	private String tipoCedulaReceptor;

	@Column(name = "cedula_receptor", length = 20)
	private String cedulaReceptor;

	@Column(name = "condicion_venta", length = 50)
	private String condicionVenta;

	@Column(name = "estado_hacienda", length = 50)
	private String estadoHacienda;

	@Column(length = 20)
	private String moneda;

	@Column(name = "medio_pago", length = 50)
	private String medioPago;

	@Column(name = "tipo_cambio", length = 20)
	private String tipoCambio;

	@Column(name = "total_ser_exentos", length = 20)
	private String totalSerExentos;

	@Column(name = "total_ser_gravados", length = 20)
	private String TotalSerGravados;

	@Column(name = "total_ser_exonerados", length = 20)
	private String totalSerExonerados;

	@Column(name = "total_mer_exentas", length = 20)
	private String totalMerExentas;

	@Column(name = "total_mer_gravadas", length = 20)
	private String totalMerGravadas;

	@Column(name = "total_mer_exoneradas", length = 20)
	private String totalMerExoneradas;

	@Column(name = "total_exento", length = 20)
	private String totalExento;

	@Column(name = "total_gravado", length = 20)
	private String totalGravado;

	@Column(name = "total_exonerado", length = 20)
	private String totalExonerado;

	@Column(name = "total_venta", length = 20)
	private String totalVenta;

	@Column(name = "total_descuentos", length = 20)
	private String totalDescuentos;

	@Column(name = "total_venta_neta", length = 20)
	private String totalVentaNeta;

	@Column(name = "total_impuesto", length = 20)
	private String totalImpuesto;

	@Column(name = "total_iva_devuelto", length = 20)
	private String totalIVADevuelto;

	@Column(name = "total_otros_cargos", length = 20)
	private String totalOtrosCargos;

	@Column(name = "total_comprobante", length = 20)
	private String totalComprobante;

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

	public String getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getFactura() {
		return this.factura;
	}

	public void setFactura(String factura) {
		this.factura = factura;
	}

	public String getActividadEconomica() {
		return this.actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getNombreReceptor() {
		return this.nombreReceptor;
	}

	public void setNombreReceptor(String nombreReceptor) {
		this.nombreReceptor = nombreReceptor;
	}

	public String getTipoCedulaReceptor() {
		return this.tipoCedulaReceptor;
	}

	public void setTipoCedulaReceptor(String tipoCedulaReceptor) {
		this.tipoCedulaReceptor = tipoCedulaReceptor;
	}

	public String getCedulaReceptor() {
		return this.cedulaReceptor;
	}

	public void setCedulaReceptor(String cedulaReceptor) {
		this.cedulaReceptor = cedulaReceptor;
	}

	public String getCondicionVenta() {
		return this.condicionVenta;
	}

	public void setCondicionVenta(String condicionVenta) {
		this.condicionVenta = condicionVenta;
	}

	public String getEstadoHacienda() {
		return this.estadoHacienda;
	}

	public void setEstadoHacienda(String estadoHacienda) {
		this.estadoHacienda = estadoHacienda;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMedioPago() {
		return this.medioPago;
	}

	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}

	public String getTipoCambio() {
		return this.tipoCambio;
	}

	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public String getTotalSerExentos() {
		return this.totalSerExentos;
	}

	public void setTotalSerExentos(String totalSerExentos) {
		this.totalSerExentos = totalSerExentos;
	}

	public String getTotalSerGravados() {
		return this.TotalSerGravados;
	}

	public void setTotalSerGravados(String totalSerGravados) {
		this.TotalSerGravados = totalSerGravados;
	}

	public String getTotalSerExonerados() {
		return this.totalSerExonerados;
	}

	public void setTotalSerExonerados(String totalSerExonerados) {
		this.totalSerExonerados = totalSerExonerados;
	}

	public String getTotalMerExentas() {
		return this.totalMerExentas;
	}

	public void setTotalMerExentas(String totalMerExentas) {
		this.totalMerExentas = totalMerExentas;
	}

	public String getTotalMerGravadas() {
		return this.totalMerGravadas;
	}

	public void setTotalMerGravadas(String totalMerGravadas) {
		this.totalMerGravadas = totalMerGravadas;
	}

	public String getTotalMerExoneradas() {
		return this.totalMerExoneradas;
	}

	public void setTotalMerExoneradas(String totalMerExoneradas) {
		this.totalMerExoneradas = totalMerExoneradas;
	}

	public String getTotalExento() {
		return this.totalExento;
	}

	public void setTotalExento(String totalExento) {
		this.totalExento = totalExento;
	}

	public String getTotalGravado() {
		return this.totalGravado;
	}

	public void setTotalGravado(String totalGravado) {
		this.totalGravado = totalGravado;
	}

	public String getTotalExonerado() {
		return this.totalExonerado;
	}

	public void setTotalExonerado(String totalExonerado) {
		this.totalExonerado = totalExonerado;
	}

	public String getTotalVenta() {
		return this.totalVenta;
	}

	public void setTotalVenta(String totalVenta) {
		this.totalVenta = totalVenta;
	}

	public String getTotalDescuentos() {
		return this.totalDescuentos;
	}

	public void setTotalDescuentos(String totalDescuentos) {
		this.totalDescuentos = totalDescuentos;
	}

	public String getTotalVentaNeta() {
		return this.totalVentaNeta;
	}

	public void setTotalVentaNeta(String totalVentaNeta) {
		this.totalVentaNeta = totalVentaNeta;
	}

	public String getTotalImpuesto() {
		return this.totalImpuesto;
	}

	public void setTotalImpuesto(String totalImpuesto) {
		this.totalImpuesto = totalImpuesto;
	}

	public String getTotalIVADevuelto() {
		return this.totalIVADevuelto;
	}

	public void setTotalIVADevuelto(String totalIVADevuelto) {
		this.totalIVADevuelto = totalIVADevuelto;
	}

	public String getTotalOtrosCargos() {
		return this.totalOtrosCargos;
	}

	public void setTotalOtrosCargos(String totalOtrosCargos) {
		this.totalOtrosCargos = totalOtrosCargos;
	}

	public String getTotalComprobante() {
		return this.totalComprobante;
	}

	public void setTotalComprobante(String totalComprobante) {
		this.totalComprobante = totalComprobante;
	}
}
