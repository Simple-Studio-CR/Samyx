package com.samyx.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samyx.models.entity.Emisor;
import com.samyx.service.interfaces.IEmisorService;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Controller
public class ConsultarComprobantesController {
	@Value("${api.jmata.consulta.cualquier.doc}")
	private String apiJmataConsultaCualquierDoc;

	@Autowired
	private IEmisorService _emisorService;

	@GetMapping({ "/consulta-comprobantes-manualmente/" })
	public String home(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null)
			return "/consulta-comprobantes/index";
		return "redirect:/";
	}

	@PostMapping(value = { "/consulta-comprobantes-manualmente/" }, produces = { "application/json" })
	@ResponseBody
	public String buscarProductos(@RequestBody @RequestParam("clave") String clave, HttpSession session)
			throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		String r = "";
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Emisor emisor = this._emisorService.findById(empresaId);
			if (emisor != null) {
				String j = "";
				j = j + "{";
				j = j + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
				j = j + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
				j = j + "\"clave\":\"" + clave.trim() + "\"";
				j = j + "}";
				CloseableHttpClient client = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(this.apiJmataConsultaCualquierDoc);
				try {
					String json = j;
					StringEntity entity = new StringEntity(json, "UTF-8");
					httpPost.setEntity((HttpEntity) entity);
					httpPost.setHeader("Accept", "application/json");
					httpPost.setHeader("Content-type", "application/json");
					CloseableHttpResponse response = client.execute((HttpUriRequest) httpPost);
					HttpEntity entity2 = response.getEntity();
					String responseMh = EntityUtils.toString(entity2, "UTF-8");
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode data = objectMapper.readTree(responseMh);
					DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					InputSource is = new InputSource();
					is.setCharacterStream(new StringReader(
							new String(Base64.decodeBase64(data.path("respuesta-xml").asText()), "UTF-8")));
					Document doc = db.parse(is);
					NodeList nodes = doc.getElementsByTagName("MensajeHacienda");
					String mensajeMh = "";
					for (int i = 0; i < nodes.getLength(); i++) {
						Element element = (Element) nodes.item(i);
						NodeList name = element.getElementsByTagName("DetalleMensaje");
						Element line = (Element) name.item(0);
						mensajeMh = StringEscapeUtils.escapeJava(getCharacterDataFromElement(line));
					}
					r = r + "{";
					r = r + "\"response\":\"200\",";
					r = r + "\"clave\":\"" + data.path("clave").asText() + "\",";
					r = r + "\"fechaEmision\":\"" + data.path("fecha").asText() + "\",";
					r = r + "\"estadoDocumento\":\"" + data.path("ind-estado").asText() + "\",";
					r = r + "\"respuestaXml\":\"" + mensajeMh + "\"";
					r = r + "}";
				} catch (Exception e) {
					r = r + "{";
					r = r + "\"response\":\"400\",";
					r = r + "\"clave\":\"\",";
					r = r + "\"fechaEmision\":\"\",";
					r = r + "\"estadoDocumento\":\"no enviado\",";
					r = r + "\"respuestaXml\":\"Al parecer este comprobante no ha sido enviado al Ministerio de Hacienda\"";
					r = r + "}";
				}
			}
		} else {
			return null;
		}
		return r;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}
}
