package com.samyx.controllers;

import com.samyx.models.entity.CSucursal;
import com.samyx.models.entity.CTerminal;
import com.samyx.service.interfaces.ICSucursalService;
import com.samyx.service.interfaces.ICTerminalService;
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
@RequestMapping({ "/terminales" })
@SessionAttributes({ "terminal" })
public class TerminalController {
	@Autowired
	private ICTerminalService _terminalService;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private ICSucursalService _sucursalService;

	@PostMapping({ "/" })
	public String Home(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<CTerminal> listaTerminales = this._terminalService.findAllTerminalBySucursalByEmisor(emisorId);
			model.addAttribute("listaTerminales", listaTerminales);
			return "/catalogos/terminales/index";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/edit" })
	public String Form(Model model, HttpSession session, @RequestParam(value = "id", required = false) Long id) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<CSucursal> listaSucursales = this._sucursalService.findAllByEmisorId(emisorId);
			CTerminal terminal = new CTerminal();
			if (id.longValue() > 0L) {
				terminal = this._terminalService.findById(id);
				model.addAttribute("statusInput", Integer.valueOf(1));
			} else {
				model.addAttribute("statusInput", Integer.valueOf(0));
			}
			model.addAttribute("listaSucursales", listaSucursales);
			model.addAttribute("terminal", terminal);
			return "/catalogos/terminales/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/save" })
	public ResponseEntity<?> save(CTerminal terminal, Model model, Authentication authentication, HttpSession session,
			SessionStatus status) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				this._terminalService.save(terminal);
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
