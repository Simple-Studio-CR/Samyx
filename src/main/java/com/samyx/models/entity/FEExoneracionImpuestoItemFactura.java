package com.samyx.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_facturas_items_impuestos_exoneraciones")
public class FEExoneracionImpuestoItemFactura {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "tipo_documento", length = 2)
	private String tipoDocumento;

	@Column(name = "numero_documento", length = 17)
	private String numeroDocumento;

	@Column(name = "nombre_institucion", length = 100)
	private String nombreInstitucion;

	@Column(name = "fecha_emision", length = 30)
	private String fechaEmision;

	@Column(name = "monto_impuesto", precision = 18, scale = 5)
	private Double montoImpuesto;

	@Column(name = "porcentaje_Compra", length = 3)
	private int porcentajeCompra;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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

	public String getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Double getMontoImpuesto() {
		return this.montoImpuesto;
	}

	public void setMontoImpuesto(Double montoImpuesto) {
		this.montoImpuesto = montoImpuesto;
	}

	public int getPorcentajeCompra() {
		return this.porcentajeCompra;
	}

	public void setPorcentajeCompra(int porcentajeCompra) {
		this.porcentajeCompra = porcentajeCompra;
	}
}