package com.samyx.controllers;

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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.samyx.models.entity.ComprobantesElectronicos;
import com.samyx.models.entity.Emisor;
import com.samyx.service.interfaces.IComprobantesElectronicosService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.util.PageRender;

@Controller
@SessionAttributes({ "comprobantesElectronico" })
public class ComprobantesElectronicosApiController {
	@Autowired
	private IComprobantesElectronicosService _comprobantesElectronicosService;

	@Autowired
	private IEmisorService _emisorService;

	@Value("${api.jmata.download.docs}")
	private String urlApiDownloadDocs;

	@Value("${api.jmata.imprimir.pdf}")
	private String urlApiImprimirPdf;

	@Value("${api.jmata.reenviar.xmls}")
	private String urlApiReenviarXmls;

	@Secured({ "ROLE_ADMIN" })
	@GetMapping({ "/comprobantes-electronicos" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		PageRequest pageRequest = PageRequest.of(page, 40);
		Page<ComprobantesElectronicos> listaComprobantesElectronicos = this._comprobantesElectronicosService
				.findAllComprobante(q.toUpperCase(), (Pageable) pageRequest);
		PageRender<ComprobantesElectronicos> pageRender = new PageRender("/comprobantes-electronicos",
				listaComprobantesElectronicos);
		model.addAttribute("listaComprobantesElectronicos", listaComprobantesElectronicos);
		model.addAttribute("page", pageRender);
		model.addAttribute("urlApiDownloadDocs", this.urlApiDownloadDocs);
		model.addAttribute("urlApiImprimirPdf", this.urlApiImprimirPdf);
		return "/catalogos/comprobantes-electronicos/index";
	}

	@GetMapping({ "/comprobantes-electronicos-enviados" })
	public String comprobantesUsuario(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Emisor emisor = this._emisorService.findById(emisorId);
			PageRequest pageRequest = PageRequest.of(page, 40);
			Page<ComprobantesElectronicos> listaComprobantesElectronicos = this._comprobantesElectronicosService
					.findAllComprobanteByEmisor(emisor.getIdentificacion(), q.toUpperCase(), (Pageable) pageRequest);
			PageRender<ComprobantesElectronicos> pageRender = new PageRender("/comprobantes-electronicos-enviados",
					listaComprobantesElectronicos);
			model.addAttribute("listaComprobantesElectronicos", listaComprobantesElectronicos);
			model.addAttribute("page", pageRender);
			model.addAttribute("urlApiDownloadDocs", this.urlApiDownloadDocs);
			model.addAttribute("urlApiImprimirPdf", this.urlApiImprimirPdf);
			return "/catalogos/comprobantes-electronicos/comprobantes-cliente";
		}
		return "redirect:/";
	}

	@Secured({ "ROLE_ADMIN" })
	@GetMapping({ "/comprobantes-electronicos/edit/{id}" })
	public String edit(Model model, @PathVariable("id") Long id) {
		ComprobantesElectronicos comprobantesElectronico = this._comprobantesElectronicosService.findById(id);
		model.addAttribute("comprobantesElectronico", comprobantesElectronico);
		return "/catalogos/comprobantes-electronicos/form";
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping({ "/comprobantes-electronicos/form/save" })
	public String save(Model model, ComprobantesElectronicos ce, BindingResult result, SessionStatus status) {
		this._comprobantesElectronicosService.UpdateComprobantesElectronicosApi(ce.getResponseCodeSend(),
				ce.getIndEstado(), ce.getEmailDistribucion(), ce.getReconsultas(), ce.getId());
		status.setComplete();
		return "redirect:/comprobantes-electronicos";
	}

	@PostMapping(value = { "/comprobantes-electronicos/reenviar-factura" }, produces = { "application/json" })
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
