package com.samyx.controllers;

import com.samyx.models.entity.CTipoDeCambio;
import com.samyx.models.entity.Moneda;
import com.samyx.service.interfaces.ICTipoDeCambioService;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.util.PageRender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
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
@RequestMapping({ "/tipo-de-cambio" })
@SessionAttributes({ "objetoTipoDeCambio" })
public class TipoDeCambioController {
	@Autowired
	private ICTipoDeCambioService _tipoDeCambioService;

	@Autowired
	private IMonedaService _monedaService;

	@GetMapping({ "/" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<CTipoDeCambio> ListaTiposDeCambio = null;
			ListaTiposDeCambio = this._tipoDeCambioService.findAllByEmisorId((Pageable) pageRequest);
			PageRender<CTipoDeCambio> pageRender = new PageRender("/tipo-de-cambio/", ListaTiposDeCambio);
			model.addAttribute("ListaTiposDeCambio", ListaTiposDeCambio);
			model.addAttribute("page", pageRender);
			return "/catalogos/tipo-de-cambio/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/form" })
	public String form(Model model) {
		CTipoDeCambio objetoTipoDeCambio = new CTipoDeCambio();
		List<Moneda> listaMonedas = this._monedaService.findAll();
		model.addAttribute("objetoTipoDeCambio", objetoTipoDeCambio);
		model.addAttribute("listaMonedas", listaMonedas);
		return "/catalogos/tipo-de-cambio/form";
	}

	@GetMapping({ "/edit/{id}" })
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			CTipoDeCambio objetoTipoDeCambio = this._tipoDeCambioService.findById(id);
			List<Moneda> listaMonedas = this._monedaService.findAll();
			model.addAttribute("listaMonedas", listaMonedas);
			model.addAttribute("objetoTipoDeCambio", objetoTipoDeCambio);
			return "/catalogos/tipo-de-cambio/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/save" })
	public ResponseEntity<?> save(CTipoDeCambio objetoTipoDeCambio, Model model, Authentication authentication,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				objetoTipoDeCambio.setFecha(new Date());
				this._tipoDeCambioService.save(objetoTipoDeCambio);
				response.put("response", "1");
			} else {
				response.put("response", "0");
			}
		} catch (Exception e) {
			response.put("response", "2");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}
}
