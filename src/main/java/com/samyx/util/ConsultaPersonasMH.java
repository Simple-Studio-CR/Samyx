package com.samyx.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsultaPersonasMH {
	@RequestMapping(value = { "/consulta-personas-mh" }, method = { RequestMethod.GET }, produces = {
			"application/json" })
	public String consulta(@RequestParam(name = "identificacion", required = false) String identificacion)
			throws Exception {
		String response = "";
		String d = "";
		try {
			String urlConsulta = "https://api.hacienda.go.cr/fe/ae?identificacion=" + identificacion;
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet _j = new HttpGet(urlConsulta);
			CloseableHttpResponse responseApi = client.execute((HttpUriRequest) _j);
			HttpEntity entity2 = responseApi.getEntity();
			response = EntityUtils.toString(entity2, "UTF-8");
		} catch (Exception e) {
			d = d + "{";
			d = d + "\"code\": 400";
			d = d + "\"status\": Not found";
			d = d + "}";
			d = d + "}";
			response = d;
		}
		return response;
	}
}
