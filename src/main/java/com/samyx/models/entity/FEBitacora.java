package com.samyx.models.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.samyx.models.entity.FEFactura;

@Entity
@Table(name = "fe_bitacora")
public class FEBitacora {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private FEFactura factura;

	@Column(length = 50)
	private String clave;

	@Column(name = "tipo_documento", length = 5)
	private String tipoDocumento;

	@Column(length = 3)
	private int responseCode;

	@Column(name = "name_xml_enviado")
	private String nameXmlEnviado;

	@Column(name = "name_xml_respuesta")
	private String nameXmlRespuesta;

	@Column(name = "fecha_emision", length = 30)
	private String fechaEmision;

	@Column(name = "estado_hacienda", length = 15)
	private String estadoHacienda;

	@Column(name = "fecha_aceptacion", length = 30)
	private String fechaAceptacion;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FEFactura getFactura() {
		return this.factura;
	}

	public void setFactura(FEFactura factura) {
		this.factura = factura;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getEstadoHacienda() {
		return this.estadoHacienda;
	}

	public void setEstadoHacienda(String estadoHacienda) {
		this.estadoHacienda = estadoHacienda;
	}

	public String getNameXmlEnviado() {
		return this.nameXmlEnviado;
	}

	public void setNameXmlEnviado(String nameXmlEnviado) {
		this.nameXmlEnviado = nameXmlEnviado;
	}

	public String getNameXmlRespuesta() {
		return this.nameXmlRespuesta;
	}

	public void setNameXmlRespuesta(String nameXmlRespuesta) {
		this.nameXmlRespuesta = nameXmlRespuesta;
	}

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getFechaAceptacion() {
		return this.fechaAceptacion;
	}

	public void setFechaAceptacion(String fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}
}
