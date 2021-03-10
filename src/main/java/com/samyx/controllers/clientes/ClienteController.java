package com.samyx.controllers.clientes;

import com.samyx.models.entity.CBarrio;
import com.samyx.models.entity.CCanton;
import com.samyx.models.entity.CDistrito;
import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.CTipoDocumentoExoneracionOAutorizacion;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICBarrioService;
import com.samyx.service.interfaces.ICCantonService;
import com.samyx.service.interfaces.ICDistritoService;
import com.samyx.service.interfaces.ICProvinciaService;
import com.samyx.service.interfaces.ICTipoDeIdentificacionService;
import com.samyx.service.interfaces.ICTipoDocumentoExoneracionOAutorizacionService;
import com.samyx.service.interfaces.IClienteService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({ "/clientes" })
@Secured({ "ROLE_ADMIN", "ROLE_USER", "ROLE_USER_COBRADOR", "ROLE_CLIENTES" })
public class ClienteController {
	@Autowired
	private IClienteService _clienteService;

	@Autowired
	private ICProvinciaService _provinciaService;

	@Autowired
	private ICCantonService _cantonService;

	@Autowired
	private ICDistritoService _distritoService;

	@Autowired
	private ICBarrioService _barrioService;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private ICTipoDeIdentificacionService _tipoDeIdentificacionService;

	@Autowired
	private ICTipoDocumentoExoneracionOAutorizacionService _exoneraTipoDocService;

	@Autowired
	private IEmisorService _emisorService;

	@GetMapping({ "/" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<Cliente> ListaClientes = null;
			ListaClientes = this._clienteService.findAllByIdUserId(emisorId, "C", q.toUpperCase(),
					(Pageable) pageRequest);
			PageRender<Cliente> pageRender = new PageRender("/clientes/", ListaClientes);
			model.addAttribute("ListaClientes", ListaClientes);
			model.addAttribute("page", pageRender);
			return "/catalogos/clientes/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/form" })
	public String form(Model model) {
		Cliente cliente = new Cliente();
		List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
		List<CProvincia> listaProvincias = this._provinciaService.findAll();
		List<CTipoDocumentoExoneracionOAutorizacion> listaTipoDocsExonera = this._exoneraTipoDocService.findAll();
		model.addAttribute("listaTipoDocsExonera", listaTipoDocsExonera);
		model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
		model.addAttribute("listaProvincias", listaProvincias);
		model.addAttribute("cliente", cliente);
		return "/catalogos/clientes/form";
	}

	@GetMapping({ "/edit/{id}" })
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Cliente cliente = this._clienteService.findByIdByUserId(id, emisorId, "C");
			List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
			List<CProvincia> listaProvincias = this._provinciaService.findAll();
			List<CCanton> listaCantones = this._cantonService.findAllByIdProvincia(
					Long.valueOf((cliente.getProvincia() != null) ? cliente.getProvincia().getId().longValue()
							: Long.parseLong("0")));
			List<CDistrito> listaDistritos = this._distritoService.findAllByIdCanton(Long.valueOf(
					(cliente.getCanton() != null) ? cliente.getCanton().getId().longValue() : Long.parseLong("0")));
			List<CBarrio> listaBarrios = this._barrioService.findAllByIdDistrito(Long.valueOf(
					(cliente.getDistrito() != null) ? cliente.getDistrito().getId().longValue() : Long.parseLong("0")));
			List<CTipoDocumentoExoneracionOAutorizacion> listaTipoDocsExonera = this._exoneraTipoDocService.findAll();
			model.addAttribute("listaTipoDocsExonera", listaTipoDocsExonera);
			model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
			model.addAttribute("listaProvincias", listaProvincias);
			model.addAttribute("listaCantones", listaCantones);
			model.addAttribute("listaDistritos", listaDistritos);
			model.addAttribute("listaBarrios", listaBarrios);
			model.addAttribute("cliente", cliente);
			return "/catalogos/clientes/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/save" })
	public ResponseEntity<?> save(Cliente cliente, Model model, Authentication authentication, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
				Emisor emisor = this._emisorService.findById(emisorId);
				if (cliente.getCodigoPais() == null)
					cliente.setCodigoPais(Integer.valueOf(506));
				cliente.setTipoCatalogo("C");
				cliente.setUsuario(UsuarioId);
				cliente.setEmisor(emisor);
				this._clienteService.save(cliente);
				response.put("response", "1");
			} else {
				response.put("response", "0");
			}
		} catch (Exception e) {
			response.put("response", "2");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping({ "/delete" })
	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	public ResponseEntity<?> deleteProducto(@RequestParam(name = "id", required = false) Long id, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				this._clienteService.deleteByIdByUserIdAndIdEmisor(id, emisorId);
				response.put("response", Integer.valueOf(1));
				response.put("msj", "Registro eliminado con éxito");
			} else {
				response.put("response", Integer.valueOf(2));
				response.put("msj", "No existe la empresa.");
			}
		} catch (Exception e) {
			response.put("response", Integer.valueOf(2));
			response.put("msj", "Este registro tiene facturas ligadas, no puede eliminarlo!!!");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}
}
