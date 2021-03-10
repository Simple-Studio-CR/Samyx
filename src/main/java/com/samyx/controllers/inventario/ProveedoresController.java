package com.samyx.controllers.inventario;

import com.samyx.models.entity.CBarrio;
import com.samyx.models.entity.CCanton;
import com.samyx.models.entity.CDistrito;
import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICBarrioService;
import com.samyx.service.interfaces.ICCantonService;
import com.samyx.service.interfaces.ICDistritoService;
import com.samyx.service.interfaces.ICProvinciaService;
import com.samyx.service.interfaces.ICTipoDeIdentificacionService;
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
@RequestMapping({ "/inventario" })
@SessionAttributes({ "proveedores" })
public class ProveedoresController {
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
	private IEmisorService _emisorService;

	@GetMapping({ "/proveedores" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<Cliente> ListaProveedores = null;
			ListaProveedores = this._clienteService.findAllByIdUserId(emisorId, "P", q.toUpperCase(),
					(Pageable) pageRequest);
			PageRender<Cliente> pageRender = new PageRender("/inventario/proveedores/", ListaProveedores);
			model.addAttribute("ListaProveedores", ListaProveedores);
			model.addAttribute("page", pageRender);
			return "/catalogos/productos/inventario/proveedores/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/proveedores/form" })
	public String form(Model model) {
		Cliente cliente = new Cliente();
		List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
		List<CProvincia> listaProvincias = this._provinciaService.findAll();
		model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
		model.addAttribute("listaProvincias", listaProvincias);
		model.addAttribute("proveedores", cliente);
		return "/catalogos/productos/inventario/proveedores/form";
	}

	@GetMapping({ "/proveedores/edit/{id}" })
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Cliente proveedores = this._clienteService.findByIdByUserId(id, emisorId, "P");
			List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
			List<CProvincia> listaProvincias = this._provinciaService.findAll();
			List<CCanton> listaCantones = this._cantonService.findAllByIdProvincia(
					Long.valueOf((proveedores.getProvincia() != null) ? proveedores.getProvincia().getId().longValue()
							: Long.parseLong("0")));
			List<CDistrito> listaDistritos = this._distritoService.findAllByIdCanton(
					Long.valueOf((proveedores.getCanton() != null) ? proveedores.getCanton().getId().longValue()
							: Long.parseLong("0")));
			List<CBarrio> listaBarrios = this._barrioService.findAllByIdDistrito(
					Long.valueOf((proveedores.getDistrito() != null) ? proveedores.getDistrito().getId().longValue()
							: Long.parseLong("0")));
			model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
			model.addAttribute("listaProvincias", listaProvincias);
			model.addAttribute("listaCantones", listaCantones);
			model.addAttribute("listaDistritos", listaDistritos);
			model.addAttribute("listaBarrios", listaBarrios);
			model.addAttribute("proveedores", proveedores);
			return "/catalogos/productos/inventario/proveedores/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/proveedores/form/save" })
	public ResponseEntity<?> save(Cliente proveedores, Model model, Authentication authentication,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
				Emisor emisor = this._emisorService.findById(emisorId);
				proveedores.setUsuario(UsuarioId);
				proveedores.setEmisor(emisor);
				proveedores.setTipoCatalogo("P");
				if (proveedores.getCodigoPais() == null)
					proveedores.setCodigoPais(Integer.valueOf(506));
				this._clienteService.save(proveedores);
				response.put("response", "1");
			} else {
				response.put("response", "0");
			}
		} catch (Exception e) {
			response.put("response", "2");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping({ "/proveedores/delete" })
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
