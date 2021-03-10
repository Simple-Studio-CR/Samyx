package com.samyx.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.samyx.indicadores.economicos.TipoCambio;

@Controller
public class TipoCambioController {
	@PostMapping(value = { "/tipo-cambio-dolar" }, produces = { "application/json" })
	public ResponseEntity<?> tipoCambioDolar(@RequestParam("tc") Integer tc) {
		Map<String, Object> resp = new HashMap<>();
		try {
			resp.put("response", "202");
			if (tc.intValue() == 2) {
				TipoCambio servicioTipoCambio = new TipoCambio();
				resp.put("compra", Double.valueOf(servicioTipoCambio.getCompra()));
				resp.put("venta", Double.valueOf(servicioTipoCambio.getVenta()));
			} else {
				resp.put("compra", "1.00");
				resp.put("venta", "1.00");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.put("response", "400");
			resp.put("compra", Integer.valueOf(0));
			resp.put("venta", Integer.valueOf(0));
		}
		return new ResponseEntity(resp, HttpStatus.CREATED);
	}
}
