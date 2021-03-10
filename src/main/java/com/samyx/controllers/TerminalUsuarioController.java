package com.samyx.controllers;

import com.samyx.models.entity.CSucursal;
import com.samyx.models.entity.CTerminalUsuario;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICSucursalService;
import com.samyx.service.interfaces.ICTerminalService;
import com.samyx.service.interfaces.ICTerminalUsuarioService;
import com.samyx.service.interfaces.IEmisorActividadesService;
import com.samyx.service.interfaces.IEmpresaUsuarioAccesoService;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.service.interfaces.IUsuarioService;

import java.util.Date;
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

@Controller
@RequestMapping({ "/terminales/acceso" })
public class TerminalUsuarioController {
	@Autowired
	private ICTerminalUsuarioService _terminalUsuarioService;

	@Autowired
	private ICSucursalService _sucursalService;

	@Autowired
	private ICTerminalService _terminalService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IEmpresaUsuarioAccesoService _empresaUsuarioAccesoService;

	@Autowired
	public IEmisorActividadesService _actividadesService;

	@Autowired
	private IMonedaService _monedaService;

	@PostMapping({ "/" })
	public String Home(Model model, @RequestParam(value = "id", required = true) Long sucursalId, HttpSession session) {
		try {
			List<CTerminalUsuario> listaTerminalUsuario = this._terminalUsuarioService.findAllBySucursal(sucursalId);
			model.addAttribute("listaTerminalUsuario", listaTerminalUsuario);
			model.addAttribute("listaMonedas", this._monedaService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/catalogos/terminales/usuarios/index";
	}

	@PostMapping({ "/form-nuevo-acceso" })
	public String FormNuevoAcceso(Model model, HttpSession session, Authentication auth) {
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				List<CSucursal> listaSucursales = this._sucursalService.findAllByEmisorId(emisorId);
				List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
				CTerminalUsuario terminalUsuario = new CTerminalUsuario();
				model.addAttribute("listaSucursales", listaSucursales);
				model.addAttribute("terminalUsuario", terminalUsuario);
				model.addAttribute("listaActividades", listaActividades);
				model.addAttribute("listaMonedas", this._monedaService.findAll());
				Usuario usuario = this.usuarioService.findByUsername(auth.getName());
				if (usuario.getId().longValue() == 1L) {
					model.addAttribute("listUsuarios",
							this._empresaUsuarioAccesoService.findUsuariosByEmpresaAdmin(emisorId));
				} else {
					model.addAttribute("listUsuarios",
							this._empresaUsuarioAccesoService.findUsuariosByEmpresa(emisorId));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/catalogos/terminales/usuarios/formNuevoAcceso";
	}

	@PostMapping({ "/terminales-por-sucursal" })
	public String FormSucursalByTerminal(Model model, @RequestParam("id") Long id) {
		model.addAttribute("listaTerminales", this._terminalService.findAllTerminalBySucursal(id));
		return "/catalogos/terminales/usuarios/formSucursalByTerminal";
	}

	@PostMapping({ "/form-save-nuevo-acceso" })
	public ResponseEntity<?> FormSaveNuevoAcceso(Model model, CTerminalUsuario terminalUsuario,
			@RequestParam(name = "sucursal", required = false) Long sucursal) {
		Map<String, Object> response = new HashMap<>();
		try {
			terminalUsuario.setFechaCreacion(new Date());
			this._terminalUsuarioService.save(terminalUsuario);
			response.put("resp", Integer.valueOf(1));
			response.put("sucursal", sucursal);
		} catch (Exception e) {
			response.put("resp", Integer.valueOf(2));
			response.put("sucursal", Integer.valueOf(0));
			e.printStackTrace();
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping({ "/delete-acceso-usuario" })
	public ResponseEntity<?> FormSaveNuevoAcceso(@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "s", required = false) Long s) {
		Map<String, Object> response = new HashMap<>();
		try {
			this._terminalUsuarioService.deleteAccesoUsuarioById(id);
			response.put("resp", Integer.valueOf(1));
			response.put("sucursal", s);
		} catch (Exception e) {
			response.put("resp", Integer.valueOf(2));
			response.put("sucursal", s);
			e.printStackTrace();
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}
}
