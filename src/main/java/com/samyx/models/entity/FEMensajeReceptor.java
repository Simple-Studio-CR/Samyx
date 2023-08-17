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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.FEMensajeReceptorItems;
import com.samyx.models.entity.Usuario;

@Entity
@Table(name = "fe_mensaje_receptor", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "clave", "tipo_documento" }) })
public class FEMensajeReceptor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "codigo_actividad_emisor")
	private String codigoActividadEmisor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "emisor_id")
	private Emisor emisor;

	@Column(name = "tipo_documento", length = 4)
	private String tipoDocumento;

	@Column(name = "detalle_mensaje", length = 80)
	private String detalleMensaje;

	@Column(name = "emisor_mr", length = 150)
	private String emisorMr;

	@Column(name = "nombre_comercial_emisor_mr", length = 150)
	private String nombreComercialEmisorMr;

	@Column(name = "tipo_identificacion_emisor_mr", length = 2)
	private String tipoIdentificacionEmisorMr;

	@Column(name = "identificacion_emisor_mr", length = 15)
	private String identificacionEmisorMr;

	@Column(name = "correo_emisor_mr", length = 100)
	private String correoEmisorMr;

	@Column(name = "nombre_receptor_mr", length = 150)
	private String nombreReceptorMr;

	@Column(name = "nombre_comercial_receptor_mr", length = 150)
	private String nombreComercialReceptorMr;

	@Column(name = "tipo_identificacion_receptor_mr", length = 2)
	private String tipoIdentificacionReceptorMr;

	@Column(name = "identificacion_receptor_mr", length = 15)
	private String identificacionReceptorMr;

	@Column(name = "codigo_actividad", length = 6)
	private String codigoActividad;

	@Column(length = 50)
	private String clave;

	@Column(name = "condicion_impuesto", length = 2)
	public String condicionImpuesto;

	@Column(name = "medio_pago", length = 2)
	public String medioPago;

	@Column(name = "monto_total_impuesto_acreditar", precision = 18, scale = 5)
	public Double montoTotalImpuestoAcreditar;

	@Column(name = "monto_total_de_gasto_aplicable", precision = 18, scale = 5)
	public Double montoTotalDeGastoAplicable;

	@Column(name = "fecha_emision", length = 40)
	private String fechaEmision;

	@Column(length = 5)
	private String moneda;

	@Column(name = "tipoCambio", precision = 18, scale = 5)
	private double tipoCambio;

	@Column(name = "total_serv_gravados", precision = 18, scale = 5)
	private Double totalServGravados = Double.valueOf(0.0D);

	@Column(name = "total_serv_exentos", precision = 18, scale = 5)
	private double totalServExentos = 0.0D;

	@Column(name = "total_serv_exonerado", precision = 18, scale = 5)
	private double totalServExonerado = 0.0D;

	@Column(name = "total_merc_gravadas", precision = 18, scale = 5)
	private double totalMercGravadas = 0.0D;

	@Column(name = "total_merc_exentas", precision = 18, scale = 5)
	private double totalMercExentas = 0.0D;

	@Column(name = "total_merc_exonerada", precision = 18, scale = 5)
	private double totalMercExonerada = 0.0D;

	@Column(name = "total_gravados", precision = 18, scale = 5)
	private double totalGravados = 0.0D;

	@Column(name = "total_exentos", precision = 18, scale = 5)
	private double totalExentos = 0.0D;

	@Column(name = "total_exonerado", precision = 18, scale = 5)
	private double totalExonerado = 0.0D;

	@Column(name = "total_ventas", precision = 18, scale = 5)
	private double totalVentas = 0.0D;

	@Column(name = "total_descuentos", precision = 18, scale = 5)
	private double totalDescuentos = 0.0D;

	@Column(name = "total_venta_neta", precision = 18, scale = 5)
	private double totalVentaNeta = 0.0D;

	@Column(precision = 18, scale = 5)
	private double impuestos = 0.0D;

	@Column(name = "total_iva_devuelto", precision = 18, scale = 5)
	private double totalIVADevuelto = 0.0D;

	@Column(name = "total_otros_cargos", length = 20)
	private double totalOtrosCargos = 0.0D;

	@Column(name = "total_factura", precision = 18, scale = 5)
	private Double totalFactura = Double.valueOf(0.0D);

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "user_id")
	private Usuario usuario;

	@NotNull
	@Column(name = "fecha_creacion")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaCreacion;

	@Column(name = "clave_generada", length = 50)
	private String claveGenerada;

	@Column(name = "consecutivo_generado", length = 20)
	private String consecutivoGenerado;

	@Column(name = "fecha_emision_generado", length = 40)
	private String fechaEmisionGenerado;

	@Column(name = "file_xml_sign", length = 100)
	private String fileXmlSign;

	@Column(name = "fecha_aceptacion", length = 40)
	private String fechaAceptacion;

	@Column(name = "fileXml_aceptacion", length = 100)
	private String fileXmlAceptacion;

	@Column(name = "estado_hacienda", length = 15)
	private String estadoHacienda;

	@Column(name = "response_code", length = 3)
	private int responseCode;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "mr_id")
	private List<FEMensajeReceptorItems> items;

	@Column(name = "condicion_venta", length = 2)
	private String condicionVenta;

	@Column(name = "plazo_credito", length = 10)
	private String plazoCredito;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_aplico_id")
	private Usuario usuarioAplico;

	@Column(name = "estado_inventario", length = 1)
	private String estadoInventario;

	@Column(name = "fecha_aplicacion_inventario")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaAplicacionInventario;

	public FEMensajeReceptor() {
		this.items = new ArrayList<>();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoActividadEmisor() {
		return this.codigoActividadEmisor;
	}

	public void setCodigoActividadEmisor(String codigoActividadEmisor) {
		this.codigoActividadEmisor = codigoActividadEmisor;
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDetalleMensaje() {
		return this.detalleMensaje;
	}

	public void setDetalleMensaje(String detalleMensaje) {
		this.detalleMensaje = detalleMensaje;
	}

	public String getEmisorMr() {
		return this.emisorMr;
	}

	public void setEmisorMr(String emisorMr) {
		this.emisorMr = emisorMr;
	}

	public String getNombreComercialEmisorMr() {
		return this.nombreComercialEmisorMr;
	}

	public void setNombreComercialEmisorMr(String nombreComercialEmisorMr) {
		this.nombreComercialEmisorMr = nombreComercialEmisorMr;
	}

	public String getTipoIdentificacionEmisorMr() {
		return this.tipoIdentificacionEmisorMr;
	}

	public void setTipoIdentificacionEmisorMr(String tipoIdentificacionEmisorMr) {
		this.tipoIdentificacionEmisorMr = tipoIdentificacionEmisorMr;
	}

	public String getIdentificacionEmisorMr() {
		return this.identificacionEmisorMr;
	}

	public void setIdentificacionEmisorMr(String identificacionEmisorMr) {
		this.identificacionEmisorMr = identificacionEmisorMr;
	}

	public String getCorreoEmisorMr() {
		return this.correoEmisorMr;
	}

	public void setCorreoEmisorMr(String correoEmisorMr) {
		this.correoEmisorMr = correoEmisorMr;
	}

	public String getNombreReceptorMr() {
		return this.nombreReceptorMr;
	}

	public void setNombreReceptorMr(String nombreReceptorMr) {
		this.nombreReceptorMr = nombreReceptorMr;
	}

	public String getNombreComercialReceptorMr() {
		return this.nombreComercialReceptorMr;
	}

	public void setNombreComercialReceptorMr(String nombreComercialReceptorMr) {
		this.nombreComercialReceptorMr = nombreComercialReceptorMr;
	}

	public String getTipoIdentificacionReceptorMr() {
		return this.tipoIdentificacionReceptorMr;
	}

	public void setTipoIdentificacionReceptorMr(String tipoIdentificacionReceptorMr) {
		this.tipoIdentificacionReceptorMr = tipoIdentificacionReceptorMr;
	}

	public String getIdentificacionReceptorMr() {
		return this.identificacionReceptorMr;
	}

	public void setIdentificacionReceptorMr(String identificacionReceptorMr) {
		this.identificacionReceptorMr = identificacionReceptorMr;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCodigoActividad() {
		return this.codigoActividad;
	}

	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	public String getCondicionImpuesto() {
		return this.condicionImpuesto;
	}

	public void setCondicionImpuesto(String condicionImpuesto) {
		this.condicionImpuesto = condicionImpuesto;
	}

	public Double getMontoTotalImpuestoAcreditar() {
		return this.montoTotalImpuestoAcreditar;
	}

	public void setMontoTotalImpuestoAcreditar(Double montoTotalImpuestoAcreditar) {
		this.montoTotalImpuestoAcreditar = montoTotalImpuestoAcreditar;
	}

	public Double getMontoTotalDeGastoAplicable() {
		return this.montoTotalDeGastoAplicable;
	}

	public void setMontoTotalDeGastoAplicable(Double montoTotalDeGastoAplicable) {
		this.montoTotalDeGastoAplicable = montoTotalDeGastoAplicable;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Double getImpuestos() {
		return Double.valueOf(this.impuestos);
	}

	public void setImpuestos(Double impuestos) {
		this.impuestos = impuestos.doubleValue();
	}

	public Double getTotalFactura() {
		return this.totalFactura;
	}

	public void setTotalFactura(Double totalFactura) {
		this.totalFactura = totalFactura;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getClaveGenerada() {
		return this.claveGenerada;
	}

	public void setClaveGenerada(String claveGenerada) {
		this.claveGenerada = claveGenerada;
	}

	public String getConsecutivoGenerado() {
		return this.consecutivoGenerado;
	}

	public void setConsecutivoGenerado(String consecutivoGenerado) {
		this.consecutivoGenerado = consecutivoGenerado;
	}

	public String getFechaEmisionGenerado() {
		return this.fechaEmisionGenerado;
	}

	public void setFechaEmisionGenerado(String fechaEmisionGenerado) {
		this.fechaEmisionGenerado = fechaEmisionGenerado;
	}

	public String getFileXmlSign() {
		return this.fileXmlSign;
	}

	public void setFileXmlSign(String fileXmlSign) {
		this.fileXmlSign = fileXmlSign;
	}

	public String getFechaAceptacion() {
		return this.fechaAceptacion;
	}

	public void setFechaAceptacion(String fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}

	public String getFileXmlAceptacion() {
		return this.fileXmlAceptacion;
	}

	public void setFileXmlAceptacion(String fileXmlAceptacion) {
		this.fileXmlAceptacion = fileXmlAceptacion;
	}

	public String getEstadoHacienda() {
		return this.estadoHacienda;
	}

	public void setEstadoHacienda(String estadoHacienda) {
		this.estadoHacienda = estadoHacienda;
	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public void addFEItemsMR(FEMensajeReceptorItems item) {
		this.items.add(item);
	}

	public List<FEMensajeReceptorItems> getItems() {
		return this.items;
	}

	public void setItems(List<FEMensajeReceptorItems> items) {
		this.items = items;
	}

	public String getCondicionVenta() {
		return this.condicionVenta;
	}

	public void setCondicionVenta(String condicionVenta) {
		this.condicionVenta = condicionVenta;
	}

	public String getPlazoCredito() {
		return this.plazoCredito;
	}

	public void setPlazoCredito(String plazoCredito) {
		this.plazoCredito = plazoCredito;
	}

	public Double getTipoCambio() {
		return Double.valueOf(this.tipoCambio);
	}

	public void setTipoCambio(Double tipoCambio) {
		this.tipoCambio = tipoCambio.doubleValue();
	}

	public double getTotalServGravados() {
		return this.totalServGravados.doubleValue();
	}

	public void setTotalServGravados(double totalServGravados) {
		this.totalServGravados = Double.valueOf(totalServGravados);
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

	public String getMedioPago() {
		return this.medioPago;
	}

	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public void setTotalServGravados(Double totalServGravados) {
		this.totalServGravados = totalServGravados;
	}

	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}

	public String getEstadoInventario() {
		return this.estadoInventario;
	}

	public void setEstadoInventario(String estadoInventario) {
		this.estadoInventario = estadoInventario;
	}

	public Usuario getUsuarioAplico() {
		return this.usuarioAplico;
	}

	public void setUsuarioAplico(Usuario usuarioAplico) {
		this.usuarioAplico = usuarioAplico;
	}

	public Date getFechaAplicacionInventario() {
		return this.fechaAplicacionInventario;
	}

	public void setFechaAplicacionInventario(Date fechaAplicacionInventario) {
		this.fechaAplicacionInventario = fechaAplicacionInventario;
	}
}
