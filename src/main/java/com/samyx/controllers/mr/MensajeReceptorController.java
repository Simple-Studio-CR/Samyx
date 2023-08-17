package com.samyx.controllers.mr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samyx.models.entity.CCodigosTarifasIva;
import com.samyx.models.entity.CImpuesto;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.FEMensajeReceptor;
import com.samyx.models.entity.FEMensajeReceptorItems;
import com.samyx.models.entity.FEMensajeReceptorItemsImpuestos;
import com.samyx.models.entity.FEMensajeReceptorItemsImpuestosExoneraciones;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICCodigosTarifasIvaService;
import com.samyx.service.interfaces.ICImpuestoService;
import com.samyx.service.interfaces.ICProductoService;
import com.samyx.service.interfaces.IEmisorActividadesService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IFEMensajeReceptorAutomaticoService;
import com.samyx.service.interfaces.IFEMensajeReceptorService;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping({ "/mensaje-receptor" })
@Secured({ "ROLE_ADMIN", "ROLE_USER" })
public class MensajeReceptorController {
	@Value("${api.jmata.mensaje.receptor}")
	private String urlApiMensajeReceptor;

	@Value("${api.jmata.download.docs}")
	private String urlApiDownloadXml;

	@Value("${api.jmata.consulta.docs}")
	private String urlApiConsultaDocs;

	@Value("${api.jmata.consulta.cualquier.doc}")
	private String apiJmataConsultaCualquierDoc;

	@Autowired
	private IEmisorService _emisorService;

	@Autowired
	private ICImpuestoService _impuestoService;

	@Autowired
	private ICCodigosTarifasIvaService _tarifasIvaService;

	@Autowired
	private IFEMensajeReceptorService _mensajeReceptor;

	@Autowired
	private IFEMensajeReceptorAutomaticoService _mrinboxService;

	@Autowired
	public IEmisorActividadesService _actividadesService;

	@Autowired
	private IUsuarioService _usuario;

	@Autowired
	private ICProductoService _productoService;

