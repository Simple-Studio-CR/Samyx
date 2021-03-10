package com.samyx.controllers;

import com.samyx.models.entity.CSucursal;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICSucursalService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IUsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping({ "/sucursales" })
@SessionAttributes({ "sucursal" })
public class SucursalController {
	@Autowired
	private ICSucursalService _sucursalService;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private IEmisorService _emisorService;

	@PostMapping({ "/" })
	public String Home(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long sucursalId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<CSucursal> listaSucursales = this._sucursalService.findAllByEmisorId(sucursalId);
			model.addAttribute("listaSucursales", listaSucursales);
			return "/catalogos/sucursales/index";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/edit" })
	public String Form(Model model, HttpSession session, @RequestParam(value = "id", required = false) Long id) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			CSucursal sucursal = new CSucursal();
			if (id.longValue() > 0L) {
				sucursal = this._sucursalService.findById(id);
				model.addAttribute("statusInput", Integer.valueOf(1));
			} else {
				model.addAttribute("statusInput", Integer.valueOf(0));
			}
			model.addAttribute("sucursal", sucursal);
			return "/catalogos/sucursales/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/save" })
	public ResponseEntity<?> save(CSucursal sucursal, Model model, Authentication authentication, HttpSession session,
			SessionStatus status) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
				Emisor emisor = this._emisorService.findById(emisorId);
				sucursal.setUsuario(UsuarioId);
				sucursal.setEmisor(emisor);
				this._sucursalService.save(sucursal);
				status.setComplete();
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
