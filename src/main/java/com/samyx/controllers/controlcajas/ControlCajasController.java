package com.samyx.controllers.controlcajas;

import com.samyx.models.entity.ControlCaja;
import com.samyx.models.entity.ControlCajaMovimientos;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.IControlCajaMovimientosService;
import com.samyx.service.interfaces.IControlCajaService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IFEFacturaService;
import com.samyx.service.interfaces.IFEFacturasCXCServices;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping({ "/control-cajas" })
@SessionAttributes({ "controlCaja" })
public class ControlCajasController {
	@Autowired
	private IControlCajaService _cajaService;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private IEmisorService _emisorService;

	@Autowired
	private IFEFacturaService _facturaService;

	@Autowired
	private IFEFacturasCXCServices _cxcService;

	@Autowired
	private IControlCajaMovimientosService _cajaMovimientoService;

	@Autowired
	private IMonedaService _monedaService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping({ "/" })
	public String home(Model model, HttpServletRequest request,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session, Authentication auth) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<ControlCaja> ListaCajas = null;
			Usuario usuario = this._usuarioService.findByUsername(auth.getName());
			if (request.isUserInRole("ROLE_USER_COBRADOR")) {
				ListaCajas = this._cajaService.findByAllByEmisorByUserId(emisorId, usuario.getId(), q.toUpperCase(),
						(Pageable) pageRequest);
			} else {
				ListaCajas = this._cajaService.findByAllByEmisor(emisorId, q.toUpperCase(), (Pageable) pageRequest);
			}
			PageRender<ControlCaja> pageRender = new PageRender("/control-cajas/", ListaCajas);
			model.addAttribute("ListaCajas", ListaCajas);
			model.addAttribute("page", pageRender);
			return "/catalogos/control-cajas/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/form" })
	public String form(HttpServletRequest request, Model model, HttpSession session, Authentication auth) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Usuario usuario = this._usuarioService.findByUsername(auth.getName());
			ControlCaja controlCaja = new ControlCaja();
			model.addAttribute("controlCaja", controlCaja);
			Integer cajasAbierta = this._cajaService.countByUsuarioAndEmpresa(emisorId, usuario.getId());
			if (cajasAbierta.intValue() > 0)
				return "redirect:/control-cajas/?cajaAbierta";
			Double totalEfectivoColones = Double.valueOf(0.0D);
			Double totalEfectivoDolares = Double.valueOf(0.0D);
			Double totalEfectivoEuros = Double.valueOf(0.0D);
			Double totalTarjetaColones = Double.valueOf(0.0D);
			Double totalTarjetaDolares = Double.valueOf(0.0D);
			Double totalTarjetaEuros = Double.valueOf(0.0D);
			Double totalChequeColones = Double.valueOf(0.0D);
			Double totalChequeDolares = Double.valueOf(0.0D);
			Double totalChequeEuros = Double.valueOf(0.0D);
			Double totalDepositosColones = Double.valueOf(0.0D);
			Double totalDepositosDolares = Double.valueOf(0.0D);
			Double totalDepositosEuros = Double.valueOf(0.0D);
			model.addAttribute("totalEfectivoColones", totalEfectivoColones);
			model.addAttribute("totalEfectivoDolares", totalEfectivoDolares);
			model.addAttribute("totalEfectivoEuros", totalEfectivoEuros);
			model.addAttribute("totalTarjetaColones", totalTarjetaColones);
			model.addAttribute("totalTarjetaDolares", totalTarjetaDolares);
			model.addAttribute("totalTarjetaEuros", totalTarjetaEuros);
			model.addAttribute("totalChequeColones", totalChequeColones);
			model.addAttribute("totalChequeDolares", totalChequeDolares);
			model.addAttribute("totalChequeEuros", totalChequeEuros);
			model.addAttribute("totalDepositosColones", totalDepositosColones);
			model.addAttribute("totalDepositosDolares", totalDepositosDolares);
			model.addAttribute("totalDepositosEuros", totalDepositosEuros);
			model.addAttribute("salidasEfectivoColones", Double.valueOf(0.0D));
			model.addAttribute("salidasEfectivoDolares", Double.valueOf(0.0D));
			model.addAttribute("salidasEfectivoEuros", Double.valueOf(0.0D));
			model.addAttribute("entradasEfectivoColones", Double.valueOf(0.0D));
			model.addAttribute("entradasEfectivoDolares", Double.valueOf(0.0D));
			model.addAttribute("entradasEfectivoEuros", Double.valueOf(0.0D));
			model.addAttribute("ventasColonesEfectivo", Double.valueOf(0.0D));
			model.addAttribute("ventasDolaresEfectivo", Double.valueOf(0.0D));
			model.addAttribute("ventasEurosEfectivo", Double.valueOf(0.0D));
			model.addAttribute("ventasColonesTarjeta", Double.valueOf(0.0D));
			model.addAttribute("ventasDolaresTarjeta", Double.valueOf(0.0D));
			model.addAttribute("ventasEurosTarjeta", Double.valueOf(0.0D));
			model.addAttribute("ventasColonesCheque", Double.valueOf(0.0D));
			model.addAttribute("ventasDolaresCheque", Double.valueOf(0.0D));
			model.addAttribute("ventasEurosCheque", Double.valueOf(0.0D));
			model.addAttribute("ventasColonesDeposito", Double.valueOf(0.0D));
			model.addAttribute("ventasDolaresDeposito", Double.valueOf(0.0D));
			model.addAttribute("ventasEurosDeposito", Double.valueOf(0.0D));
			model.addAttribute("ventasColonesCredito", Double.valueOf(0.0D));
			model.addAttribute("ventasDolaresCredito", Double.valueOf(0.0D));
			model.addAttribute("ventasEurosCredito", Double.valueOf(0.0D));
			model.addAttribute("cxcColonesEfectivo", Double.valueOf(0.0D));
			model.addAttribute("cxcDolaresEfectivo", Double.valueOf(0.0D));
			model.addAttribute("cxcEurosEfectivo", Double.valueOf(0.0D));
			model.addAttribute("cxcColonesTarjeta", Double.valueOf(0.0D));
			model.addAttribute("cxcDolaresTarjeta", Double.valueOf(0.0D));
			model.addAttribute("cxcEurosTarjeta", Double.valueOf(0.0D));
			model.addAttribute("cxcColonesCheque", Double.valueOf(0.0D));
			model.addAttribute("cxcDolaresCheque", Double.valueOf(0.0D));
			model.addAttribute("cxcEurosCheque", Double.valueOf(0.0D));
			model.addAttribute("cxcColonesDeposito", Double.valueOf(0.0D));
			model.addAttribute("cxcDolaresDeposito", Double.valueOf(0.0D));
			model.addAttribute("cxcEurosDeposito", Double.valueOf(0.0D));
			model.addAttribute("cxcColonesCheques", Double.valueOf(0.0D));
			model.addAttribute("cxcDolaresCheques", Double.valueOf(0.0D));
			model.addAttribute("cxcEurosCheques", Double.valueOf(0.0D));
			return "/catalogos/control-cajas/form";
		}
		return "redirect:/";
	}

	@GetMapping({ "/edit/{id}" })
	public String edit(HttpServletRequest request, Model model, @PathVariable("id") Long id, HttpSession session,
			Authentication auth) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			ControlCaja controlCaja = this._cajaService.findByIdAndEmisorId(id, emisorId);
			Double salidasEfectivoColones = Double.valueOf(0.0D);
			Double salidasEfectivoDolares = Double.valueOf(0.0D);
			Double salidasEfectivoEuros = Double.valueOf(0.0D);
			Double entradasEfectivoColones = Double.valueOf(0.0D);
			Double entradasEfectivoDolares = Double.valueOf(0.0D);
			Double entradasEfectivoEuros = Double.valueOf(0.0D);
			Double totalEfectivoColones = Double.valueOf(0.0D);
			Double totalEfectivoDolares = Double.valueOf(0.0D);
			Double totalEfectivoEuros = Double.valueOf(0.0D);
			Double totalTarjetaColones = Double.valueOf(0.0D);
			Double totalTarjetaDolares = Double.valueOf(0.0D);
			Double totalTarjetaEuros = Double.valueOf(0.0D);
			Double totalChequeColones = Double.valueOf(0.0D);
			Double totalChequeDolares = Double.valueOf(0.0D);
			Double totalChequeEuros = Double.valueOf(0.0D);
			Double totalDepositosColones = Double.valueOf(0.0D);
			Double totalDepositosDolares = Double.valueOf(0.0D);
			Double totalDepositosEuros = Double.valueOf(0.0D);
			Double totalDevolucionesColones = Double.valueOf(0.0D);
			Double totalDevolucionesDolares = Double.valueOf(0.0D);
			Double totalDevolucionesEuros = Double.valueOf(0.0D);
			salidasEfectivoColones = this._cajaMovimientoService.totalEntradasSalidasEfectivo(emisorId, "S", id, "CRC");
			salidasEfectivoColones = Double
					.valueOf((salidasEfectivoColones != null && salidasEfectivoColones.doubleValue() > 0.0D)
							? salidasEfectivoColones.doubleValue()
							: 0.0D);
			model.addAttribute("salidasEfectivoColones", salidasEfectivoColones);
			salidasEfectivoDolares = this._cajaMovimientoService.totalEntradasSalidasEfectivo(emisorId, "S", id, "USD");
			salidasEfectivoDolares = Double
					.valueOf((salidasEfectivoDolares != null && salidasEfectivoDolares.doubleValue() > 0.0D)
							? salidasEfectivoDolares.doubleValue()
							: 0.0D);
			model.addAttribute("salidasEfectivoDolares", salidasEfectivoDolares);
			salidasEfectivoEuros = this._cajaMovimientoService.totalEntradasSalidasEfectivo(emisorId, "S", id, "EUR");
			salidasEfectivoEuros = Double
					.valueOf((salidasEfectivoEuros != null && salidasEfectivoEuros.doubleValue() > 0.0D)
							? salidasEfectivoEuros.doubleValue()
							: 0.0D);
			model.addAttribute("salidasEfectivoEuros", salidasEfectivoEuros);
			entradasEfectivoColones = this._cajaMovimientoService.totalEntradasSalidasEfectivo(emisorId, "E", id,
					"CRC");
			entradasEfectivoColones = Double
					.valueOf((entradasEfectivoColones != null && entradasEfectivoColones.doubleValue() > 0.0D)
							? entradasEfectivoColones.doubleValue()
							: 0.0D);
			model.addAttribute("entradasEfectivoColones", entradasEfectivoColones);
			entradasEfectivoDolares = this._cajaMovimientoService.totalEntradasSalidasEfectivo(emisorId, "E", id,
					"USD");
			entradasEfectivoDolares = Double
					.valueOf((entradasEfectivoDolares != null && entradasEfectivoDolares.doubleValue() > 0.0D)
							? entradasEfectivoDolares.doubleValue()
							: 0.0D);
			model.addAttribute("entradasEfectivoDolares", entradasEfectivoDolares);
			entradasEfectivoEuros = this._cajaMovimientoService.totalEntradasSalidasEfectivo(emisorId, "E", id, "EUR");
			entradasEfectivoEuros = Double
					.valueOf((entradasEfectivoEuros != null && entradasEfectivoEuros.doubleValue() > 0.0D)
							? entradasEfectivoEuros.doubleValue()
							: 0.0D);
			model.addAttribute("entradasEfectivoEuros", entradasEfectivoEuros);
			totalDevolucionesColones = this._facturaService.devolucionesNc(emisorId, controlCaja.getUsuario().getId(),
					controlCaja.getFechaApertura(), "CRC");
			totalDevolucionesColones = Double
					.valueOf((totalDevolucionesColones != null && totalDevolucionesColones.doubleValue() > 0.0D)
							? totalDevolucionesColones.doubleValue()
							: 0.0D);
			model.addAttribute("totalDevolucionesColones", totalDevolucionesColones);
			totalDevolucionesDolares = this._facturaService.devolucionesNc(emisorId, controlCaja.getUsuario().getId(),
					controlCaja.getFechaApertura(), "USD");
			totalDevolucionesDolares = Double
					.valueOf((totalDevolucionesDolares != null && totalDevolucionesDolares.doubleValue() > 0.0D)
							? totalDevolucionesDolares.doubleValue()
							: 0.0D);
			model.addAttribute("totalDevolucionesDolares", totalDevolucionesDolares);
			totalDevolucionesEuros = this._facturaService.devolucionesNc(emisorId, controlCaja.getUsuario().getId(),
					controlCaja.getFechaApertura(), "EUR");
			totalDevolucionesEuros = Double
					.valueOf((totalDevolucionesEuros != null && totalDevolucionesEuros.doubleValue() > 0.0D)
							? totalDevolucionesEuros.doubleValue()
							: 0.0D);
			model.addAttribute("totalDevolucionesEuros", totalDevolucionesEuros);
			List<String> tiposDocsVentas = new ArrayList<>();
			tiposDocsVentas.add("FE");
			tiposDocsVentas.add("TE");
			tiposDocsVentas.add("FEE");
			Double ventasColonesEfectivo = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "1", controlCaja.getFechaApertura(), "CRC", tiposDocsVentas);
			ventasColonesEfectivo = Double
					.valueOf((ventasColonesEfectivo != null && ventasColonesEfectivo.doubleValue() > 0.0D)
							? ventasColonesEfectivo.doubleValue()
							: 0.0D);
			model.addAttribute("ventasColonesEfectivo", ventasColonesEfectivo);
			Double ventasDolaresEfectivo = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "1", controlCaja.getFechaApertura(), "USD", tiposDocsVentas);
			ventasDolaresEfectivo = Double
					.valueOf((ventasDolaresEfectivo != null && ventasDolaresEfectivo.doubleValue() > 0.0D)
							? ventasDolaresEfectivo.doubleValue()
							: 0.0D);
			model.addAttribute("ventasDolaresEfectivo", ventasDolaresEfectivo);
			Double ventasEurosEfectivo = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "1", controlCaja.getFechaApertura(), "EUR", tiposDocsVentas);
			ventasEurosEfectivo = Double
					.valueOf((ventasEurosEfectivo != null && ventasEurosEfectivo.doubleValue() > 0.0D)
							? ventasEurosEfectivo.doubleValue()
							: 0.0D);
			model.addAttribute("ventasEurosEfectivo", ventasEurosEfectivo);
			Double ventasColonesTarjeta = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "2", controlCaja.getFechaApertura(), "CRC", tiposDocsVentas);
			ventasColonesTarjeta = Double
					.valueOf((ventasColonesTarjeta != null && ventasColonesTarjeta.doubleValue() > 0.0D)
							? ventasColonesTarjeta.doubleValue()
							: 0.0D);
			model.addAttribute("ventasColonesTarjeta", ventasColonesTarjeta);
			Double ventasDolaresTarjeta = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "2", controlCaja.getFechaApertura(), "USD", tiposDocsVentas);
			ventasDolaresTarjeta = Double
					.valueOf((ventasDolaresTarjeta != null && ventasDolaresTarjeta.doubleValue() > 0.0D)
							? ventasDolaresTarjeta.doubleValue()
							: 0.0D);
			model.addAttribute("ventasDolaresTarjeta", ventasDolaresTarjeta);
			Double ventasEurosTarjeta = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "2", controlCaja.getFechaApertura(), "EUR", tiposDocsVentas);
			ventasEurosTarjeta = Double.valueOf((ventasEurosTarjeta != null && ventasEurosTarjeta.doubleValue() > 0.0D)
					? ventasEurosTarjeta.doubleValue()
					: 0.0D);
			model.addAttribute("ventasEurosTarjeta", ventasEurosTarjeta);
			Double ventasColonesCheque = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "3", controlCaja.getFechaApertura(), "CRC", tiposDocsVentas);
			ventasColonesCheque = Double
					.valueOf((ventasColonesCheque != null && ventasColonesCheque.doubleValue() > 0.0D)
							? ventasColonesCheque.doubleValue()
							: 0.0D);
			model.addAttribute("ventasColonesCheque", ventasColonesCheque);
			Double ventasDolaresCheque = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "3", controlCaja.getFechaApertura(), "USD", tiposDocsVentas);
			ventasDolaresCheque = Double
					.valueOf((ventasDolaresCheque != null && ventasDolaresCheque.doubleValue() > 0.0D)
							? ventasDolaresCheque.doubleValue()
							: 0.0D);
			model.addAttribute("ventasDolaresCheque", ventasDolaresCheque);
			Double ventasEurosCheque = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "3", controlCaja.getFechaApertura(), "EUR", tiposDocsVentas);
			ventasEurosCheque = Double.valueOf((ventasEurosCheque != null && ventasEurosCheque.doubleValue() > 0.0D)
					? ventasEurosCheque.doubleValue()
					: 0.0D);
			model.addAttribute("ventasEurosCheque", ventasEurosCheque);
			Double ventasColonesDeposito = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "4", controlCaja.getFechaApertura(), "CRC", tiposDocsVentas);
			ventasColonesDeposito = Double
					.valueOf((ventasColonesDeposito != null && ventasColonesDeposito.doubleValue() > 0.0D)
							? ventasColonesDeposito.doubleValue()
							: 0.0D);
			model.addAttribute("ventasColonesDeposito", ventasColonesDeposito);
			Double ventasDolaresDeposito = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "4", controlCaja.getFechaApertura(), "USD", tiposDocsVentas);
			ventasDolaresDeposito = Double
					.valueOf((ventasDolaresDeposito != null && ventasDolaresDeposito.doubleValue() > 0.0D)
							? ventasDolaresDeposito.doubleValue()
							: 0.0D);
			model.addAttribute("ventasDolaresDeposito", ventasDolaresDeposito);
			Double ventasEurosDeposito = this._facturaService.totalVentasByCondicionVentaAndMedioPago(emisorId,
					controlCaja.getUsuario().getId(), "1", "4", controlCaja.getFechaApertura(), "EUR", tiposDocsVentas);
			ventasEurosDeposito = Double
					.valueOf((ventasEurosDeposito != null && ventasEurosDeposito.doubleValue() > 0.0D)
							? ventasEurosDeposito.doubleValue()
							: 0.0D);
			model.addAttribute("ventasEurosDeposito", ventasEurosDeposito);
			Double ventasColonesCredito = this._facturaService.totalVentasByCondicionVentaCredito(emisorId,
					controlCaja.getUsuario().getId(), "2", controlCaja.getFechaApertura(), "CRC");
			ventasColonesCredito = Double
					.valueOf((ventasColonesCredito != null && ventasColonesCredito.doubleValue() > 0.0D)
							? ventasColonesCredito.doubleValue()
							: 0.0D);
			model.addAttribute("ventasColonesCredito", ventasColonesCredito);
			Double ventasDolaresCredito = this._facturaService.totalVentasByCondicionVentaCredito(emisorId,
					controlCaja.getUsuario().getId(), "2", controlCaja.getFechaApertura(), "USD");
			ventasDolaresCredito = Double
					.valueOf((ventasDolaresCredito != null && ventasDolaresCredito.doubleValue() > 0.0D)
							? ventasDolaresCredito.doubleValue()
							: 0.0D);
			model.addAttribute("ventasDolaresCredito", ventasDolaresCredito);
			Double ventasEurosCredito = this._facturaService.totalVentasByCondicionVentaCredito(emisorId,
					controlCaja.getUsuario().getId(), "2", controlCaja.getFechaApertura(), "EUR");
			ventasEurosCredito = Double.valueOf((ventasEurosCredito != null && ventasEurosCredito.doubleValue() > 0.0D)
					? ventasEurosCredito.doubleValue()
					: 0.0D);
			model.addAttribute("ventasEurosCredito", ventasEurosCredito);
			Double cxcColonesEfectivo = this._cxcService.findAllCXCPagadas(emisorId, "1",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "CRC");
			cxcColonesEfectivo = Double.valueOf((cxcColonesEfectivo != null && cxcColonesEfectivo.doubleValue() > 0.0D)
					? cxcColonesEfectivo.doubleValue()
					: 0.0D);
			model.addAttribute("cxcColonesEfectivo", cxcColonesEfectivo);
			Double cxcDolaresEfectivo = this._cxcService.findAllCXCPagadas(emisorId, "1",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "USD");
			cxcDolaresEfectivo = Double.valueOf((cxcDolaresEfectivo != null && cxcDolaresEfectivo.doubleValue() > 0.0D)
					? cxcDolaresEfectivo.doubleValue()
					: 0.0D);
			model.addAttribute("cxcDolaresEfectivo", cxcDolaresEfectivo);
			Double cxcEurosEfectivo = this._cxcService.findAllCXCPagadas(emisorId, "1",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "EUR");
			cxcEurosEfectivo = Double.valueOf(
					(cxcEurosEfectivo != null && cxcEurosEfectivo.doubleValue() > 0.0D) ? cxcEurosEfectivo.doubleValue()
							: 0.0D);
			model.addAttribute("cxcEurosEfectivo", cxcEurosEfectivo);
			Double cxcColonesTarjeta = this._cxcService.findAllCXCPagadas(emisorId, "2",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "CRC");
			cxcColonesTarjeta = Double.valueOf((cxcColonesTarjeta != null && cxcColonesTarjeta.doubleValue() > 0.0D)
					? cxcColonesTarjeta.doubleValue()
					: 0.0D);
			model.addAttribute("cxcColonesTarjeta", cxcColonesTarjeta);
			Double cxcDolaresTarjeta = this._cxcService.findAllCXCPagadas(emisorId, "2",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "USD");
			cxcDolaresTarjeta = Double.valueOf((cxcDolaresTarjeta != null && cxcDolaresTarjeta.doubleValue() > 0.0D)
					? cxcDolaresTarjeta.doubleValue()
					: 0.0D);
			model.addAttribute("cxcDolaresTarjeta", cxcDolaresTarjeta);
			Double cxcEurosTarjeta = this._cxcService.findAllCXCPagadas(emisorId, "2", controlCaja.getUsuario().getId(),
					controlCaja.getFechaApertura(), "EUR");
			cxcEurosTarjeta = Double.valueOf(
					(cxcEurosTarjeta != null && cxcEurosTarjeta.doubleValue() > 0.0D) ? cxcEurosTarjeta.doubleValue()
							: 0.0D);
			model.addAttribute("cxcEurosTarjeta", cxcEurosTarjeta);
			Double cxcColonesCheque = this._cxcService.findAllCXCPagadas(emisorId, "3",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "CRC");
			cxcColonesCheque = Double.valueOf(
					(cxcColonesCheque != null && cxcColonesCheque.doubleValue() > 0.0D) ? cxcColonesCheque.doubleValue()
							: 0.0D);
			model.addAttribute("cxcColonesCheque", cxcColonesCheque);
			Double cxcDolaresCheque = this._cxcService.findAllCXCPagadas(emisorId, "3",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "USD");
			cxcDolaresCheque = Double.valueOf(
					(cxcDolaresCheque != null && cxcDolaresCheque.doubleValue() > 0.0D) ? cxcDolaresCheque.doubleValue()
							: 0.0D);
			model.addAttribute("cxcDolaresCheque", cxcDolaresCheque);
			Double cxcEurosCheque = this._cxcService.findAllCXCPagadas(emisorId, "3", controlCaja.getUsuario().getId(),
					controlCaja.getFechaApertura(), "EUR");
			cxcEurosCheque = Double.valueOf(
					(cxcEurosCheque != null && cxcEurosCheque.doubleValue() > 0.0D) ? cxcEurosCheque.doubleValue()
							: 0.0D);
			model.addAttribute("cxcEurosCheque", cxcEurosCheque);
			Double cxcColonesDeposito = this._cxcService.findAllCXCPagadas(emisorId, "4",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "CRC");
			cxcColonesDeposito = Double.valueOf((cxcColonesDeposito != null && cxcColonesDeposito.doubleValue() > 0.0D)
					? cxcColonesDeposito.doubleValue()
					: 0.0D);
			model.addAttribute("cxcColonesDeposito", cxcColonesDeposito);
			Double cxcDolaresDeposito = this._cxcService.findAllCXCPagadas(emisorId, "4",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "USD");
			cxcDolaresDeposito = Double.valueOf((cxcDolaresDeposito != null && cxcDolaresDeposito.doubleValue() > 0.0D)
					? cxcDolaresDeposito.doubleValue()
					: 0.0D);
			model.addAttribute("cxcDolaresDeposito", cxcDolaresDeposito);
			Double cxcEurosDeposito = this._cxcService.findAllCXCPagadas(emisorId, "4",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "EUR");
			cxcEurosDeposito = Double.valueOf(
					(cxcEurosDeposito != null && cxcEurosDeposito.doubleValue() > 0.0D) ? cxcEurosDeposito.doubleValue()
							: 0.0D);
			model.addAttribute("cxcEurosDeposito", cxcEurosDeposito);
			Double cxcColonesCheques = this._cxcService.findAllCXCPagadas(emisorId, "3",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "CRC");
			cxcColonesCheques = Double.valueOf((cxcColonesCheques != null && cxcColonesCheques.doubleValue() > 0.0D)
					? cxcColonesCheques.doubleValue()
					: 0.0D);
			model.addAttribute("cxcColonesCheques", cxcColonesCheques);
			Double cxcDolaresCheques = this._cxcService.findAllCXCPagadas(emisorId, "3",
					controlCaja.getUsuario().getId(), controlCaja.getFechaApertura(), "USD");
			cxcDolaresCheques = Double.valueOf((cxcDolaresCheques != null && cxcDolaresCheques.doubleValue() > 0.0D)
					? cxcDolaresCheques.doubleValue()
					: 0.0D);
			model.addAttribute("cxcDolaresCheques", cxcDolaresCheques);
			Double cxcEurosCheques = this._cxcService.findAllCXCPagadas(emisorId, "3", controlCaja.getUsuario().getId(),
					controlCaja.getFechaApertura(), "EUR");
			cxcEurosCheques = Double.valueOf(
					(cxcEurosCheques != null && cxcEurosCheques.doubleValue() > 0.0D) ? cxcEurosCheques.doubleValue()
							: 0.0D);
			model.addAttribute("cxcEurosCheques", cxcEurosCheques);
			totalEfectivoColones = Double.valueOf(ventasColonesEfectivo.doubleValue() + cxcColonesEfectivo.doubleValue()
					+ entradasEfectivoColones.doubleValue() - salidasEfectivoColones.doubleValue()
					- totalDevolucionesColones.doubleValue());
			totalEfectivoDolares = Double.valueOf(ventasDolaresEfectivo.doubleValue() + cxcDolaresEfectivo.doubleValue()
					+ entradasEfectivoDolares.doubleValue() - salidasEfectivoDolares.doubleValue()
					- totalDevolucionesDolares.doubleValue());
			totalEfectivoEuros = Double.valueOf(ventasEurosEfectivo.doubleValue() + cxcEurosEfectivo.doubleValue()
					+ entradasEfectivoEuros.doubleValue() - salidasEfectivoEuros.doubleValue()
					- totalDepositosEuros.doubleValue());
			totalTarjetaColones = Double.valueOf(ventasColonesTarjeta.doubleValue() + cxcColonesTarjeta.doubleValue());
			totalTarjetaDolares = Double.valueOf(ventasDolaresTarjeta.doubleValue() + cxcDolaresTarjeta.doubleValue());
			totalTarjetaEuros = Double.valueOf(ventasEurosTarjeta.doubleValue() + cxcEurosTarjeta.doubleValue());
			totalChequeColones = Double.valueOf(ventasColonesCheque.doubleValue() + cxcColonesCheque.doubleValue());
			totalChequeDolares = Double.valueOf(ventasDolaresCheque.doubleValue() + cxcDolaresCheque.doubleValue());
			totalChequeEuros = Double.valueOf(ventasEurosCheque.doubleValue() + cxcEurosCheque.doubleValue());
			totalDepositosColones = Double
					.valueOf(ventasColonesDeposito.doubleValue() + cxcColonesDeposito.doubleValue());
			totalDepositosDolares = Double
					.valueOf(ventasDolaresDeposito.doubleValue() + cxcDolaresDeposito.doubleValue());
			totalDepositosEuros = Double.valueOf(ventasEurosDeposito.doubleValue() + cxcEurosDeposito.doubleValue());
			model.addAttribute("totalEfectivoColones", totalEfectivoColones);
			model.addAttribute("totalEfectivoDolares", totalEfectivoDolares);
			model.addAttribute("totalEfectivoEuros", totalEfectivoEuros);
			model.addAttribute("totalTarjetaColones", totalTarjetaColones);
			model.addAttribute("totalTarjetaDolares", totalTarjetaDolares);
			model.addAttribute("totalTarjetaEuros", totalTarjetaEuros);
			model.addAttribute("totalChequeColones", totalChequeColones);
			model.addAttribute("totalChequeDolares", totalChequeDolares);
			model.addAttribute("totalChequeEuros", totalChequeEuros);
			model.addAttribute("totalDepositosColones", totalDepositosColones);
			model.addAttribute("totalDepositosDolares", totalDepositosDolares);
			model.addAttribute("totalDepositosEuros", totalDepositosEuros);
			model.addAttribute("controlCaja", controlCaja);
			if (request.isUserInRole("ROLE_CAJAS_FULL"))
				return "/catalogos/control-cajas/form-completo";
			return "/catalogos/control-cajas/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/save" })
	public ResponseEntity<?> save(ControlCaja controlCaja, Model model, Authentication authentication,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
				Emisor emisor = this._emisorService.findById(emisorId);
				controlCaja.setEmisor(emisor);
				if (controlCaja.getId() != null && controlCaja.getId().longValue() > 0L) {
					controlCaja.setFechaCierre(new Date());
					controlCaja.setUsuarioCerro(UsuarioId);
					controlCaja.setEstadoCaja("C");
				} else {
					controlCaja.setFechaApertura(new Date());
					controlCaja.setUsuario(UsuarioId);
					controlCaja.setEstadoCaja("A");
				}
				this._cajaService.save(controlCaja);
				response.put("response", "1");
			} else {
				response.put("response", "0");
			}
		} catch (Exception e) {
			response.put("response", "2");
			e.printStackTrace();
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@GetMapping({ "/movimientos-efectivo/{id}" })
	public String movimientoEfectivo(Model model, HttpSession session, @PathVariable("id") Long cajaId) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<ControlCajaMovimientos> listaMovimientos = this._cajaMovimientoService
					.findAllByEmisorAndIdCaja(emisorId, cajaId);
			List<Moneda> listaMonedas = this._monedaService.findAll();
			String statusCaja = "A";
			ControlCaja status = this._cajaService.findByIdAndEmisorId(cajaId, emisorId);
			if (status != null && status.getEstadoCaja().equalsIgnoreCase("C"))
				statusCaja = "C";
			model.addAttribute("statusCaja", statusCaja);
			ControlCajaMovimientos controlCajaMovimientos = new ControlCajaMovimientos();
			model.addAttribute("controlCajaMovimientos", controlCajaMovimientos);
			model.addAttribute("listaMonedas", listaMonedas);
			model.addAttribute("CAJA_ID", cajaId);
			model.addAttribute("listaMovimientos", listaMovimientos);
			return "/catalogos/control-cajas/movimientos-efectivo/index";
		}
		return "redirect:/";
	}

	@PostMapping({ "/movimientos-efectivo/save" })
	public ResponseEntity<?> movimientosEfectivoSave(ControlCajaMovimientos controlCajaMovimientos, Model model,
			Authentication authentication, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
				controlCajaMovimientos.setUsuario(UsuarioId);
				controlCajaMovimientos.setFechaMovimiento(new Date());
				this._cajaMovimientoService.save(controlCajaMovimientos);
				response.put("response", "1");
			} else {
				response.put("response", "0");
			}
		} catch (Exception e) {
			response.put("response", "2");
			e.printStackTrace();
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}
}
