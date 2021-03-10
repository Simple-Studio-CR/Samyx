package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.FEFacturaRegistroPagosCXC;
import com.samyx.models.entity.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "fe_factura_cxc", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "emisor_id", "cliente_id", "numero_factura" }) })
public class FEFacturasCXC {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "emisor_id")
	@JsonIgnore
	private Emisor emisor;

	@Column(length = 50)
	private String clave;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "cliente_id")
	@JsonIgnore
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Usuario usuario;

	@Column(name = "fecha_emision_fe")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaEmisionFe;

	@Column(name = "fecha_vencimiento_fe")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaVencimientoFe;

	@Column(name = "plazoCredito", length = 5)
	private String plazoCredito;

	@Column(name = "numero_factura")
	private Long numeroFactura;

	private String moneda;

	@Column(name = "total_deuda", precision = 18, scale = 5)
	private double totalDeuda;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@JoinColumn(name = "factura_cxc_id")
	@JsonIgnore
	private List<FEFacturaRegistroPagosCXC> itemsRegistroPagosCXC;

	@Column(name = "estado_pago", length = 1)
	private String estadoPago;

	@Column(name = "en_tramite", length = 1)
	private String enTramite;

	@Column(name = "fecha_cancelacion")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	private Date fechaCancelacion;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Emisor getEmisor() {
		return this.emisor;
	}

	public void setEmisor(Emisor emisor) {
		this.emisor = emisor;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFechaEmisionFe() {
		return this.fechaEmisionFe;
	}

	public void setFechaEmisionFe(Date fechaEmisionFe) {
		this.fechaEmisionFe = fechaEmisionFe;
	}

	public Date getFechaVencimientoFe() {
		return this.fechaVencimientoFe;
	}

	public void setFechaVencimientoFe(Date fechaVencimientoFe) {
		this.fechaVencimientoFe = fechaVencimientoFe;
	}

	public String getPlazoCredito() {
		return this.plazoCredito;
	}

	public void setPlazoCredito(String plazoCredito) {
		this.plazoCredito = plazoCredito;
	}

	public Long getNumeroFactura() {
		return this.numeroFactura;
	}

	public void setNumeroFactura(Long numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public double getTotalDeuda() {
		return this.totalDeuda;
	}

	public void setTotalDeuda(double totalDeuda) {
		this.totalDeuda = totalDeuda;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public List<FEFacturaRegistroPagosCXC> getItemsRegistroPagosCXC() {
		return this.itemsRegistroPagosCXC;
	}

	public void setItemsRegistroPagosCXC(List<FEFacturaRegistroPagosCXC> itemsRegistroPagosCXC) {
		this.itemsRegistroPagosCXC = itemsRegistroPagosCXC;
	}

	public String getEstadoPago() {
		return this.estadoPago;
	}

	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}

	public Double getDeuda() {
		return Double.valueOf(this.totalDeuda += this.totalDeuda);
	}

	public Date getFechaCancelacion() {
		return this.fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public Double getDeudaActualCXC() {
		Double deuda = Double.valueOf(0.0D), totalDeuda = Double.valueOf(0.0D);
		for (FEFacturaRegistroPagosCXC r : this.itemsRegistroPagosCXC)
			deuda = Double.valueOf(deuda.doubleValue() + r.getMontoAbono());
		totalDeuda = Double.valueOf(this.totalDeuda - deuda.doubleValue());
		return totalDeuda;
	}

	public String getFechaVencimientoFactura() {
		String response = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(this.fechaEmisionFe);
		if (this.plazoCredito != null) {
			c.add(5, Integer.parseInt(this.plazoCredito));
		} else {
			c.add(5, 0);
		}
		response = sdf.format(c.getTime());
		return response;
	}

	public String getStatusFactura() throws ParseException {
		String resp = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(this.fechaEmisionFe);
		Double plazoCredito = Double.valueOf(Double.parseDouble(this.plazoCredito));
		if (this.plazoCredito != null) {
			c.add(5, plazoCredito.intValue());
		} else {
			c.add(5, 0);
		}
		Date t = sdf.parse(sdf.format(c.getTime()) + "");
		int dias = (int) ((t.getTime() - (new Date()).getTime()) / 86400000L);
		String sDias = dias + "";
		if (dias >= 0) {
			resp = "<span class=\"badge badge-info\"><i class=\"fas fa-check\"></i> Al día</span>";
		} else {
			resp = "<span class=\"badge badge-danger\"><i class=\"fas fa-exclamation-triangle\"></i> Días de atraso: <strong>"
					+ sDias.substring(1, sDias.toString().length()) + "</strong></span>";
		}
		return resp;
	}

	public String getConsecutivo() {
		String resp = "N/A";
		try {
			if (this.clave != null && this.clave.length() > 40)
				resp = this.clave.substring(21, 41);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	public String getEnTramite() {
		return this.enTramite;
	}

	public void setEnTramite(String enTramite) {
		this.enTramite = enTramite;
	}

	public String getEstadoTramite() {
		String resp = "No";
		if (getEnTramite() != null && getEnTramite().equalsIgnoreCase("S"))
			resp = "Si";
		return resp;
	}

	public String getEstadoCuenta() {
		String resp = "";
		switch (this.estadoPago.toUpperCase()) {
		case "C":
			resp = "Cancelada";
			break;
		case "A":
			resp = "Pendiente";
			break;
		case "N":
			resp = "Anulado";
			break;
		}
		return resp;
	}
}
