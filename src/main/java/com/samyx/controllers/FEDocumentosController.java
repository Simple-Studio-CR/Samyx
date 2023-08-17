package com.samyx.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.FEBitacora;
import com.samyx.models.entity.FEFactura;
import com.samyx.models.entity.FEFacturaItem;
import com.samyx.service.interfaces.ICProductoService;
import com.samyx.service.interfaces.IClienteService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IFEBitacoraService;
import com.samyx.service.interfaces.IFEFacturaService;
import com.samyx.service.interfaces.IFEFacturasCXCServices;
import com.samyx.util.PageRender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/fe-documentos" })
@Secured({ "ROLE_ADMIN", "ROLE_USER" })
public class FEDocumentosController {
	@Autowired
	private IFEFacturaService _facturaService;

	@Autowired
	private IFEBitacoraService _bitacoraService;

	@Value("${api.jmata.download.docs}")
	private String urlApiDownloadXml;

	@Value("${api.jmata.recepcion.nd.nc}")
	private String urlApiRecepcionNdNc;

	@Value("${api.jmata.consulta.docs}")
	private String urlApiConsultaDocs;

	@Value("${api.jmata.reenviar.xmls}")
	private String urlApiReenviarXmls;

	@Value("${api.jmata.imprimir.pdf}")
	private String urlApiImprimirPdf;

	@Autowired
	private ICProductoService _productoService;

	@Autowired
	private IEmisorService _emisorService;

	@Autowired
	private IClienteService _clienteService;

	@Autowired
	private IFEFacturasCXCServices _cxcFacturasService;

	@GetMapping({ "/" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			if (session.getAttribute("SESSION_SUCURSAL_ID") != null
					&& session.getAttribute("SESSION_TERMINAL_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				PageRequest pageRequest = PageRequest.of(page, 15,
						Sort.by(new String[] { "numeroFactura" }).descending());
				Page<FEFactura> ListaDocumentos = null;
				ListaDocumentos = this._facturaService.findAllByEmisorId(emisorId, q.toUpperCase(),
						(Pageable) pageRequest);
				PageRender<FEFactura> pageRender = new PageRender("/fe-documentos/", ListaDocumentos);
				model.addAttribute("ListaDocumentos", ListaDocumentos);
				model.addAttribute("page", pageRender);
				return "/catalogos/fe-documentos/index";
			}
			return "redirect:/proformas/seleccionar-terminal?urlRetorno=/fe-documentos/";
		}
		return "redirect:/";
	}

