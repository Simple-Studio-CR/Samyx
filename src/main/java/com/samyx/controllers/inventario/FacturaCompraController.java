package com.samyx.controllers.inventario;

import com.samyx.models.entity.CImpuesto;
import com.samyx.models.entity.CInventarioMovimiento;
import com.samyx.models.entity.CInventarioMovimientoItems;
import com.samyx.models.entity.CInventarioMovimientoItemsImpuesto;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Producto;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICImpuestoService;
import com.samyx.service.interfaces.ICInventarioMovimientoService;
import com.samyx.service.interfaces.ICProductoService;
import com.samyx.service.interfaces.IClienteService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.apache.commons.text.StringEscapeUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/inventario/factura-compra" })
public class FacturaCompraController {
	@Autowired
	private IClienteService _proveedor;

	@Autowired
	private IMonedaService _monedaService;

	@Autowired
	private IEmisorService _emisorService;

	@Autowired
	private ICProductoService _productoService;

	@Autowired
	private ICInventarioMovimientoService _inventMovService;

	@Autowired
	private ICImpuestoService _impuestoService;

	@Autowired
	private IUsuarioService _usuarioService;

	@GetMapping({ "/" })
	public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<CInventarioMovimiento> ListaMovimientos = null;
			ListaMovimientos = this._inventMovService.findAllByEmisorId(emisorId, q.toUpperCase(),
					(Pageable) pageRequest);
			PageRender<CInventarioMovimiento> pageRender = new PageRender("/inventario/factura-compra/",
					ListaMovimientos);
			model.addAttribute("ListaMovimientos", ListaMovimientos);
			model.addAttribute("page", pageRender);
			return "/catalogos/productos/inventario/factura-compra/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/registrar" })
	public String registrarFactura(Model model, HttpSession session, Authentication auth) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Usuario usuario = this._usuarioService.findByUsername(auth.getName());
			CInventarioMovimiento inventario = new CInventarioMovimiento();
			List<Moneda> listaMonedas = this._monedaService.findAll();
			model.addAttribute("inventario", inventario);
			model.addAttribute("listaMonedas", listaMonedas);
			if (session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO") != null
					&& session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO").toString().equalsIgnoreCase("S")) {
				model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "S");
			} else {
				model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "M");
			}
			model.addAttribute("V_FACTURADOR", "V1");
			if (session.getAttribute("SESSION_TIPO_CLIENTE").toString().equalsIgnoreCase("A")) {
				model.addAttribute("TXT_CANTIDAD_UNIDAD", "Cantidad");
				model.addAttribute("TXT_FRACCIONES", "Fracciones");
				model.addAttribute("TXT_PRECIO_UNIDAD", "Precio");
				model.addAttribute("TXT_PRECIO_FRACCIONES", "Precio");
				model.addAttribute("TIPO_VENTA", "A");
			} else {
				model.addAttribute("TXT_CANTIDAD_UNIDAD", "Unidades");
				model.addAttribute("TXT_FRACCIONES", "Fracciones");
				model.addAttribute("TXT_PRECIO_UNIDAD", "Precio unidad");
				model.addAttribute("TXT_PRECIO_FRACCIONES", "Precio fracción");
				model.addAttribute("TIPO_VENTA", "B");
			}
			return "/catalogos/productos/inventario/factura-compra/form";
		}
		return "redirect:/";
	}

	@GetMapping({ "/view/{id}" })
	public String verFactura(Model model, HttpSession session, @PathVariable("id") Long id) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			CInventarioMovimiento inventario = this._inventMovService.findByEmisorIdAndIdFactura(emisorId, id);
			List<Moneda> listaMonedas = this._monedaService.findAll();
			model.addAttribute("listaMonedas", listaMonedas);
			model.addAttribute("inventario", inventario);
			return "/catalogos/productos/inventario/factura-compra/view";
		}
		return "redirect:/";
	}

	@GetMapping(value = { "/proveedor" }, produces = { "application/json" })
	@ResponseBody
	public List<Cliente> getProveedores(@RequestParam(name = "term", required = false) String term,
			HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			return this._proveedor.findByIdentificacionOrNombreCompleto(emisorId, "P", term.toUpperCase());
		}
		return null;
	}

	@Transactional
	@PostMapping({ "/save" })
	public ResponseEntity<?> registrarCompra(Model model, HttpSession session, CInventarioMovimiento entity,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Double[] cantidad,
			@RequestParam(name = "partidaArancelaria[]", required = false) String[] partidaArancelaria,
			@RequestParam(name = "detalleProducto[]", required = false) String[] detalleProducto,
			@RequestParam(name = "codigoProducto1[]", required = false) String[] codigoProducto1,
			@RequestParam(name = "codigoProducto2[]", required = false) String[] codigoProducto2,
			@RequestParam(name = "unidadDeMedida[]", required = false) String[] unidadDeMedida,
			@RequestParam(name = "nombreProducto[]", required = false) String[] nombreProducto,
			@RequestParam(name = "precioCompra[]", required = false) Double[] precioCompra,
			@RequestParam(name = "utilidad[]", required = false) Double[] utilidad,
			@RequestParam(name = "precioPromediado[]", required = false) Double[] precioPromediado,
			@RequestParam(name = "precioUnitario[]", required = false) Double[] precioUnitario,
			@RequestParam(name = "descuentoTotal[]", required = false) Double[] descuentoTotal,
			@RequestParam(name = "subtotal[]", required = false) Double[] subTotal,
			@RequestParam(name = "impuestoTotal[]", required = false) Double[] impuestoTotal,
			@RequestParam(name = "totalLinea[]", required = false) Double[] totalLinea,
			@RequestParam(name = "impuestoTarifaIva[]", required = false) String[] impuestoTarifaIva,
			@RequestParam(name = "impuestoNeto[]", required = false) String[] impuestoNeto,
			@RequestParam(name = "tipoVentaLinea[]", required = false) String[] tipoVentaLinea,
			@RequestParam(name = "fraccionesPorUnidad[]", required = false) Double[] fraccionesPorUnidad,
			@RequestParam(name = "fraccion[]", required = false) Double[] fraccion,
			@RequestParam(name = "precioFraccion[]", required = false) Double[] precioFraccion,
			@RequestParam(name = "utilidadFraccion[]", required = false) Double[] utilidadFraccion,
			@RequestParam(name = "precioVentaFraccion[]", required = false) Double[] precioVentaFraccion,
			HttpServletRequest request, Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Emisor emisor = this._emisorService.findById(empresaId);
				CInventarioMovimiento secuencia = this._inventMovService.secuenciaFacturaCompra(empresaId);
				Long secuenciaFacturaCompraFinal = Long.valueOf(Long.parseLong("0"));
				if (secuencia != null && secuencia.getSecuenciaFacturaCompra().longValue() > 0L) {
					secuenciaFacturaCompraFinal = Long.valueOf(secuencia.getSecuenciaFacturaCompra().longValue() + 1L);
				} else {
					secuenciaFacturaCompraFinal = Long.valueOf(Long.parseLong("1"));
				}
				entity.setSecuenciaFacturaCompra(secuenciaFacturaCompraFinal);
				entity.setEmisor(emisor);
				entity.setFechaIngresoSistema(new Date());
				Usuario usuario = this._usuarioService.findByUsername(authentication.getName());
				entity.setUsuario(usuario);
				int linea = 0;
				String naturalezaDescuento = "";
				for (int i = 0; i < itemId.length; i++) {
					String detalleProductoFinal;
					linea++;
					Double montoTotal = Double.valueOf(Double.parseDouble(cantidad[i].toString())
							* Double.parseDouble(precioUnitario[i].toString()));
					Double descuento = Double.valueOf(
							(descuentoTotal[i].toString() != null) ? Double.parseDouble(descuentoTotal[i].toString())
									: 0.0D);
					Double subTotalLinea = subTotal[i];
					try {
						detalleProductoFinal = " - " + detalleProducto[i].toString();
					} catch (Exception e) {
						detalleProductoFinal = "";
					}
					if (descuento.doubleValue() > 0.0D)
						naturalezaDescuento = "Cliente Frecuente.";
					CInventarioMovimientoItems item = new CInventarioMovimientoItems();
					Producto producto = this._productoService
							.findById(Long.valueOf(Long.parseLong(itemId[i].toString())));
					item.setNumeroLinea(linea);
					item.setProducto(producto);
					item.setDetalleProducto(procesarTexto(detalleProductoFinal));
					item.setCantidad(Double.valueOf(Double.parseDouble(cantidad[i].toString())));
					item.setPrecioCompra(Double.valueOf(Double.parseDouble(precioCompra[i].toString())));
					item.setPrecioPromediado(Double.valueOf(Double.parseDouble(precioPromediado[i].toString())));
					item.setPrecioUnitario(Double.valueOf(Double.parseDouble(precioUnitario[i].toString())));
					item.setMontoTotal(Double.valueOf(Double.parseDouble(montoTotal.toString())));
					item.setSubTotal(subTotalLinea);
					item.setMontoDescuento(Double.valueOf(Double.parseDouble(descuento.toString())));
					item.setNaturalezaDescuento(naturalezaDescuento);
					item.setMontoTotalLinea(Double.valueOf(Double.parseDouble(totalLinea[i].toString())));
					Double cantidadArticulos = Double.valueOf(0.0D);
					Double _fraccionesPorUnidad = Double.valueOf(0.0D);
					try {
						if (!tipoVentaLinea[i].isEmpty() && tipoVentaLinea[i].toString().equals("F")) {
							_fraccionesPorUnidad = fraccionesPorUnidad[i];
							try {
								if (Double.parseDouble(cantidad[i].toString()) > 0.0D)
									cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[i].toString())
											* _fraccionesPorUnidad.doubleValue());
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								if (fraccion[i].doubleValue() > 0.0D)
									cantidadArticulos = Double
											.valueOf(cantidadArticulos.doubleValue() + fraccion[i].doubleValue());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[i].toString()));
						}
					} catch (Exception e) {
						cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[i].toString()));
					}
					if (producto.getAfectaInventario().equals("1"))
						this._productoService.registroEntradasInventUpdatePrecios(
								Double.valueOf(Double.parseDouble(precioCompra[i].toString())),
								Double.valueOf(Double.parseDouble(precioPromediado[i].toString())),
								Double.valueOf(Double.parseDouble(precioUnitario[i].toString())),
								Double.valueOf(Double.parseDouble(cantidadArticulos.toString())),
								Double.valueOf(Double.parseDouble(utilidad[i].toString())), utilidadFraccion[i],
								precioVentaFraccion[i], producto.getId());
					String[] impuestosId = request.getParameterValues("impuestosId" + itemId[i] + "[]");
					String[] Impuestoimpuestos = request.getParameterValues("Impuestoimpuestos" + itemId[i] + "[]");
					String[] impuestosMonto = request.getParameterValues("impuestosMonto" + itemId[i] + "[]");
					if (impuestosId != null && impuestosId.length > 0) {
						for (int q = 0; q < impuestosId.length; q++) {
							CInventarioMovimientoItemsImpuesto iif = new CInventarioMovimientoItemsImpuesto();
							CImpuesto impuesto = this._impuestoService
									.findById(Long.valueOf(Long.parseLong(impuestosId[q])));
							iif.setImpuesto(impuesto);
							iif.setTarifa(Double.valueOf(Double.parseDouble(Impuestoimpuestos[q])));
							iif.setMonto(Double.valueOf(Double.parseDouble(impuestosMonto[q].replaceAll(" ", ""))));
							item.addCInventarioMovimientoItemsImpuesto(iif);
						}
						entity.addCInventarioMovimientoItems(item);
					}
				}
				entity.setTotalServGravados(
						Double.parseDouble(request.getParameter("totalServiciosGravados").replaceAll(" ", "")));
				entity.setTotalServExentos(
						Double.parseDouble(request.getParameter("totalServiciosExentos").replaceAll(" ", "")));
				entity.setTotalServExonerado(
						Double.parseDouble(request.getParameter("totalServiciosExonerado").replaceAll(" ", "")));
				entity.setTotalMercGravadas(
						Double.parseDouble(request.getParameter("totalMercanciaGravadas").replaceAll(" ", "")));
				entity.setTotalMercExentas(
						Double.parseDouble(request.getParameter("totalMercanciasExentas").replaceAll(" ", "")));
				entity.setTotalMercExonerada(
						Double.parseDouble(request.getParameter("totalMercanciasExonerada").replaceAll(" ", "")));
				entity.setTotalGravados(Double.parseDouble(request.getParameter("totalGravados").replaceAll(" ", "")));
				entity.setTotalExentos(Double.parseDouble(request.getParameter("totalExentos").replaceAll(" ", "")));
				entity.setTotalExonerado(
						Double.parseDouble(request.getParameter("totalExonerado").replaceAll(" ", "")));
				entity.setTotalVentas(Double.parseDouble(request.getParameter("totalVenta").replaceAll(" ", "")));
				entity.setTotalDescuentos(Double.parseDouble(request.getParameter("descuentos").replaceAll(" ", "")));
				entity.setTotalVentaNeta(
						Double.parseDouble(request.getParameter("totalVentaNeta").replaceAll(" ", "")));
				entity.setTotalImp(Double.parseDouble(request.getParameter("totalImpuestos").replaceAll(" ", "")));
				entity.setTotalIVADevuelto(
						Double.parseDouble(request.getParameter("totalIVADevuelto").replaceAll(" ", "")));
				entity.setTotalOtrosCargos(
						Double.parseDouble(request.getParameter("totalOtrosCargos").replaceAll(" ", "")));
				entity.setTotalComprobante(
						Double.parseDouble(request.getParameter("totalFactura").replaceAll(" ", "")));
				this._inventMovService.save(entity);
				response.put("response", "200");
			} else {
				response.put("response", "401");
			}
		} catch (Exception e) {
			response.put("response", "400");
			e.printStackTrace();
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	public String procesarNumeros(String j, String decimales) {
		NumberFormat formatter = new DecimalFormat(decimales);
		String r = "";
		r = (j != null && !j.equals("")) ? j : "0.00000";
		r = formatter.format(Double.parseDouble(r));
		r = r.replaceAll(",", ".");
		return r;
	}

	public String procesarTexto(String j) {
		String r = "";
		r = StringEscapeUtils.escapeJson(j);
		return r;
	}
}
