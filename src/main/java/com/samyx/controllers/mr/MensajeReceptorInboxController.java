package com.samyx.controllers.mr;

import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.FEMensajeReceptor;
import com.samyx.models.entity.FEMensajeReceptorAutomatico;
import com.samyx.service.impl.FuncionesService;
import com.samyx.service.interfaces.IEmisorActividadesService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IFEMensajeReceptorAutomaticoService;
import com.samyx.util.PageRender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping({"/mensaje-receptor-inbox"})
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class MensajeReceptorInboxController {
	  @Autowired
	  private IEmisorService _emisorService;
	  
	  @Autowired
	  private FuncionesService _funcionesService;
	  
	  @Value("${path.upload.files.api}")
	  private String pathUploadFilesApi;
	  
	  @Autowired
	  public IEmisorActividadesService _actividadesService;
	  
	  @Autowired
	  private IFEMensajeReceptorAutomaticoService _mrinboxService;
	  
	  public static final String UTF8_BOM = "";
	  
	  @GetMapping({"/"})
	  public String Home(Model model, @RequestParam(name = "page", defaultValue = "0") int page, HttpSession session, @RequestParam(name = "e", defaultValue = "P") String e, @RequestParam(name = "q", defaultValue = "") String q) {
	    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
	      Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
	      Emisor emisor = this._emisorService.findById(emisorId);
	      String tipoIdentificacion = this._funcionesService.str_pad(emisor.getTipoDeIdentificacion().getId().toString(), 2, "0", "STR_PAD_LEFT");
	      PageRequest pageRequest = PageRequest.of(page, 25);
	      Page<FEMensajeReceptorAutomatico> listaDocumentos = this._mrinboxService.findAllByReceptor(tipoIdentificacion, emisor.getIdentificacion(), e, q, (Pageable)pageRequest);
	      PageRender<FEMensajeReceptorAutomatico> pageRender = new PageRender("/mensaje-receptor-inbox/", listaDocumentos);
	      model.addAttribute("listaDocumentos", listaDocumentos);
	      model.addAttribute("page", pageRender);
	      model.addAttribute("BASE_URL", this.pathUploadFilesApi + "/mr-automatico/");
	      return "/mensaje-receptor/mr-inbox/index";
	    } 
	    return "redirect:/";
	  }
	  
	  @GetMapping({"/form/{id}"})
	  public String Form(Model model, HttpSession session, @PathVariable("id") Long id) {
	    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
	      if (session.getAttribute("SESSION_SUCURSAL_ID") != null && session.getAttribute("SESSION_TERMINAL_ID") != null && id.longValue() > 0L) {
	        FEMensajeReceptor mensajeReceptor = new FEMensajeReceptor();
	        FEMensajeReceptorAutomatico mensajeReceptorInbox = this._mrinboxService.findById(id);
	        Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
	        List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
	        String filePath = this.pathUploadFilesApi + "mr-automatico/" + mensajeReceptorInbox.getFacturaXml();
	        String facturaXmlFinal = "";
	        try {
	          facturaXmlFinal = new String(Files.readAllBytes(Paths.get(filePath, new String[0])));
	        } catch (IOException e) {
	          e.printStackTrace();
	          facturaXmlFinal = "";
	        } 
	        model.addAttribute("idMrInbox", mensajeReceptorInbox.getId());
	        model.addAttribute("facturaXmlFinal", removeUTF8BOM(facturaXmlFinal));
	        model.addAttribute("mensajeReceptor", mensajeReceptor);
	        model.addAttribute("listaActividades", listaActividades);
	        return "/mensaje-receptor/mr-inbox/form";
	      } 
	      return "redirect:/proformas/seleccionar-terminal?urlRetorno=/mensaje-receptor-inbox/";
	    } 
	    return "redirect:/";
	  }
	  
	  private static String removeUTF8BOM(String s) {
	    if (s.startsWith(""))
	      s = s.substring(1); 
	    return s;
	  }
	}
