package com.samyx.models.entity;

import java.util.ArrayList;
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

import com.samyx.models.entity.CInventarioMovimientoItemsImpuesto;
import com.samyx.models.entity.Producto;

@Entity
@Table(name = "c_inventario_movimientos_items")
public class CInventarioMovimientoItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "item_factura_id")
	private List<CInventarioMovimientoItemsImpuesto> impuestosItem;

	@Column(name = "numero_linea")
	private int numeroLinea;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "producto_id")
	private Producto producto;

	@Column(name = "detalle_producto", length = 255)
	private String detalleProducto;

	@Column(precision = 18, scale = 5)
	private Double cantidad;

	@Column(name = "precio_compra", precision = 18, scale = 5)
	private Double precioCompra;

	@Column(name = "precio_promediado", precision = 18, scale = 5)
	private Double precioPromediado;

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

	@Column(name = "monto_total_linea", precision = 18, scale = 5)
	private Double montoTotalLinea;

	public Double getTotalImpuestos() {
		Double response = Double.valueOf(0.0D);
		for (CInventarioMovimientoItemsImpuesto i : this.impuestosItem) {
			if (i.getImpuesto().getId().longValue() == 1L)
				response = Double.valueOf(response.doubleValue() + i.getMonto().doubleValue());
		}
		return response;
	}

	public CInventarioMovimientoItems() {
		this.impuestosItem = new ArrayList<>();
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

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public List<CInventarioMovimientoItemsImpuesto> getImpuestosItem() {
		return this.impuestosItem;
	}

	public void setImpuestosItem(List<CInventarioMovimientoItemsImpuesto> impuestosItem) {
		this.impuestosItem = impuestosItem;
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

	public Double getPrecioCompra() {
		return this.precioCompra;
	}

	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public Double getPrecioPromediado() {
		return this.precioPromediado;
	}

	public void setPrecioPromediado(Double precioPromediado) {
		this.precioPromediado = precioPromediado;
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

	public Double getMontoTotalLinea() {
		return this.montoTotalLinea;
	}

	public void setMontoTotalLinea(Double montoTotalLinea) {
		this.montoTotalLinea = montoTotalLinea;
	}

	public void addCInventarioMovimientoItemsImpuesto(CInventarioMovimientoItemsImpuesto item) {
		this.impuestosItem.add(item);
	}
}
