package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.samyx.models.entity.CTerminal;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Usuario;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "c_terminales_usuarios_acceso", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "terminal_id", "usuario_id" }) })
public class CTerminalUsuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "terminal_id")
	private CTerminal terminal;

	@NotNull
	@Column(name = "fecha_creacion")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaCreacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(length = 1)
	private String status;

	@Column(name = "actividad_economica", length = 6)
	private String actividadEconomica;

	@Column(name = "tipo_de_documento", length = 5)
	private String tipoDeDocumento;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "moneda_id")
	@JsonBackReference
	private Moneda moneda;

	@Column(name = "incluir_omitir_receptor", length = 5)
	private String incluirOmitirReceptor;

	@Column(name = "tipo_busqueda_producto", length = 1)
	private String tipoBusquedaProducto;

	@Column(name = "aplica_descuento_facturacion")
	private Boolean aplicaDescuentoFacturacion;

	@Column(name = "modifica_precio_facturacion")
	private Boolean modificaPrecioFacturacion;

	@Column(name = "descuento_facturacion", precision = 18, scale = 5)
	private Double descuentoFacturacion = Double.valueOf(0.0D);

	@PrePersist
	public void prePersist() {
		this.fechaCreacion = new Date();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CTerminal getTerminal() {
		return this.terminal;
	}

	public void setTerminal(CTerminal terminal) {
		this.terminal = terminal;
	}

	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActividadEconomica() {
		return this.actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public String getTipoDeDocumento() {
		return this.tipoDeDocumento;
	}

	public void setTipoDeDocumento(String tipoDeDocumento) {
		this.tipoDeDocumento = tipoDeDocumento;
	}

	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public String getIncluirOmitirReceptor() {
		return this.incluirOmitirReceptor;
	}

	public void setIncluirOmitirReceptor(String incluirOmitirReceptor) {
		this.incluirOmitirReceptor = incluirOmitirReceptor;
	}

	public String getTipoBusquedaProducto() {
		return this.tipoBusquedaProducto;
	}

	public void setTipoBusquedaProducto(String tipoBusquedaProducto) {
		this.tipoBusquedaProducto = tipoBusquedaProducto;
	}

	public Boolean getAplicaDescuentoFacturacion() {
		return this.aplicaDescuentoFacturacion;
	}

	public void setAplicaDescuentoFacturacion(Boolean aplicaDescuentoFacturacion) {
		this.aplicaDescuentoFacturacion = aplicaDescuentoFacturacion;
	}

	public Boolean getModificaPrecioFacturacion() {
		return this.modificaPrecioFacturacion;
	}

	public void setModificaPrecioFacturacion(Boolean modificaPrecioFacturacion) {
		this.modificaPrecioFacturacion = modificaPrecioFacturacion;
	}

	public Double getDescuentoFacturacion() {
		return this.descuentoFacturacion;
	}

	public void setDescuentoFacturacion(Double descuentoFacturacion) {
		this.descuentoFacturacion = descuentoFacturacion;
	}

	public String getLabelModificaPrecioFacturacion() {
		if (this.modificaPrecioFacturacion.booleanValue())
			return "Si";
		return "No";
	}

	public String getLabelAplicaDescuentoFacturacion() {
		if (this.aplicaDescuentoFacturacion.booleanValue())
			return "Si";
		return "No";
	}
}
