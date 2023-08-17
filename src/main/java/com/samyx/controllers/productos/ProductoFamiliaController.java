package com.samyx.controllers.productos;

import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.ProductoFamilia;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IProductoFamiliaService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "/producto-familia" })
public class ProductoFamiliaController {
	@Autowired
	private IProductoFamiliaService _familiasService;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private IEmisorService _emisorService;

	@GetMapping({ "/" })
	public String home(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<ProductoFamilia> listaProductoFamilia = this._familiasService.findAllByEmisorId(emisorId);
			model.addAttribute("listaProductoFamilia", listaProductoFamilia);
			return "/catalogos/productos/familia/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/form" })
	public String form(Model model) {
		ProductoFamilia productoFamilia = new ProductoFamilia();
		model.addAttribute("productoFamilia", productoFamilia);
		return "/catalogos/productos/familia/form";
	}

	@GetMapping({ "/edit/{id}" })
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session, Authentication authentication) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			ProductoFamilia productoFamilia = this._familiasService.findByIdAndEmisorId(emisorId, id);
			model.addAttribute("productoFamilia", productoFamilia);
			return "/catalogos/productos/familia/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/save" })
	public ResponseEntity<?> save(ProductoFamilia productoFamilia, Model model, Authentication authentication,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
				Emisor emisor = this._emisorService.findById(emisorId);
				productoFamilia.setUsuario(UsuarioId);
				productoFamilia.setEmisor(emisor);
				this._familiasService.save(productoFamilia);
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
