package com.samyx.controllers.historicos;

import com.samyx.models.entity.HistoricoOtrosSistemas;
import com.samyx.service.interfaces.IHistoricoOtrosSistemasService;
import com.samyx.util.PageRender;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping({ "/historicos" })
@SessionAttributes({ "cliente" })
public class HistoricoOtrosSistemasController {
	@Autowired
	private IHistoricoOtrosSistemasService _historicoService;

	@GetMapping({ "/" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 25);
			Page<HistoricoOtrosSistemas> ListaDocumentos = null;
			ListaDocumentos = this._historicoService.findAllByIdEmisorAndId(emisorId, q.toUpperCase(),
					(Pageable) pageRequest);
			PageRender<HistoricoOtrosSistemas> pageRender = new PageRender("/historicos/", ListaDocumentos);
			model.addAttribute("ListaDocumentos", ListaDocumentos);
			model.addAttribute("page", pageRender);
			return "/catalogos/historicos-otros-sistemas/index";
		}
		return "redirect:/";
	}
}
