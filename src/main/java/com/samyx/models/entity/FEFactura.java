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

import com.samyx.models.entity.CSucursal;
import com.samyx.models.entity.CTerminal;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.FEBitacora;
import com.samyx.models.entity.FEFacturaItem;
import com.samyx.models.entity.FEFacturaOtrosCargos;
import com.samyx.models.entity.FEFacturaReferencia;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Usuario;

@Entity
@Table(name = "fe_facturas")
public class FEFactura {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numero_factura")
	private Long numeroFactura;

	@Column(name = "codigo_actividad_emisor", length = 6)
	private String codigoActividadEmisor;

	@Column(length = 1)
	private String estado;

	@Column(name = "receta_medico", length = 1)
	private String recetaMedico;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "emisor_id")
	private Emisor emisor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@Column(name = "cliente_contado", length = 160)
	private String clienteContado = "";

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "user_id")
	private Usuario usuario;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "factura_id")
	private List<FEFacturaItem> items;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "factura_id")
	private List<FEFacturaReferencia> itemsReferencias;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "factura_id")
	private List<FEBitacora> itemsBitatora;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "factura_id")
	private List<FEFacturaOtrosCargos> itemsOtrosCargos;

	@Column(length = 50)
	private String clave;

	@Column(length = 20)
	private String consecutivo;

	@Column(name = "fecha_emision", length = 30)
	private String fechaEmision;

	@NotNull
	@Column(name = "fecha_limite")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaEmisionFe;

	@Column(name = "tipo_documento", length = 4)
	private String tipoDocumento;

	@Column(length = 10)
	private String situacion;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "sucursal_id")
	private CSucursal sucursal;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "terminal_id")
	private CTerminal terminal;

	@Column(name = "cond_venta", length = 2)
	private String condVenta;

	@Column(name = "plazoCredito", length = 5)
	private String plazoCredito;

	@Column(name = "medioPago", length = 2)
	private String medioPago;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "moneda_id")
	private Moneda moneda;

	@Column(name = "tipoCambio", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double tipoCambio;

	@Column(name = "tipo_cambio_dolar", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double tipoCambioDolar;

	@Column(name = "tipo_cambio_euro", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double tipoCambioEuro;

	@Column(name = "total_serv_gravados", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalServGravados;

	@Column(name = "total_serv_exentos", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalServExentos;

	@Column(name = "total_serv_exonerado", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalServExonerado;

	@Column(name = "total_merc_gravadas", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalMercGravadas;

	@Column(name = "total_merc_exentas", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalMercExentas;

	@Column(name = "total_merc_exonerada", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalMercExonerada;

	@Column(name = "total_gravados", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalGravados;

	@Column(name = "total_exentos", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalExentos;

	@Column(name = "total_exonerado", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalExonerado;

	@Column(name = "total_ventas", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalVentas;

	@Column(name = "total_descuentos", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalDescuentos;

	@Column(name = "total_venta_neta", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalVentaNeta;

	@Column(name = "total_imp", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalImp;

	@Column(name = "total_iva_devuelto", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalIVADevuelto;

	@Column(name = "total_otros_cargos", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalOtrosCargos;

	@Column(name = "total_comprobante", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double totalComprobante;

	@Column(name = "otros", columnDefinition = "TEXT")
	private String otros;

	@Column(name = "pago_con", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double pagoCon;

	@Column(precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double efectivo;

	@Column(precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double tarjeta;

	@Column(name = "numero_tarjeta", length = 4)
	private String numeroTarjeta;

	@Column(name = "numero_autorizacion", length = 50)
	private String numeroAutorizacion;

	@Column(precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double cheque;

	@Column(name = "transferencia", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double transferencia;

	@Column(name = "numero_deposito_transferencia", length = 100)
	private String numeroDepositoTransferencia;

	@Column(name = "su_vueltro", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double suVueltro;

	@Column(name = "efectivo_dolares", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double efectivoDolares;

	@Column(name = "tarjeta_dolares", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double tarjetaDolares;

	@Column(name = "numero_tarjeta_dolares", length = 4)
	private String numeroTarjetaDolares;

	@Column(name = "numero_autorizacion_dolares", length = 50)
	private String numeroAutorizacionDolares;

	@Column(name = "cheque_dolares", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double chequeDolares;

	@Column(name = "transferencia_dolares", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double transferenciaDolares;

	@Column(name = "numero_deposito_transferencia_dolares", length = 100)
	private String numeroDepositoTransferenciaDolares;

	@Column(name = "su_vuelto_dolares", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double suVueltoDolares;

	@Column(name = "efectivo_euros", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double efectivoEuros;

	@Column(name = "tarjeta_euros", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double tarjetaEuros;

	@Column(name = "numero_tarjeta_euros", length = 4)
	private String numeroTarjetaEuros;

	@Column(name = "numero_autorizacion_euros", length = 50)
	private String numeroAutorizacionEuros;

	@Column(name = "cheque_euros", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double chequeEuros;

	@Column(name = "transferencia_euros", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double transferenciaEuros;

	@Column(name = "numero_deposito_transferencia_euros", length = 100)
	private String numeroDepositoTransferenciaEuros;

	@Column(name = "su_vuelto_euros", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double suVueltoEuros;

	@Column(name = "tipo_tarjeta", length = 1)
	private String tipoTarjeta;

	@Column(name = "observacion_pago_factura")
	private String observacionPagoFactura;

	@Column(name = "monto_exento", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double montoExento;

	@Column(precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double exento0;

	@Column(name = "iva_reducida1", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double ivaReducida1;

	@Column(name = "iva_reducida2", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double ivaReducida2;

	@Column(name = "iva_reducida4", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double ivaReducida4;

	@Column(name = "ivatransitorio0", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double ivatransitorio0;

	@Column(name = "ivatransitorio4", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double ivatransitorio4;

	@Column(name = "ivatransitorio8", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double ivatransitorio8;

	@Column(name = "ivageneral13", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double ivageneral13;

	@Column(name = "montoIvaReducida1", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double montoIvaReducida1;

	@Column(name = "montoIvaReducida2", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double montoIvaReducida2;

	@Column(name = "montoIvaReducida4", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double montoIvaReducida4;

	@Column(name = "montoIvatransitorio0", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double montoIvatransitorio0;

	@Column(name = "montoIvatransitorio4", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double montoIvatransitorio4;

	@Column(name = "montoIvatransitorio8", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double montoIvatransitorio8;

	@Column(name = "montoIvageneral13", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double monto_ivageneral13;

	public FEFactura() {
		this.efectivo = Double.valueOf(0.0D);
		this.tarjeta = Double.valueOf(0.0D);
		this.cheque = Double.valueOf(0.0D);
		this.transferencia = Double.valueOf(0.0D);
		this.efectivoDolares = Double.valueOf(0.0D);
		this.tarjetaDolares = Double.valueOf(0.0D);
		this.chequeDolares = Double.valueOf(0.0D);
		this.transferenciaDolares = Double.valueOf(0.0D);
		this.efectivoEuros = Double.valueOf(0.0D);
		this.tarjetaEuros = Double.valueOf(0.0D);
		this.chequeEuros = Double.valueOf(0.0D);
		this.transferenciaEuros = Double.valueOf(0.0D);
		this.items = new ArrayList<>();
		this.itemsReferencias = new ArrayList<>();
		this.itemsBitatora = new ArrayList<>();
		this.itemsOtrosCargos = new ArrayList<>();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroFactura() {
		return this.numeroFactura;
	}

	public String getCodigoActividadEmisor() {
		return this.codigoActividadEmisor;
	}

	public void setCodigoActividadEmisor(String codigoActividadEmisor) {
		this.codigoActividadEmisor = codigoActividadEmisor;
	}

	public void setNumeroFactura(Long numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getRecetaMedico() {
		return this.recetaMedico;
	}

	public void setRecetaMedico(String recetaMedico) {
		this.recetaMedico = recetaMedico;
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

	public String getClienteContado() {
		return this.clienteContado;
	}

	public void setClienteContado(String clienteContado) {
		this.clienteContado = clienteContado;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<FEFacturaItem> getItems() {
		return this.items;
	}

	public void setItems(List<FEFacturaItem> items) {
		this.items = items;
	}

	public List<FEFacturaReferencia> getItemsReferencias() {
		return this.itemsReferencias;
	}

	public void setItemsReferencias(List<FEFacturaReferencia> itemsReferencias) {
		this.itemsReferencias = itemsReferencias;
	}

	public List<FEBitacora> getItemsBitatora() {
		return this.itemsBitatora;
	}

	public void setItemsBitatora(List<FEBitacora> itemsBitatora) {
		this.itemsBitatora = itemsBitatora;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaEmisionFe() {
		return this.fechaEmisionFe;
	}

	public void setFechaEmisionFe(Date fechaEmisionFe) {
		this.fechaEmisionFe = fechaEmisionFe;
	}

	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getSituacion() {
		return this.situacion;
	}

	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	public CSucursal getSucursal() {
		return this.sucursal;
	}

	public void setSucursal(CSucursal sucursal) {
		this.sucursal = sucursal;
	}

	public CTerminal getTerminal() {
		return this.terminal;
	}

	public void setTerminal(CTerminal terminal) {
		this.terminal = terminal;
	}

	public String getCondVenta() {
		return this.condVenta;
	}

	public void setCondVenta(String condVenta) {
		this.condVenta = condVenta;
	}

	public String getPlazoCredito() {
		return this.plazoCredito;
	}

	public void setPlazoCredito(String plazoCredito) {
		this.plazoCredito = plazoCredito;
	}

	public String getMedioPago() {
		return this.medioPago;
	}

	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
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

	public Double getTipoCambioDolar() {
		return this.tipoCambioDolar;
	}

	public void setTipoCambioDolar(Double tipoCambioDolar) {
		this.tipoCambioDolar = tipoCambioDolar;
	}

	public Double getTipoCambioEuro() {
		return this.tipoCambioEuro;
	}

	public void setTipoCambioEuro(Double tipoCambioEuro) {
		this.tipoCambioEuro = tipoCambioEuro;
	}

	public Double getTotalServGravados() {
		return this.totalServGravados;
	}

	public void setTotalServGravados(Double totalServGravados) {
		this.totalServGravados = totalServGravados;
	}

	public Double getTotalServExentos() {
		return this.totalServExentos;
	}

	public void setTotalServExentos(Double totalServExentos) {
		this.totalServExentos = totalServExentos;
	}

	public Double getTotalMercGravadas() {
		return this.totalMercGravadas;
	}

	public void setTotalMercGravadas(Double totalMercGravadas) {
		this.totalMercGravadas = totalMercGravadas;
	}

	public Double getTotalMercExentas() {
		return this.totalMercExentas;
	}

	public void setTotalMercExentas(Double totalMercExentas) {
		this.totalMercExentas = totalMercExentas;
	}

	public Double getTotalGravados() {
		return this.totalGravados;
	}

	public void setTotalGravados(Double totalGravados) {
		this.totalGravados = totalGravados;
	}

	public Double getTotalExentos() {
		return this.totalExentos;
	}

	public void setTotalExentos(Double totalExentos) {
		this.totalExentos = totalExentos;
	}

	public Double getTotalVentas() {
		return this.totalVentas;
	}

	public void setTotalVentas(Double totalVentas) {
		this.totalVentas = totalVentas;
	}

	public Double getTotalDescuentos() {
		return this.totalDescuentos;
	}

	public void setTotalDescuentos(Double totalDescuentos) {
		this.totalDescuentos = totalDescuentos;
	}

	public Double getTotalVentaNeta() {
		return this.totalVentaNeta;
	}

	public void setTotalVentaNeta(Double totalVentaNeta) {
		this.totalVentaNeta = totalVentaNeta;
	}

	public Double getTotalImp() {
		return this.totalImp;
	}

	public void setTotalImp(Double totalImp) {
		this.totalImp = totalImp;
	}

	public Double getTotalComprobante() {
		return this.totalComprobante;
	}

	public void setTotalComprobante(Double totalComprobante) {
		this.totalComprobante = totalComprobante;
	}

	public String getOtros() {
		return this.otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
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

	public Double getPagoCon() {
		return this.pagoCon;
	}

	public void setPagoCon(Double pagoCon) {
		this.pagoCon = pagoCon;
	}

	public Double getSuVueltro() {
		return this.suVueltro;
	}

	public void setSuVueltro(Double suVueltro) {
		this.suVueltro = suVueltro;
	}

	public Double getSuVueltoDolares() {
		return this.suVueltoDolares;
	}

	public void setSuVueltoDolares(Double suVueltoDolares) {
		this.suVueltoDolares = suVueltoDolares;
	}

	public Double getSuVueltoEuros() {
		return this.suVueltoEuros;
	}

	public void setSuVueltoEuros(Double suVueltoEuros) {
		this.suVueltoEuros = suVueltoEuros;
	}

	public void addFEItemFactura(FEFacturaItem item) {
		this.items.add(item);
	}

	public void addReferenciaFactura(FEFacturaReferencia referencia) {
		this.itemsReferencias.add(referencia);
	}

	public void addDocumentoABitacora(FEBitacora bitacora) {
		this.itemsBitatora.add(bitacora);
	}

	public void addOtrosCargos(FEFacturaOtrosCargos otrosCargos) {
		this.itemsOtrosCargos.add(otrosCargos);
	}

	public List<FEFacturaOtrosCargos> getItemsOtrosCargos() {
		return this.itemsOtrosCargos;
	}

	public void setItemsOtrosCargos(List<FEFacturaOtrosCargos> itemsOtrosCargos) {
		this.itemsOtrosCargos = itemsOtrosCargos;
	}

	public Double getTotalServExonerado() {
		return this.totalServExonerado;
	}

	public void setTotalServExonerado(Double totalServExonerado) {
		this.totalServExonerado = totalServExonerado;
	}

	public Double getTotalMercExonerada() {
		return this.totalMercExonerada;
	}

	public void setTotalMercExonerada(Double totalMercExonerada) {
		this.totalMercExonerada = totalMercExonerada;
	}

	public Double getTotalExonerado() {
		return this.totalExonerado;
	}

	public void setTotalExonerado(Double totalExonerado) {
		this.totalExonerado = totalExonerado;
	}

	public Double getTotalIVADevuelto() {
		return this.totalIVADevuelto;
	}

	public void setTotalIVADevuelto(Double totalIVADevuelto) {
		this.totalIVADevuelto = totalIVADevuelto;
	}

	public Double getTotalOtrosCargos() {
		return this.totalOtrosCargos;
	}

	public void setTotalOtrosCargos(Double totalOtrosCargos) {
		this.totalOtrosCargos = totalOtrosCargos;
	}

	public Double getEfectivo() {
		return this.efectivo;
	}

	public void setEfectivo(Double efectivo) {
		this.efectivo = efectivo;
	}

	public Double getTarjeta() {
		return this.tarjeta;
	}

	public void setTarjeta(Double tarjeta) {
		this.tarjeta = tarjeta;
	}

	public Double getCheque() {
		return this.cheque;
	}

	public void setCheque(Double cheque) {
		this.cheque = cheque;
	}

	public Double getTransferencia() {
		return this.transferencia;
	}

	public void setTransferencia(Double transferencia) {
		this.transferencia = transferencia;
	}

	public String getNumeroDepositoTransferencia() {
		return this.numeroDepositoTransferencia;
	}

	public void setNumeroDepositoTransferencia(String numeroDepositoTransferencia) {
		this.numeroDepositoTransferencia = numeroDepositoTransferencia;
	}

	public String getTipoTarjeta() {
		return this.tipoTarjeta;
	}

	public void setTipoTarjeta(String tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}

	public Double getEfectivoDolares() {
		return this.efectivoDolares;
	}

	public void setEfectivoDolares(Double efectivoDolares) {
		this.efectivoDolares = efectivoDolares;
	}

	public Double getTarjetaDolares() {
		return this.tarjetaDolares;
	}

	public void setTarjetaDolares(Double tarjetaDolares) {
		this.tarjetaDolares = tarjetaDolares;
	}

	public String getNumeroTarjetaDolares() {
		return this.numeroTarjetaDolares;
	}

	public void setNumeroTarjetaDolares(String numeroTarjetaDolares) {
		this.numeroTarjetaDolares = numeroTarjetaDolares;
	}

	public String getNumeroAutorizacionDolares() {
		return this.numeroAutorizacionDolares;
	}

	public void setNumeroAutorizacionDolares(String numeroAutorizacionDolares) {
		this.numeroAutorizacionDolares = numeroAutorizacionDolares;
	}

	public Double getChequeDolares() {
		return this.chequeDolares;
	}

	public void setChequeDolares(Double chequeDolares) {
		this.chequeDolares = chequeDolares;
	}

	public Double getTransferenciaDolares() {
		return this.transferenciaDolares;
	}

	public void setTransferenciaDolares(Double transferenciaDolares) {
		this.transferenciaDolares = transferenciaDolares;
	}

	public String getNumeroDepositoTransferenciaDolares() {
		return this.numeroDepositoTransferenciaDolares;
	}

	public void setNumeroDepositoTransferenciaDolares(String numeroDepositoTransferenciaDolares) {
		this.numeroDepositoTransferenciaDolares = numeroDepositoTransferenciaDolares;
	}

	public Double getEfectivoEuros() {
		return this.efectivoEuros;
	}

	public void setEfectivoEuros(Double efectivoEuros) {
		this.efectivoEuros = efectivoEuros;
	}

	public Double getTarjetaEuros() {
		return this.tarjetaEuros;
	}

	public void setTarjetaEuros(Double tarjetaEuros) {
		this.tarjetaEuros = tarjetaEuros;
	}

	public String getNumeroTarjetaEuros() {
		return this.numeroTarjetaEuros;
	}

	public void setNumeroTarjetaEuros(String numeroTarjetaEuros) {
		this.numeroTarjetaEuros = numeroTarjetaEuros;
	}

	public String getNumeroAutorizacionEuros() {
		return this.numeroAutorizacionEuros;
	}

	public void setNumeroAutorizacionEuros(String numeroAutorizacionEuros) {
		this.numeroAutorizacionEuros = numeroAutorizacionEuros;
	}

	public Double getChequeEuros() {
		return this.chequeEuros;
	}

	public void setChequeEuros(Double chequeEuros) {
		this.chequeEuros = chequeEuros;
	}

	public Double getTransferenciaEuros() {
		return this.transferenciaEuros;
	}

	public void setTransferenciaEuros(Double transferenciaEuros) {
		this.transferenciaEuros = transferenciaEuros;
	}

	public String getNumeroDepositoTransferenciaEuros() {
		return this.numeroDepositoTransferenciaEuros;
	}

	public void setNumeroDepositoTransferenciaEuros(String numeroDepositoTransferenciaEuros) {
		this.numeroDepositoTransferenciaEuros = numeroDepositoTransferenciaEuros;
	}

	public String getEstadoMH() {
		String resp = "";
		for (FEBitacora b : this.itemsBitatora)
			resp = b.getEstadoHacienda();
		return resp;
	}

	public String getObservacionPagoFactura() {
		return this.observacionPagoFactura;
	}

	public void setObservacionPagoFactura(String observacionPagoFactura) {
		this.observacionPagoFactura = observacionPagoFactura;
	}

	public Double getExento0() {
		return this.exento0;
	}

	public void setExento0(Double exento0) {
		this.exento0 = exento0;
	}

	public Double getIvaReducida1() {
		return this.ivaReducida1;
	}

	public void setIvaReducida1(Double ivaReducida1) {
		this.ivaReducida1 = ivaReducida1;
	}

	public Double getIvaReducida2() {
		return this.ivaReducida2;
	}

	public void setIvaReducida2(Double ivaReducida2) {
		this.ivaReducida2 = ivaReducida2;
	}

	public Double getIvaReducida4() {
		return this.ivaReducida4;
	}

	public void setIvaReducida4(Double ivaReducida4) {
		this.ivaReducida4 = ivaReducida4;
	}

	public Double getIvatransitorio0() {
		return this.ivatransitorio0;
	}

	public void setIvatransitorio0(Double ivatransitorio0) {
		this.ivatransitorio0 = ivatransitorio0;
	}

	public Double getIvatransitorio4() {
		return this.ivatransitorio4;
	}

	public void setIvatransitorio4(Double ivatransitorio4) {
		this.ivatransitorio4 = ivatransitorio4;
	}

	public Double getIvatransitorio8() {
		return this.ivatransitorio8;
	}

	public void setIvatransitorio8(Double ivatransitorio8) {
		this.ivatransitorio8 = ivatransitorio8;
	}

	public Double getIvageneral13() {
		return this.ivageneral13;
	}

	public void setIvageneral13(Double ivageneral13) {
		this.ivageneral13 = ivageneral13;
	}

	public Double getMontoExento() {
		return this.montoExento;
	}

	public void setMontoExento(Double montoExento) {
		this.montoExento = montoExento;
	}

	public Double getMontoIvaReducida1() {
		return this.montoIvaReducida1;
	}

	public void setMontoIvaReducida1(Double montoIvaReducida1) {
		this.montoIvaReducida1 = montoIvaReducida1;
	}

	public Double getMontoIvaReducida2() {
		return this.montoIvaReducida2;
	}

	public void setMontoIvaReducida2(Double montoIvaReducida2) {
		this.montoIvaReducida2 = montoIvaReducida2;
	}

	public Double getMontoIvaReducida4() {
		return this.montoIvaReducida4;
	}

	public void setMontoIvaReducida4(Double montoIvaReducida4) {
		this.montoIvaReducida4 = montoIvaReducida4;
	}

	public Double getMontoIvatransitorio0() {
		return this.montoIvatransitorio0;
	}

	public void setMontoIvatransitorio0(Double montoIvatransitorio0) {
		this.montoIvatransitorio0 = montoIvatransitorio0;
	}

	public Double getMontoIvatransitorio4() {
		return this.montoIvatransitorio4;
	}

	public void setMontoIvatransitorio4(Double montoIvatransitorio4) {
		this.montoIvatransitorio4 = montoIvatransitorio4;
	}

	public Double getMontoIvatransitorio8() {
		return this.montoIvatransitorio8;
	}

	public void setMontoIvatransitorio8(Double montoIvatransitorio8) {
		this.montoIvatransitorio8 = montoIvatransitorio8;
	}

	public Double getMonto_ivageneral13() {
		return this.monto_ivageneral13;
	}

	public void setMonto_ivageneral13(Double monto_ivageneral13) {
		this.monto_ivageneral13 = monto_ivageneral13;
	}

	public String getRespTipoDocumento() {
		String resp = "";
		String td = this.tipoDocumento;
		switch (td) {
		case "FE":
			resp = "Factura Electrónica";
			break;
		case "ND":
			resp = "Nota de débito Electrónica";
			break;
		case "NC":
			resp = "Nota de crédito Electrónica";
			break;
		case "TE":
			resp = "Tiquete Electrónico";
			break;
		case "FEC":
			resp = "Factura Electrónica Compra";
			break;
		case "FEE":
			resp = "Factura Electrónica Exportación";
			break;
		case "PR":
			resp = "Proforma";
			break;
		}
		return resp;
	}
}
