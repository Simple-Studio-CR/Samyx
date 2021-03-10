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
import org.springframework.format.annotation.DateTimeFormat;

import com.samyx.models.entity.FEMensajeReceptorItemsImpuestos;
import com.samyx.models.entity.Producto;
import com.samyx.models.entity.Usuario;

@Entity
@Table(name = "fe_mensaje_receptor_items")
public class FEMensajeReceptorItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "item_factura_id")
	private List<FEMensajeReceptorItemsImpuestos> impuestosItemFactura;

	@Column(name = "numero_linea")
	private int numeroLinea;

	@Column(name = "codigo", length = 20)
	private String codigo;

	@Column(name = "detalle_producto", length = 250)
	private String detalleProducto;

	@Column(precision = 18, scale = 5)
	private Double cantidad;

	@Column(name = "unidad_de_medida", length = 10)
	private String unidadDeMedida;

	@Column(name = "precio_unitario", precision = 18, scale = 5)
	private Double precioUnitario;

	@Column(name = "monto_total", precision = 18, scale = 5)
	private Double montoTotal;

	@Column(name = "sub_total", precision = 18, scale = 5)
	private Double subTotal;

	@Column(name = "monto_descuento", precision = 18, scale = 5)
	private Double montoDescuento;

	@Column(name = "naturaleza_descuento")
	private String naturalezaDescuento;

	@Column(name = "impuesto_neto", precision = 20, scale = 3)
	private Double impuestoNeto;

	@Column(name = "monto_total_linea", precision = 18, scale = 5)
	private Double montoTotalLinea;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "producto_id")
	private Producto producto;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_aplico_id")
	private Usuario usuarioAplico;

	@Column(precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double utilidad;

	@Column(name = "precio_actual_catalogo", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double precioActualCatalogo;

	@Column(name = "utilidad_fraccion", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double utilidadFraccion;

	@Column(name = "precio_venta_sin_iva", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double precioVentaSinIva;

	@Column(name = "precio_venta_fraccion_sin_iva", precision = 18, scale = 5, columnDefinition = "Decimal(18,5) default '0.00'")
	private Double precioVentaFraccionSinIva;

	@Column(name = "estado_inventario_linea", length = 1)
	public String estadoInventarioLinea;

	@Column(name = "fecha_aplicacion_inventario")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaAplicacionInventario;

	public Double getTotalImpuestos() {
		Double response = Double.valueOf(0.0D);
		for (FEMensajeReceptorItemsImpuestos i : this.impuestosItemFactura) {
			if (i.getImpuesto().getId().longValue() == 1L)
				response = Double.valueOf(response.doubleValue() + i.getMonto().doubleValue());
		}
		return response;
	}

	public FEMensajeReceptorItems() {
		this.impuestosItemFactura = new ArrayList<>();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumeroLinea() {
		return this.numeroLinea;
	}

	public void setNumeroLinea(int numeroLinea) {
		this.numeroLinea = numeroLinea;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDetalleProducto() {
		return this.detalleProducto;
	}

	public void setDetalleProducto(String detalleProducto) {
		this.detalleProducto = detalleProducto;
	}

	public Double getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidadDeMedida() {
		return this.unidadDeMedida;
	}

	public void setUnidadDeMedida(String unidadDeMedida) {
		this.unidadDeMedida = unidadDeMedida;
	}

	public Double getPrecioUnitario() {
		return this.precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Double getMontoTotal() {
		return this.montoTotal;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public Double getSubTotal() {
		return this.subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getMontoDescuento() {
		return this.montoDescuento;
	}

	public void setMontoDescuento(Double montoDescuento) {
		this.montoDescuento = montoDescuento;
	}

	public String getNaturalezaDescuento() {
		return this.naturalezaDescuento;
	}

	public void setNaturalezaDescuento(String naturalezaDescuento) {
		this.naturalezaDescuento = naturalezaDescuento;
	}

	public Double getImpuestoNeto() {
		return this.impuestoNeto;
	}

	public void setImpuestoNeto(Double impuestoNeto) {
		this.impuestoNeto = impuestoNeto;
	}

	public Double getMontoTotalLinea() {
		return this.montoTotalLinea;
	}

	public void setMontoTotalLinea(Double montoTotalLinea) {
		this.montoTotalLinea = montoTotalLinea;
	}

	public List<FEMensajeReceptorItemsImpuestos> getImpuestosItemFactura() {
		return this.impuestosItemFactura;
	}

	public void setImpuestosItemFactura(List<FEMensajeReceptorItemsImpuestos> impuestosItemFactura) {
		this.impuestosItemFactura = impuestosItemFactura;
	}

	public void addFEImpuestosItemFactura(FEMensajeReceptorItemsImpuestos item) {
		this.impuestosItemFactura.add(item);
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Usuario getUsuarioAplico() {
		return this.usuarioAplico;
	}

	public void setUsuarioAplico(Usuario usuarioAplico) {
		this.usuarioAplico = usuarioAplico;
	}

	public Double getUtilidad() {
		return this.utilidad;
	}

	public void setUtilidad(Double utilidad) {
		this.utilidad = utilidad;
	}

	public Double getUtilidadFraccion() {
		return this.utilidadFraccion;
	}

	public void setUtilidadFraccion(Double utilidadFraccion) {
		this.utilidadFraccion = utilidadFraccion;
	}

	public Double getPrecioVentaSinIva() {
		return this.precioVentaSinIva;
	}

	public void setPrecioVentaSinIva(Double precioVentaSinIva) {
		this.precioVentaSinIva = precioVentaSinIva;
	}

	public Double getPrecioVentaFraccionSinIva() {
		return this.precioVentaFraccionSinIva;
	}

	public void setPrecioVentaFraccionSinIva(Double precioVentaFraccionSinIva) {
		this.precioVentaFraccionSinIva = precioVentaFraccionSinIva;
	}

	public String getEstadoInventarioLinea() {
		return this.estadoInventarioLinea;
	}

	public void setEstadoInventarioLinea(String estadoInventarioLinea) {
		this.estadoInventarioLinea = estadoInventarioLinea;
	}

	public Date getFechaAplicacionInventario() {
		return this.fechaAplicacionInventario;
	}

	public void setFechaAplicacionInventario(Date fechaAplicacionInventario) {
		this.fechaAplicacionInventario = fechaAplicacionInventario;
	}

	public Double getPrecioActualCatalogo() {
		return this.precioActualCatalogo;
	}

	public void setPrecioActualCatalogo(Double precioActualCatalogo) {
		this.precioActualCatalogo = precioActualCatalogo;
	}
}
