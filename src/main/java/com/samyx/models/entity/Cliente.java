package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.samyx.models.entity.CBarrio;
import com.samyx.models.entity.CCanton;
import com.samyx.models.entity.CDistrito;
import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.CTipoDocumentoExoneracionOAutorizacion;
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
import javax.persistence.UniqueConstraint;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "c_clientes", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "emisor_id", "identificacion", "tipo_catalogo" }) })
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	@JsonBackReference
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emisor_id")
	@JsonBackReference
	private Emisor emisor;

	@Column(name = "codigo_actividad", length = 6)
	private String codigoActividad;

	@Column(name = "tipo_catalogo", length = 1)
	private String tipoCatalogo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_de_identificacion_id")
	private CTipoDeIdentificacion tipoDeIdentificacion;

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

	@Column(name = "otras_senas", length = 160)
	private String otrasSenas;

	@Column(length = 12)
	private String identificacion;

	@Column(name = "nombre_completo", length = 80)
	private String nombreCompleto;

	@Column(name = "codigo_pais", length = 3)
	private Integer codigoPais;

	@Column(length = 20)
	private String telefono1;

	@Column(length = 20)
	private String telefono2;

	@Column(length = 20)
	private String fax;

	@Column(length = 25)
	private String apartado;

	@Column(length = 100)
	private String web;

	@Column(length = 60)
	private String correo1;

	@Column(length = 60)
	private String correo2;

	@Column(length = 1)
	private String estadoCredito;

	@Column(name = "dias_credito", precision = 18, scale = 5)
	private Double diasCredito = Double.valueOf(0.0D);

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_documento_exoneracion_autorizacion_id")
	private CTipoDocumentoExoneracionOAutorizacion tipoDocumentoExoneracionOAutorizacion;

	@Column(length = 25)
	private String ley;

	@Column(name = "numero_documento", length = 17)
	private String numeroDocumento;

	@Column(name = "nombre_institucion", length = 100)
	private String nombreInstitucion;

	@Column(name = "porcentaje_exoneracion", length = 4, columnDefinition = "Double default 0.00")
	private Double porcentajeExoneracion;

	@Column(name = "fecha_inicio_exoneracion")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaInicioExoneracion;

	@Column(name = "fecha_fin_exoneracion")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaFinExoneracion;

	public Long getTipoExoneracion() {
		if (this.tipoDocumentoExoneracionOAutorizacion != null)
			return this.tipoDocumentoExoneracionOAutorizacion.getId();
		return Long.valueOf(Long.parseLong("0"));
	}

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

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public CTipoDeIdentificacion getTipoDeIdentificacion() {
		return this.tipoDeIdentificacion;
	}

	public void setTipoDeIdentificacion(CTipoDeIdentificacion tipoDeIdentificacion) {
		this.tipoDeIdentificacion = tipoDeIdentificacion;
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

	public String getIdentificacion() {
		return this.identificacion;
	}

	public String getOtrasSenas() {
		return this.otrasSenas;
	}

	public void setOtrasSenas(String otrasSenas) {
		this.otrasSenas = otrasSenas;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombreCompleto() {
		return this.nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public Integer getCodigoPais() {
		return this.codigoPais;
	}

	public void setCodigoPais(Integer codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getTelefono1() {
		return this.telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCorreo1() {
		return this.correo1;
	}

	public void setCorreo1(String correo1) {
		this.correo1 = correo1;
	}

	public String getCorreo2() {
		return this.correo2;
	}

	public void setCorreo2(String correo2) {
		this.correo2 = correo2;
	}

	public String getCodigoActividad() {
		return this.codigoActividad;
	}

	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	public String getTipoCatalogo() {
		return this.tipoCatalogo;
	}

	public void setTipoCatalogo(String tipoCatalogo) {
		this.tipoCatalogo = tipoCatalogo;
	}

	public String getApartado() {
		return this.apartado;
	}

	public void setApartado(String apartado) {
		this.apartado = apartado;
	}

	public String getWeb() {
		return this.web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getEstadoCredito() {
		return this.estadoCredito;
	}

	public void setEstadoCredito(String estadoCredito) {
		this.estadoCredito = estadoCredito;
	}

	public Double getDiasCredito() {
		return this.diasCredito;
	}

	public void setDiasCredito(Double diasCredito) {
		this.diasCredito = diasCredito;
	}

	public CTipoDocumentoExoneracionOAutorizacion getTipoDocumentoExoneracionOAutorizacion() {
		return this.tipoDocumentoExoneracionOAutorizacion;
	}

	public void setTipoDocumentoExoneracionOAutorizacion(
			CTipoDocumentoExoneracionOAutorizacion tipoDocumentoExoneracionOAutorizacion) {
		this.tipoDocumentoExoneracionOAutorizacion = tipoDocumentoExoneracionOAutorizacion;
	}

	public String getLey() {
		return this.ley;
	}

	public void setLey(String ley) {
		this.ley = ley;
	}

	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getNombreInstitucion() {
		return this.nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public Double getPorcentajeExoneracion() {
		return this.porcentajeExoneracion;
	}

	public void setPorcentajeExoneracion(Double porcentajeExoneracion) {
		this.porcentajeExoneracion = porcentajeExoneracion;
	}

	public Date getFechaInicioExoneracion() {
		return this.fechaInicioExoneracion;
	}

	public void setFechaInicioExoneracion(Date fechaInicioExoneracion) {
		this.fechaInicioExoneracion = fechaInicioExoneracion;
	}

	public Date getFechaFinExoneracion() {
		return this.fechaFinExoneracion;
	}

	public void setFechaFinExoneracion(Date fechaFinExoneracion) {
		this.fechaFinExoneracion = fechaFinExoneracion;
	}
}