	@GetMapping({ "/view/{id}" })
	public String verFactura(Model model, @PathVariable("id") Long id, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			FEFactura factura = this._facturaService.findByEmisorById(emisorId, id);
			model.addAttribute("claveFactura", factura.getClave());
			model.addAttribute("consecutivoFactura", factura.getConsecutivo());
			model.addAttribute("factura", factura);
			return "/catalogos/fe-documentos/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/bitacora" })
	public String getBitacora(Model model, @RequestParam("idFactura") Long idFactura, HttpSession session) {
		List<FEBitacora> listaBitacora = null;
		if (idFactura.longValue() > 0L)
			listaBitacora = this._bitacoraService.findByIdFactura(idFactura);
		model.addAttribute("listaBitacora", listaBitacora);
		model.addAttribute("urlApiDownloadXml", this.urlApiDownloadXml);
		model.addAttribute("identificacionEmpresa", session.getAttribute("SESSION_IDENTIFICACION_EMPRESA"));
		model.addAttribute("urlApiImprimirPdf", this.urlApiImprimirPdf);
		return "/catalogos/fe-documentos/bitacora";
	}

	@PostMapping(value = { "/emitir-nd-nc" }, produces = { "application/json" })
	@ResponseBody
	public String emiteNdNc(HttpSession session, @RequestParam(name = "id") Long idDocumento,
			@RequestParam(name = "tipoDocumento") String tipoDocumento, @RequestParam(name = "razon") String razon) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			if (session.getAttribute("SESSION_SUCURSAL_ID") != null
					&& session.getAttribute("SESSION_TERMINAL_ID") != null) {
				Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Emisor emisor = this._emisorService.findById(empresaId);
				FEBitacora bitacora = this._bitacoraService.findById(idDocumento);
				String j = "{";
				j = j + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
				j = j + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
				j = j + "\"tipoDocumento\":\"" + tipoDocumento + "\",";
				j = j + "\"situacion\":\"normal\",";
				j = j + "\"sucursal\":\"" + session.getAttribute("SESSION_NUMERO_SUCURSAL").toString() + "\",";
				j = j + "\"terminal\":\"" + session.getAttribute("SESSION_NUMERO_TERMINAL").toString() + "\",";
				j = j + "\"numero\":\"" + bitacora.getClave() + "\",";
				j = j + "\"codigo\":\"01\",";
				j = j + "\"razon\":\"" + razon + "\"";
				j = j + "}";
				try {
					FEFactura factura = this._facturaService.findByEmisorById(empresaId, bitacora.getFactura().getId());
					CloseableHttpClient client = HttpClients.createDefault();
					HttpPost httpPost = new HttpPost(this.urlApiRecepcionNdNc);
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
						FEBitacora b = new FEBitacora();
						b.setTipoDocumento(tipoDocumento);
						b.setClave(data.path("clave").asText());
						b.setFechaEmision(data.path("fechaEmision").asText());
						b.setNameXmlEnviado(data.path("fileXmlSign").asText());
						b.setFactura(factura);
						b.setResponseCode(data.path("response").asInt());
						this._bitacoraService.save(b);
						if (tipoDocumento.equalsIgnoreCase("ND")) {
							for (FEFacturaItem f : factura.getItems())
								this._productoService.registroSalidasInvent(f.getCantidad(), f.getProducto().getId());
						} else if (tipoDocumento.equalsIgnoreCase("NC")) {
							for (FEFacturaItem f : factura.getItems())
								this._productoService.registroEntradasInvent(f.getCantidad(), f.getProducto().getId());
							this._cxcFacturasService.anularFacturaCXC("N", factura.getEmisor().getId(),
									factura.getNumeroFactura());
							this._facturaService.updateEstadoByIdFactura(empresaId, factura.getId());
						}
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

	@PostMapping(value = { "/anular-documento" }, produces = { "application/json" })
	public ResponseEntity<?> anularFactura(HttpSession session, @RequestParam(name = "id") Long idFactura) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				this._facturaService.updateEstadoByIdFactura(empresaId, idFactura);
				FEFactura f = this._facturaService.findByEmisorById(empresaId, idFactura);
				if (f != null) {
					this._cxcFacturasService.anularFacturaCXC("N", empresaId, f.getNumeroFactura());
					if (f.getTipoDocumento().equalsIgnoreCase("FE") || f.getTipoDocumento().equalsIgnoreCase("TE")
							|| f.getTipoDocumento().equalsIgnoreCase("FEE")
							|| f.getTipoDocumento().equalsIgnoreCase("NC")) {
						for (FEFacturaItem item : f.getItems())
							this._productoService.registroEntradasInvent(item.getCantidad(),
									item.getProducto().getId());
					} else if (f.getTipoDocumento().equalsIgnoreCase("ND")) {
						for (FEFacturaItem item : f.getItems())
							this._productoService.registroSalidasInvent(item.getCantidad(), item.getProducto().getId());
					}
				}
				response.put("response", "200");
			} else {
				response.put("response", "400");
			}
		} catch (Exception e) {
			response.put("response", "400");
		}
		return new ResponseEntity(response, HttpStatus.OK);
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
				List<FEBitacora> listaDocumentos = this._bitacoraService.findAllByEmisorId(empresaId);
				CloseableHttpClient client = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(this.urlApiConsultaDocs);
				StringEntity entity = null;
				CloseableHttpResponse response = null;
				HttpEntity entity2 = null;
				ObjectMapper objectMapper = null;
				JsonNode data = null;
				String json = "";
				for (FEBitacora b : listaDocumentos) {
					j = "{";
					j = j + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
					j = j + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
					j = j + "\"clave\":\"" + b.getClave() + "\"";
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
						this._bitacoraService.updateEstadoDocByClave(data.path("ind-estado").asText(),
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

	@PostMapping(value = { "/obtener-correo-cliente" }, produces = { "application/json" })
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEmailClient(Model model, HttpSession session,
			@RequestParam(name = "id") Long id) {
		Map<String, Object> response = new HashMap<>();
		if (session.getAttribute("SESSION_EMPRESA_ID") != null)
			try {
				Cliente cliente = this._clienteService.findByIdByUserId(id,
						Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString())), "C");
				response.put("correo", cliente.getCorreo1());
			} catch (Exception e) {
				response.put("correo", "");
			}
		response.put("response", Integer.valueOf(200));
		return new ResponseEntity(response, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = { "/reenviar-factura" }, produces = { "application/json" })
	@ResponseBody
	public String reenviarFactura(Model model, HttpSession session, @RequestParam(name = "correo") String correo,
			@RequestParam(name = "clave") String clave) {
		String resp = "";
		String j = "";
		if (session.getAttribute("SESSION_EMPRESA_ID") != null)
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(this.urlApiReenviarXmls);
				StringEntity entity = null;
				CloseableHttpResponse response = null;
				HttpEntity entity2 = null;
				String json = "";
				j = "{";
				j = j + "\"clave\":\"" + clave + "\",";
				j = j + "\"correo\":\"" + correo + "\"";
				j = j + "}";
				json = j;
				entity = new StringEntity(json, "UTF-8");
				httpPost.setEntity((HttpEntity) entity);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				response = client.execute((HttpUriRequest) httpPost);
				entity2 = response.getEntity();
				String responseString = EntityUtils.toString(entity2, "UTF-8");
				resp = responseString;
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
				j = j + "{";
				j = "\"response\":\"400\",";
				j = j + "}";
				resp = j;
			}
		return resp;
	}
}