	@GetMapping({ "/" })
	public String Home(Model model, @RequestParam(name = "page", defaultValue = "0") int page, HttpSession session,
			@RequestParam(name = "q", defaultValue = "") String q) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<FEMensajeReceptor> listaDocumentos = this._mensajeReceptor.findAllByEmisorId(emisorId, q.toUpperCase(),
					(Pageable) pageRequest);
			PageRender<FEMensajeReceptor> pageRender = new PageRender("/mensaje-receptor/", listaDocumentos);
			model.addAttribute("listaDocumentos", listaDocumentos);
			model.addAttribute("page", pageRender);
			return "/mensaje-receptor/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/view/{id}" })
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			FEMensajeReceptor mensajeReceptor = this._mensajeReceptor.findMrByEmisorIdAndId(emisorId, id);
			model.addAttribute("mensajeReceptor", mensajeReceptor);
			model.addAttribute("urlApiDownloadXml", this.urlApiDownloadXml);
			model.addAttribute("identificacionEmpresa", session.getAttribute("SESSION_IDENTIFICACION_EMPRESA"));
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
			return "/mensaje-receptor/view";
		}
		return "redirect:/";
	}

	@GetMapping({ "/form" })
	public String Form(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			if (session.getAttribute("SESSION_SUCURSAL_ID") != null
					&& session.getAttribute("SESSION_TERMINAL_ID") != null) {
				Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
				List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
				FEMensajeReceptor mensajeReceptor = new FEMensajeReceptor();
				model.addAttribute("mensajeReceptor", mensajeReceptor);
				model.addAttribute("listaActividades", listaActividades);
				return "/mensaje-receptor/form";
			}
			return "redirect:/proformas/seleccionar-terminal?urlRetorno=/mensaje-receptor/form";
		}
		return "redirect:/";
	}

	@PostMapping(value = { "/recepcion" }, produces = { "application/json" })
	@ResponseBody
	public String recepcionMr(FEMensajeReceptor mr, HttpSession session, Authentication auth,
			HttpServletRequest request, @RequestParam(name = "NumeroLinea[]", required = false) int[] NumeroLinea,
			@RequestParam(name = "Codigo[]", required = false) String[] Codigo,
			@RequestParam(name = "Cantidad[]", required = false) Double[] Cantidad,
			@RequestParam(name = "UnidadMedida[]", required = false) String[] UnidadMedida,
			@RequestParam(name = "Detalle[]", required = false) String[] Detalle,
			@RequestParam(name = "PrecioUnitario[]", required = false) Double[] PrecioUnitario,
			@RequestParam(name = "MontoTotal[]", required = false) Double[] MontoTotal,
			@RequestParam(name = "SubTotal[]", required = false) Double[] SubTotal,
			@RequestParam(name = "MontoDescuento[]", required = false) Double[] MontoDescuento,
			@RequestParam(name = "NaturalezaDescuento[]", required = false) String[] NaturalezaDescuento,
			@RequestParam(name = "ImpuestoNeto[]", required = false) Double[] ImpuestoNeto,
			@RequestParam(name = "MontoTotalLinea[]", required = false) Double[] MontoTotalLinea,
			@RequestParam(name = "form", required = false) String FormProcesa,
			@RequestParam(name = "idMrInbox", required = false) String idMrInbox) {
		String j = "";
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			if (session.getAttribute("SESSION_SUCURSAL_ID") != null
					&& session.getAttribute("SESSION_TERMINAL_ID") != null) {
				Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Emisor emisor = this._emisorService.findById(empresaId);
				System.out.println(
						"MR1 = " + mr.getIdentificacionReceptorMr() + " |||| MR2 = " + emisor.getIdentificacion());
				if (!mr.getIdentificacionReceptorMr().equals(emisor.getIdentificacion())) {
					j = j + "{";
					j = j + "\"response\":\"401\",";
					j = j + "\"msj\":\"Esta factura no está dirigida a su empresa, esta es la identificación: "
							+ mr.getIdentificacionReceptorMr()
							+ ", revise y contacte a su proveedor para que aplique las correcciones.\"";
					j = j + "}";
					return j;
				}
				String estadoDocumento = "";
				if (emisor != null) {
					String jm = "";
					jm = jm + "{";
					jm = jm + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
					jm = jm + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
					jm = jm + "\"clave\":\"" + mr.getClave() + "\"";
					jm = jm + "}";
					CloseableHttpClient client = HttpClients.createDefault();
					HttpPost httpPost = new HttpPost(this.apiJmataConsultaCualquierDoc);
					try {
						String json = jm;
						StringEntity entity = new StringEntity(json, "UTF-8");
						httpPost.setEntity((HttpEntity) entity);
						httpPost.setHeader("Accept", "application/json");
						httpPost.setHeader("Content-type", "application/json");
						CloseableHttpResponse response = client.execute((HttpUriRequest) httpPost);
						HttpEntity entity2 = response.getEntity();
						String responseMh = EntityUtils.toString(entity2, "UTF-8");
						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode data = objectMapper.readTree(responseMh);
						estadoDocumento = data.path("ind-estado").asText();
					} catch (Exception e) {
						estadoDocumento = "nulo";
					}
				}
				if (!estadoDocumento.equalsIgnoreCase("aceptado")) {
					j = j + "{";
					j = j + "\"response\":\"401\",";
					j = j + "\"msj\":\"Este comprobante no ha sido recibido en el Ministerio de Hacienda, intente más tarde. \\n\\n Es probable que el proveedor aún no lo haya procesado. \\n\\n Si ya transcurrió más de tres horas contacte a su proveedor y le indica que la factura tiene como respuesta de hacienda lo siguiente: “El comprobante no ha sido recibido”\"";
					j = j + "}";
					return j;
				}
				Usuario usuario = this._usuario.findByUsername(auth.getName());
				String codigo = "";
				switch (mr.getTipoDocumento()) {
				case "CCE":
					codigo = "1";
					break;
				case "CPCE":
					codigo = "2";
					break;
				case "RCE":
					codigo = "3";
					break;
				}
				j = "{";
				j = j + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
				j = j + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
				j = j + "\"tipoDocumento\":\"" + mr.getTipoDocumento() + "\",";
				j = j + "\"NumeroCedulaEmisor\":\"" + mr.getIdentificacionEmisorMr() + "\",";
				j = j + "\"fechaEmisionDoc\":\"" + mr.getFechaEmision() + "\",";
				j = j + "\"clave\":\"" + mr.getClave() + "\",";
				j = j + "\"mensaje\":\"" + codigo + "\",";
				j = j + "\"detalleMensaje\":\"" + procesarTexto(mr.getDetalleMensaje()) + "\",";
				j = j + "\"codigo_actividad\":\"" + mr.getCodigoActividadEmisor() + "\",";
				j = j + "\"correoDistribucion\":\"" + procesarTexto(mr.getCorreoEmisorMr().trim()) + "\",";
				j = j + "\"condicion_impuesto\":\"" + mr.getCondicionImpuesto() + "\",";
				if (mr.getMontoTotalImpuestoAcreditar() != null
						&& mr.getMontoTotalImpuestoAcreditar().doubleValue() > 0.0D)
					j = j + "\"monto_total_impuesto_acreditar\":\"" + mr.getMontoTotalImpuestoAcreditar() + "\",";
				if (mr.getMontoTotalDeGastoAplicable() != null
						&& mr.getMontoTotalDeGastoAplicable().doubleValue() > 0.0D)
					j = j + "\"monto_total_de_gasto_aplicable\":\"" + mr.getMontoTotalDeGastoAplicable() + "\",";
				if (mr.getImpuestos() != null)
					j = j + "\"montoTotalImpuesto\":\"" + mr.getImpuestos() + "\",";
				j = j + "\"totalFactura\":\"" + mr.getTotalFactura() + "\",";
				j = j + "\"situacion\":\"normal\",";
				j = j + "\"sucursal\":\"" + session.getAttribute("SESSION_NUMERO_SUCURSAL").toString() + "\",";
				j = j + "\"terminal\":\"" + session.getAttribute("SESSION_NUMERO_TERMINAL").toString() + "\",";
				j = j + "\"razon\":\"" + mr.getDetalleMensaje() + "\"";
				j = j + "}";
				try {
					CloseableHttpClient client = HttpClients.createDefault();
					HttpPost httpPost = new HttpPost(this.urlApiMensajeReceptor);
					String json = j;
					StringEntity entity = new StringEntity(json, "UTF-8");
					httpPost.setEntity((HttpEntity) entity);
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");
					CloseableHttpResponse response = client.execute((HttpUriRequest) httpPost);
					HttpEntity entity2 = response.getEntity();
					String responseString = EntityUtils.toString(entity2, "UTF-8");
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode data = objectMapper.readTree(responseString);
					if (data.path("response") != null && data.path("response").asInt() == 200) {
						Double m1 = Double.valueOf((data.path("monto_total_impuesto_acreditar") != null
								&& data.path("monto_total_impuesto_acreditar").asDouble() > 0.0D)
										? data.path("monto_total_impuesto_acreditar").asDouble()
										: 0.0D);
						Double m2 = Double.valueOf((data.path("monto_total_de_gasto_aplicable") != null
								&& data.path("monto_total_de_gasto_aplicable").asDouble() > 0.0D)
										? data.path("monto_total_de_gasto_aplicable").asDouble()
										: 0.0D);
						mr.setFechaCreacion(new Date());
						mr.setEmisor(emisor);
						mr.setUsuario(usuario);
						mr.setClaveGenerada(data.path("clave").asText());
						if (mr.getImpuestos() == null || mr.getImpuestos().doubleValue() < 0.0D)
							mr.setImpuestos(0.0D);
						if (mr.getMoneda() == null || mr.getMoneda().length() <= 0) {
							mr.setMoneda("CRC");
							mr.setTipoCambio(1.0D);
						}
						try {
							mr.setTipoCambio(mr.getTipoCambio());
						} catch (Exception e) {
							mr.setTipoCambio(1.0D);
						}
						mr.setMontoTotalImpuestoAcreditar(m1);
						mr.setMontoTotalDeGastoAplicable(m2);
						mr.setConsecutivoGenerado(data.path("consecutivo").asText());
						mr.setFechaEmisionGenerado(data.path("fechaEmision").asText());
						mr.setFileXmlSign(data.path("fileXmlSign").asText());
						mr.setResponseCode(data.path("response").asInt());
						for (int i = 0; i < NumeroLinea.length; i++) {
							FEMensajeReceptorItems items = new FEMensajeReceptorItems();
							items.setNumeroLinea(NumeroLinea[i]);
							try {
								items.setCodigo(Codigo[i]);
							} catch (Exception exception) {
							}
							items.setDetalleProducto(Detalle[i]);
							items.setUnidadDeMedida(UnidadMedida[i]);
							items.setCantidad(Cantidad[i]);
							items.setPrecioUnitario(PrecioUnitario[i]);
							items.setMontoTotal(MontoTotal[i]);
							items.setSubTotal(SubTotal[i]);
							try {
								items.setMontoDescuento(MontoDescuento[i]);
								items.setNaturalezaDescuento(NaturalezaDescuento[i]);
							} catch (Exception exception) {
							}
							try {
								String[] iCodigo = request.getParameterValues("iCodigo" + NumeroLinea[i] + "[]");
								String[] iCodigoTarifa = request
										.getParameterValues("iCodigoTarifa" + NumeroLinea[i] + "[]");
								String[] iTarifa = request.getParameterValues("iTarifa" + NumeroLinea[i] + "[]");
								String[] iMonto = request.getParameterValues("iMonto" + NumeroLinea[i] + "[]");
								String[] TipoDocumento = request
										.getParameterValues("TipoDocumento" + NumeroLinea[i] + "[]");
								String[] NumeroDocumento = request
										.getParameterValues("NumeroDocumento" + NumeroLinea[i] + "[]");
								String[] NombreInstitucion = request
										.getParameterValues("NombreInstitucion" + NumeroLinea[i] + "[]");
								String[] FechaEmision = request
										.getParameterValues("FechaEmision" + NumeroLinea[i] + "[]");
								String[] PorcentajeExoneracion = request
										.getParameterValues("PorcentajeExoneracion" + NumeroLinea[i] + "[]");
								String[] MontoExoneracion = request
										.getParameterValues("MontoExoneracion" + NumeroLinea[i] + "[]");
								for (int q = 0; q < iCodigo.length; q++) {
									FEMensajeReceptorItemsImpuestos iif = new FEMensajeReceptorItemsImpuestos();
									CImpuesto impuesto = this._impuestoService
											.findById(Long.valueOf(Long.parseLong(iCodigo[q].substring(1, 2))));
									iif.setImpuesto(impuesto);
									try {
										CCodigosTarifasIva tiva = this._tarifasIvaService
												.findById(Long.valueOf(Long.parseLong(iCodigoTarifa[q])));
										iif.setCodigoTarifaIva(tiva);
									} catch (Exception exception) {
									}
									iif.setTarifa(Double.valueOf(Double.parseDouble(iTarifa[q])));
									iif.setMonto(Double.valueOf(Double.parseDouble(iMonto[q])));
									try {
										FEMensajeReceptorItemsImpuestosExoneraciones eiif = new FEMensajeReceptorItemsImpuestosExoneraciones();
										eiif.setTipoDocumento(TipoDocumento[q]);
										eiif.setNumeroDocumento(NumeroDocumento[q]);
										eiif.setNombreInstitucion(NombreInstitucion[q]);
										eiif.setFechaEmision(FechaEmision[q]);
										eiif.setPorcentajeCompra(Integer.parseInt(PorcentajeExoneracion[q]));
										eiif.setMontoImpuesto(Double.valueOf(Double.parseDouble(MontoExoneracion[q])));
										iif.addItemFacturaImpuestosExoneracion(eiif);
									} catch (Exception e) {
										System.out.println("No tiene exoneraciones");
									}
									items.addFEImpuestosItemFactura(iif);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							try {
								items.setImpuestoNeto(ImpuestoNeto[i]);
							} catch (Exception e) {
								items.setImpuestoNeto(Double.valueOf(0.0D));
							}
							items.setMontoTotalLinea(MontoTotalLinea[i]);
							mr.addFEItemsMR(items);
						}
						this._mensajeReceptor.save(mr);
						if (FormProcesa != null && FormProcesa.equalsIgnoreCase("inbox"))
							this._mrinboxService.updateEstadoMrInbox("A", Long.valueOf(Long.parseLong(idMrInbox)));
					}
					client.close();
					return responseString;
				} catch (Exception e) {
					e.printStackTrace();
					j = j + "{";
					j = "\"response\":\"400\"";
					j = j + "}";
					return j;
				}
			}
			return "redirect:/proformas/seleccionar-terminal";
		}
		return "redirect:/";
	}

	@PostMapping(value = { "/consultar-documentos" }, produces = { "application/json" })
	@ResponseBody
	public String consultarDocumentos(Model model, HttpSession session) {
		String resp = "";
		String j = "";
		int count = 0;
		int docsPendientes = 0;
		if (session.getAttribute("SESSION_EMPRESA_ID") != null)
			try {
				Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Emisor emisor = this._emisorService.findById(empresaId);
				List<FEMensajeReceptor> listaDocumentos = this._mensajeReceptor.findAllByEmisorId(empresaId);
				CloseableHttpClient client = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(this.urlApiConsultaDocs);
				StringEntity entity = null;
				CloseableHttpResponse response = null;
				HttpEntity entity2 = null;
				ObjectMapper objectMapper = null;
				JsonNode data = null;
				String json = "";
				for (FEMensajeReceptor b : listaDocumentos) {
					j = "{";
					j = j + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
					j = j + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
					j = j + "\"clave\":\"" + b.getClaveGenerada() + "\"";
					j = j + "}";
					json = j;
					entity = new StringEntity(json, "UTF-8");
					httpPost.setEntity((HttpEntity) entity);
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");
					response = client.execute((HttpUriRequest) httpPost);
					entity2 = response.getEntity();
					String responseString = EntityUtils.toString(entity2, "UTF-8");
					objectMapper = new ObjectMapper();
					data = objectMapper.readTree(responseString);
					if (data.path("response") != null && data.path("response").asInt() == 200) {
						if (data.path("ind-estado").asText().equals("procesando")) {
							docsPendientes++;
							continue;
						}
						count++;
						this._mensajeReceptor.updateEstadoDocByClave(data.path("ind-estado").asText(),
								data.path("fecha-aceptacion").asText(), data.path("xml-aceptacion").asText(),
								b.getClave());
					}
				}
				client.close();
				j = "{";
				j = j + "\"response\":\"200\",";
				j = j + "\"docsConsultados\":\"" + count + "\",";
				j = j + "\"docsPendientes\":\"" + docsPendientes + "\"";
				j = j + "}";
				resp = j;
			} catch (Exception e) {
				e.printStackTrace();
				j = j + "{";
				j = "\"response\":\"400\",";
				j = j + "\"docsConsultados\":\"0\",";
				j = j + "\"docsPendientes\":\"" + docsPendientes + "\"";
				j = j + "}";
				resp = j;
			}
		return resp;
	}

	public String procesarTexto(String j) {
		String r = "";
		r = StringEscapeUtils.escapeJson(j);
		return r;
	}

	@PostMapping({ "/aplicar-inventario" })
	public ResponseEntity<?> AplicarInventario(Model model, HttpSession session, Authentication auth,
			@RequestParam(name = "pc", defaultValue = "0.00") Double pc,
			@RequestParam(name = "ppro", defaultValue = "0.00") Double ppro,
			@RequestParam(name = "pv", defaultValue = "0.00") Double pv,
			@RequestParam(name = "cantidad", defaultValue = "0.00") Double cantidad,
			@RequestParam(name = "utilidad", defaultValue = "0.00") Double utilidad,
			@RequestParam(name = "utilidadF", defaultValue = "0.00") Double utilidadF,
			@RequestParam(name = "pvf", defaultValue = "0.00") Double pvf, @RequestParam(name = "ci") Long idProducto,
			@RequestParam(name = "id") Long idLineaProducto,
			@RequestParam(name = "fpu", defaultValue = "0.00") Double fpu,
			@RequestParam(name = "pasiv", defaultValue = "0.00") Double precioActualCatalogo) {
		Map<String, Object> response = new HashMap<>();
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
			Usuario usuario = this._usuario.findByUsername(auth.getName());
			try {
				Double cantidadArticulos = Double.valueOf(0.0D);
				try {
					if (fpu.doubleValue() > 0.0D) {
						cantidadArticulos = Double.valueOf(cantidad.doubleValue() * fpu.doubleValue());
					} else {
						cantidadArticulos = cantidad;
					}
				} catch (Exception e) {
					cantidadArticulos = cantidad;
				}
				this._productoService.aplicarInventarioMr(pc, ppro, pv, cantidadArticulos, utilidad, utilidadF, pvf,
						idProducto, emisorId);
				this._productoService.aplicarInventarioMrLinea("A", pv, utilidad, utilidadF, pvf, idProducto,
						usuario.getId(), new Date(), precioActualCatalogo, idLineaProducto);
				response.put("response", "200");
				response.put("messaje", "OK");
				return new ResponseEntity(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				response.put("response", "400");
				response.put("messaje", "ERROR");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
		}
		response.put("response", "400");
		response.put("messaje", "ERROR");
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}

	@PostMapping({ "/aplicar-factura" })
	public ResponseEntity<?> AplicarFacturaInventario(Model model, HttpSession session,
			@RequestParam(name = "id") Long idFactura, Authentication auth) {
		Map<String, Object> response = new HashMap<>();
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
			Usuario usuario = this._usuario.findByUsername(auth.getName());
			try {
				this._mensajeReceptor.aplicarFacturaCompra(usuario, "A", new Date(), idFactura, emisorId);
				response.put("response", "200");
				response.put("messaje", "OK");
				return new ResponseEntity(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				response.put("response", "400");
				response.put("messaje", "ERROR");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
		}
		response.put("response", "400");
		response.put("messaje", "ERROR");
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}
}
