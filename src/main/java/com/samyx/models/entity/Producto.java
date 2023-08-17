package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.samyx.models.entity.CProductoImpuesto;
import com.samyx.models.entity.CTipoProducto;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.ProductoFamilia;
import com.samyx.models.entity.UnidadDeMedida;
import com.samyx.models.entity.Usuario;

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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "productos", uniqueConstraints = { @UniqueConstraint(columnNames = { "codigo", "emisor_id" }) })
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20)
	private String codigo;

	@Column(length = 30)
	private String codigoBarras;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_producto_id")
	private CTipoProducto tipoProducto;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "cabys_id")
	private EmisorCabys emisorCabys;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "familia_producto_id")
	private ProductoFamilia productoFamilia;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "producto_id")
	@JsonManagedReference
	private List<CProductoImpuesto> productoImpuesto;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "proveedor_id")
	@JsonBackReference
	private Cliente proveedor;

	@Column(length = 200)
	private String nombreProducto;

	@Column(name = "aplica_devolucion_iva", length = 1)
	private String aplicaDevolucionIva;

	@Column(name = "tipo_venta", length = 1)
	private String tipoVenta;

	@Column(name = "fracciones_por_unidad", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double fraccionesPorUnidad;

	@Column(name = "precio_compra", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double precioCompra;

	@Column(precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double utilidad;

	@Column(name = "utilidad_fraccion", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double utilidadFraccion;

	@Column(name = "precio_promediado", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double precioPromediado;

	@Column(length = 20, precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double precio;

	@Column(name = "precio_fraccion", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double precioFraccion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moneda_id")
	private Moneda moneda;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unidad_de_medida_id")
	private UnidadDeMedida unidadDeMedida;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	@JsonBackReference
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emisor_id")
	@JsonBackReference
	private Emisor emisor;

	@Column(name = "producto_exento", length = 1)
	private String productoExento;

	@Column(name = "afecta_inventario", length = 1)
	private String afectaInventario;

	public void addImpuestoProducto(CProductoImpuesto item) {
		this.productoImpuesto.add(item);
	}

	@Column(precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double entradas = Double.valueOf(0.0D);

	@Column(precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double salidas = Double.valueOf(0.0D);

	@Column(name = "existencia_minima", precision = 18, scale = 5, columnDefinition = "Double default 0.00")
	private Double existenciaMinima = Double.valueOf(0.0D);

	@Column(name = "estado_producto", length = 1)
	private String estadoProducto;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigoBarras() {
		return this.codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public CTipoProducto getTipoProducto() {
		return this.tipoProducto;
	}

	public void setTipoProducto(CTipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public ProductoFamilia getProductoFamilia() {
		return this.productoFamilia;
	}

	public void setProductoFamilia(ProductoFamilia productoFamilia) {
		this.productoFamilia = productoFamilia;
	}

	
	public EmisorCabys getEmisorCabys() {
		return this.emisorCabys;
	  }
	  
	  public void setEmisorCabys(EmisorCabys emisorCabys) {
		this.emisorCabys = emisorCabys;
	  }

	public List<CProductoImpuesto> getProductoImpuesto() {
		return this.productoImpuesto;
	}

	public void setProductoImpuesto(List<CProductoImpuesto> productoImpuesto) {
		this.productoImpuesto = productoImpuesto;
	}

	public Cliente getProveedor() {
		return this.proveedor;
	}

	public void setProveedor(Cliente proveedor) {
		this.proveedor = proveedor;
	}

	public String getNombreProducto() {
		return this.nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public Double getPrecioCompra() {
		return this.precioCompra;
	}

	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public Double getUtilidad() {
		return this.utilidad;
	}

	public void setUtilidad(Double utilidad) {
		this.utilidad = utilidad;
	}

	public Double getPrecioPromediado() {
		return this.precioPromediado;
	}

	public void setPrecioPromediado(Double precioPromediado) {
		this.precioPromediado = precioPromediado;
	}

	public Double getPrecio() {
		return this.precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public UnidadDeMedida getUnidadDeMedida() {
		return this.unidadDeMedida;
	}

	public void setUnidadDeMedida(UnidadDeMedida unidadDeMedida) {
		this.unidadDeMedida = unidadDeMedida;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public String getProductoExento() {
		return this.productoExento;
	}

	public void setProductoExento(String productoExento) {
		this.productoExento = productoExento;
	}

	public String getAfectaInventario() {
		return this.afectaInventario;
	}

	public void setAfectaInventario(String afectaInventario) {
		this.afectaInventario = afectaInventario;
	}

	public Double getEntradas() {
		return this.entradas;
	}

	public void setEntradas(Double entradas) {
		this.entradas = entradas;
	}

	public Double getSalidas() {
		return this.salidas;
	}

	public void setSalidas(Double salidas) {
		this.salidas = salidas;
	}

	public Double getExistenciaMinima() {
		return this.existenciaMinima;
	}

	public void setExistenciaMinima(Double existenciaMinima) {
		this.existenciaMinima = existenciaMinima;
	}

	public Double getImpuestoTotal() {
		Double res = Double.valueOf(0.0D);
		for (CProductoImpuesto r : this.productoImpuesto)
			res = Double.valueOf(res.doubleValue() + r.getPorcentaje().doubleValue());
		res = Double.valueOf(this.precio.doubleValue() / 100.0D * res.doubleValue());
		return res;
	}

	public Double getPrecioFinal() {
		return Double.valueOf(getPrecio().doubleValue() + getImpuestoTotal().doubleValue());
	}

	public Double getInventarioActual() {
		return Double.valueOf(this.entradas.doubleValue() - this.salidas.doubleValue());
	}

	public String getEstadoProducto() {
		return this.estadoProducto;
	}

	public void setEstadoProducto(String estadoProducto) {
		this.estadoProducto = estadoProducto;
	}

	public String getTipoVenta() {
		return this.tipoVenta;
	}

	public void setTipoVenta(String tipoVenta) {
		this.tipoVenta = tipoVenta;
	}

	public Double getFraccionesPorUnidad() {
		return this.fraccionesPorUnidad;
	}

	public void setFraccionesPorUnidad(Double fraccionesPorUnidad) {
		this.fraccionesPorUnidad = fraccionesPorUnidad;
	}

	public Double getUtilidadFraccion() {
		return this.utilidadFraccion;
	}

	public void setUtilidadFraccion(Double utilidadFraccion) {
		this.utilidadFraccion = utilidadFraccion;
	}

	public Double getPrecioFraccion() {
		return this.precioFraccion;
	}

	public void setPrecioFraccion(Double precioFraccion) {
		this.precioFraccion = precioFraccion;
	}

	public String getAplicaDevolucionIva() {
		return this.aplicaDevolucionIva;
	}

	public void setAplicaDevolucionIva(String aplicaDevolucionIva) {
		this.aplicaDevolucionIva = aplicaDevolucionIva;
	}
}
