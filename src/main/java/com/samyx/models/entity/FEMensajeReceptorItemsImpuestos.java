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

import com.samyx.models.entity.CCodigosTarifasIva;
import com.samyx.models.entity.CImpuesto;
import com.samyx.models.entity.FEMensajeReceptorItemsImpuestosExoneraciones;

@Entity
@Table(name = "fe_mensaje_receptor_items_impuestos")
public class FEMensajeReceptorItemsImpuestos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "impuestos_item_factura_id")
	private List<FEMensajeReceptorItemsImpuestosExoneraciones> exoneracionImpuestoItemFactura = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "impuesto_id")
	private CImpuesto impuesto;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "codigo_tarifa_iva_id")
	private CCodigosTarifasIva codigoTarifaIva;

	@Column(precision = 20, scale = 3)
	private Double tarifa;

	@Column(precision = 20, scale = 3)
	private Double monto;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CImpuesto getImpuesto() {
		return this.impuesto;
	}

	public void setImpuesto(CImpuesto impuesto) {
		this.impuesto = impuesto;
	}

	public CCodigosTarifasIva getCodigoTarifaIva() {
		return this.codigoTarifaIva;
	}

	public void setCodigoTarifaIva(CCodigosTarifasIva codigoTarifaIva) {
		this.codigoTarifaIva = codigoTarifaIva;
	}

	public Double getTarifa() {
		return this.tarifa;
	}

	public void setTarifa(Double tarifa) {
		this.tarifa = tarifa;
	}

	public Double getMonto() {
		return this.monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public void addItemFacturaImpuestosExoneracion(FEMensajeReceptorItemsImpuestosExoneraciones item) {
		this.exoneracionImpuestoItemFactura.add(item);
	}

	public List<FEMensajeReceptorItemsImpuestosExoneraciones> getExoneracionImpuestoItemFactura() {
		return this.exoneracionImpuestoItemFactura;
	}

	public void setExoneracionImpuestoItemFactura(
			List<FEMensajeReceptorItemsImpuestosExoneraciones> exoneracionImpuestoItemFactura) {
		this.exoneracionImpuestoItemFactura = exoneracionImpuestoItemFactura;
	}
}
