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

import com.samyx.models.entity.CBarrio;
import com.samyx.models.entity.CCanton;
import com.samyx.models.entity.CDistrito;
import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.Usuario;

@Entity
@Table(name = "emisores", uniqueConstraints = { @UniqueConstraint(columnNames = { "identificacion" }) })
public class Emisor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_de_identificacion_id")
	private CTipoDeIdentificacion tipoDeIdentificacion;

	private String identificacion;

	@Column(name = "token_access")
	private String tokenAccess;

	@Column(length = 4)
	private String ambiente;

	@Column(name = "logo_empresa")
	private String logoEmpresa;

	@Column(name = "codigo_actividad", length = 6)
	public String codigoActividad;

	@Column(name = "nombre_razon_social", length = 100)
	private String nombreRazonSocial;

	@Column(name = "nombre_comercial", length = 80)
	private String nombreComercial;

	private String email;

	@Column(name = "codigo_pais", length = 3)
	private String codigoPais;

	@Column(length = 10)
	private String telefono;

	@Column(length = 10)
	private String fax;

	@Column(name = "detalle_en_factura1")
	private String detalleEnFactura1;

	@Column(name = "detalle_en_factura2")
	private String detalleEnFactura2;

	@Column(name = "nota_validez_proforma")
	private String notaValidezProforma;

	@Column(name = "nota_factura")
	private String nataFactura;

	private String observacion;

	@Column(name = "status_empresa")
	private Boolean statusEmpresa;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "provincia_id")
	private CProvincia provincia;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "canton_id")
	private CCanton canton;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "distrito_id")
	private CDistrito distrito;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "barrio_id")
	private CBarrio barrio;

	@Column(name = "otras_senas")
	private String otrasSenas;

	@Column(name = "certificado", length = 100)
	private String certificado;

	@Column(name = "fecha_emision_cert")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaEmisionCert;

	@Column(name = "fecha_caducidad_cert")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaCaducidadCert;

	@Column(name = "user_api", length = 100)
	private String userApi;

	@Column(name = "pw_api", length = 100)
	private String pwApi;

	@Column(name = "ping_api", length = 4)
	private String pingApi;

	@Column(name = "email_notificacion", length = 100)
	private String emailNotificacion;

	@Column(length = 1)
	private String impresion;

	@Column(name = "control_cajas", length = 1)
	private String controlCajas;

	@Column(name = "multimoneda_factura", length = 1)
	private String multiMonedaFactura;

	@Column(length = 1)
	private String decimales;

	@Column(length = 1)
	private String tipoCliente;

	@Column(length = 1)
	private String devolucionIva;

	@Column(length = 1)
	private String sistemaFarmacias;

	@NotNull
	@Column(name = "fecha_creacion")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaCreacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "emisor_id")
	private List<EmisorActividades> itemsActividades = new ArrayList<>();

	public void addActividadEmisor(EmisorActividades item) {
		this.itemsActividades.add(item);
	}

	public List<EmisorActividades> getItemsActividades() {
		return this.itemsActividades;
	}

	public void setItemsActividades(List<EmisorActividades> itemsActividades) {
		this.itemsActividades = itemsActividades;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CTipoDeIdentificacion getTipoDeIdentificacion() {
		return this.tipoDeIdentificacion;
	}

	public void setTipoDeIdentificacion(CTipoDeIdentificacion tipoDeIdentificacion) {
		this.tipoDeIdentificacion = tipoDeIdentificacion;
	}

	public String getIdentificacion() {
		return this.identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getTokenAccess() {
		return this.tokenAccess;
	}

	public void setTokenAccess(String tokenAccess) {
		this.tokenAccess = tokenAccess;
	}

	public String getAmbiente() {
		return this.ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getLogoEmpresa() {
		return this.logoEmpresa;
	}

	public void setLogoEmpresa(String logoEmpresa) {
		this.logoEmpresa = logoEmpresa;
	}

	public String getNombreRazonSocial() {
		return this.nombreRazonSocial;
	}

	public void setNombreRazonSocial(String nombreRazonSocial) {
		this.nombreRazonSocial = nombreRazonSocial;
	}

	public String getNombreComercial() {
		return this.nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodigoPais() {
		return this.codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getDetalleEnFactura1() {
		return this.detalleEnFactura1;
	}

	public void setDetalleEnFactura1(String detalleEnFactura1) {
		this.detalleEnFactura1 = detalleEnFactura1;
	}

	public String getDetalleEnFactura2() {
		return this.detalleEnFactura2;
	}

	public void setDetalleEnFactura2(String detalleEnFactura2) {
		this.detalleEnFactura2 = detalleEnFactura2;
	}

	public String getNotaValidezProforma() {
		return this.notaValidezProforma;
	}

	public void setNotaValidezProforma(String notaValidezProforma) {
		this.notaValidezProforma = notaValidezProforma;
	}

	public String getNataFactura() {
		return this.nataFactura;
	}

	public void setNataFactura(String nataFactura) {
		this.nataFactura = nataFactura;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Boolean getStatusEmpresa() {
		return this.statusEmpresa;
	}

	public void setStatusEmpresa(Boolean statusEmpresa) {
		this.statusEmpresa = statusEmpresa;
	}

	public CProvincia getProvincia() {
		return this.provincia;
	}

	public void setProvincia(CProvincia provincia) {
		this.provincia = provincia;
	}

	public CCanton getCanton() {
		return this.canton;
	}

	public void setCanton(CCanton canton) {
		this.canton = canton;
	}

	public CDistrito getDistrito() {
		return this.distrito;
	}

	public void setDistrito(CDistrito distrito) {
		this.distrito = distrito;
	}

	public CBarrio getBarrio() {
		return this.barrio;
	}

	public void setBarrio(CBarrio barrio) {
		this.barrio = barrio;
	}

	public String getOtrasSenas() {
		return this.otrasSenas;
	}

	public void setOtrasSenas(String otrasSenas) {
		this.otrasSenas = otrasSenas;
	}

	public String getCertificado() {
		return this.certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public Date getFechaEmisionCert() {
		return this.fechaEmisionCert;
	}

	public void setFechaEmisionCert(Date fechaEmisionCert) {
		this.fechaEmisionCert = fechaEmisionCert;
	}

	public Date getFechaCaducidadCert() {
		return this.fechaCaducidadCert;
	}

	public void setFechaCaducidadCert(Date fechaCaducidadCert) {
		this.fechaCaducidadCert = fechaCaducidadCert;
	}

	public String getUserApi() {
		return this.userApi;
	}

	public void setUserApi(String userApi) {
		this.userApi = userApi;
	}

	public String getPwApi() {
		return this.pwApi;
	}

	public void setPwApi(String pwApi) {
		this.pwApi = pwApi;
	}

	public String getPingApi() {
		return this.pingApi;
	}

	public void setPingApi(String pingApi) {
		this.pingApi = pingApi;
	}

	public String getEmailNotificacion() {
		return this.emailNotificacion;
	}

	public void setEmailNotificacion(String emailNotificacion) {
		this.emailNotificacion = emailNotificacion;
	}

	public String getImpresion() {
		return this.impresion;
	}

	public void setImpresion(String impresion) {
		this.impresion = impresion;
	}

	public String getControlCajas() {
		return this.controlCajas;
	}

	public void setControlCajas(String controlCajas) {
		this.controlCajas = controlCajas;
	}

	public String getMultiMonedaFactura() {
		return this.multiMonedaFactura;
	}

	public void setMultiMonedaFactura(String multiMonedaFactura) {
		this.multiMonedaFactura = multiMonedaFactura;
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

	public String getCodigoActividad() {
		return this.codigoActividad;
	}

	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	public String getDecimales() {
		return this.decimales;
	}

	public void setDecimales(String decimales) {
		this.decimales = decimales;
	}

	public String getTipoCliente() {
		return this.tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getDevolucionIva() {
		return this.devolucionIva;
	}

	public void setDevolucionIva(String devolucionIva) {
		this.devolucionIva = devolucionIva;
	}

	public String getSistemaFarmacias() {
		return this.sistemaFarmacias;
	}

	public void setSistemaFarmacias(String sistemaFarmacias) {
		this.sistemaFarmacias = sistemaFarmacias;
	}
}
