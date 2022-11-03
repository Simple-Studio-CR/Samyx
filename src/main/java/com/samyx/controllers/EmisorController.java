package com.samyx.controllers;

import com.samyx.models.entity.CActividadEconomica;
import com.samyx.models.entity.CBarrio;
import com.samyx.models.entity.CCanton;
import com.samyx.models.entity.CDistrito;
import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CSucursal;
import com.samyx.models.entity.CTerminalUsuario;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.EmpresaUsuarioAcceso;
import com.samyx.models.entity.Planes;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICActividadEconomicaService;
import com.samyx.service.interfaces.ICBarrioService;
import com.samyx.service.interfaces.ICCantonService;
import com.samyx.service.interfaces.ICDistritoService;
import com.samyx.service.interfaces.ICProvinciaService;
import com.samyx.service.interfaces.ICSucursalService;
import com.samyx.service.interfaces.ICTerminalUsuarioService;
import com.samyx.service.interfaces.IEmisorActividadesService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IEmpresaUsuarioAccesoService;
import com.samyx.service.interfaces.IPlanesService;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping({ "/emisor" })
@SessionAttributes({ "emisor" })
public class EmisorController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IPlanesService _planesService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private IEmisorService _emisorService;

	@Autowired
	private IEmpresaUsuarioAccesoService _empresaUsuarioService;

	@Autowired
	private ICProvinciaService _provinciaService;

	@Autowired
	private ICCantonService _cantonService;

	@Autowired
	private ICDistritoService _distritoService;

	@Autowired
	private ICBarrioService _barrioService;

	@Autowired
	private ICSucursalService _sucursalService;

	@Autowired
	private IEmpresaUsuarioAccesoService _empresaUsuarioAccesoService;

	@Autowired
	private ICTerminalUsuarioService _terminalUsuarioService;

	@Autowired
	private ICActividadEconomicaService _actividadService;

	@Autowired
	private IEmisorActividadesService _emisorActividad;

	@Value("${path.upload.files.api}")
	private String pathUploadFilesApi;

	@GetMapping({ "/" })
	public Page<Emisor> Home(HttpServletRequest request, Model model, Authentication authentication,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		session.removeAttribute("SESSION_EMPRESA_ID");
		session.removeAttribute("SESSION_DATOS_EMPRESA_NOMBRE");
		session.removeAttribute("SESSION_SUCURSAL_ID");
		session.removeAttribute("SESSION_CONTROL_CAJAS");
		model.addAttribute("CONTROL_CAJAS", "N");
		Usuario usuario = this._usuarioService.findByUsername(authentication.getName());
		PageRequest pageRequest = PageRequest.of(page, 15);
		Page<EmpresaUsuarioAcceso> listaEmisor = this._empresaUsuarioService.findByAllEmisoresByUserId(usuario.getId(),
				q.toUpperCase(), (Pageable) pageRequest);
		PageRender<EmpresaUsuarioAcceso> pageRender = new PageRender("/emisor/", listaEmisor);
		model.addAttribute("page", pageRender);
		model.addAttribute("listaEmisor", listaEmisor);
		if (request.isUserInRole("ROLE_SUPER_ADMIN"))
			return (Page<Emisor>) ResponseEntity.ok().body(model + "SUPER_ADMIN");
		return (Page<Emisor>) ResponseEntity.ok().body(model);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping({ "/edit/{id}" })
	public String Edit(Model model, @PathVariable("id") Long id, Authentication auth, HttpSession session)
			throws ParseException {
		Emisor emisor = null;
		Usuario usuario = this._usuarioService.findByUsername(auth.getName());
		EmpresaUsuarioAcceso eua = this._empresaUsuarioService.findEmpresaByEmisorAndUsuarioById(id, usuario.getId());
		if (eua != null) {
			if (id.longValue() > 0L) {
				session.setAttribute("SESSION_EMPRESA_ID", id);
				emisor = this._emisorService.findById(id);
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				Date today = sdf.parse(sdf.format(new Date()));
				if (emisor.getFechaCaducidadCert() != null) {
					int res = emisor.getFechaCaducidadCert().compareTo(today);
					if (res > 0) {
						this.log.info("Puede seguir facturando tranquilo" + res);
					} else {
						this.log.info("Debe actualizar la llave criptográfica para continuar facturando" + res);
						model.addAttribute("cambiarLlave", "S");
					}
				}
				session.setAttribute("SESSION_DATOS_EMPRESA_IDENTIFICACION", emisor.getIdentificacion());
				session.setAttribute("SESSION_DATOS_EMPRESA_NOMBRE", emisor.getNombreRazonSocial());
				session.setAttribute("SESSION_LOGO_EMPRESA", emisor.getLogoEmpresa());
				session.setAttribute("SESSION_CONTROL_CAJAS", emisor.getControlCajas());
				session.setAttribute("SESSION_CONTROL_IMPRESION", emisor.getImpresion());
				session.setAttribute("SESSION_DECIMALES", emisor.getDecimales());
				session.setAttribute("SESSION_APLICA_DEVOLUCION_IVA", emisor.getDevolucionIva());
				session.setAttribute("SESSION_SISTEMA_FARMACIA", emisor.getSistemaFarmacias());
				session.setAttribute("SESSION_TIPO_CLIENTE", emisor.getTipoCliente());
				model.addAttribute("emisor", emisor);
				List<CTerminalUsuario> sucursales = this._terminalUsuarioService
						.findAllByEmisorAndSucursal(emisor.getId(), usuario.getId());
				if (sucursales.size() == 1) {
					Long sucursal = null;
					Long terminal = null;
					for (CTerminalUsuario j : sucursales)
						sucursal = j.getTerminal().getSucursal().getId();
					List<CTerminalUsuario> terminales = this._terminalUsuarioService
							.findAllBySucursalByUsuario(sucursal, usuario.getId());
					if (terminales.size() == 1) {
						for (CTerminalUsuario j : terminales)
							terminal = j.getTerminal().getId();
						CTerminalUsuario tu = this._terminalUsuarioService
								.findSucursalTerminalBySucursalByTerminal(sucursal, terminal, usuario.getId());
						if (tu != null) {
							session.setAttribute("SESSION_SUCURSAL_ID", sucursal);
							session.setAttribute("SESSION_TERMINAL_ID", terminal);
							session.setAttribute("SESSION_NUMERO_SUCURSAL",
									Integer.valueOf(tu.getTerminal().getSucursal().getSucursal()));
							session.setAttribute("SESSION_NUMERO_TERMINAL",
									Integer.valueOf(tu.getTerminal().getTerminal()));
							session.setAttribute("SESSION_SUCURSAL",
									tu.getTerminal().getSucursal().getNombreSucursal());
							session.setAttribute("SESSION_TERMINAL", tu.getTerminal().getNombreTerminal());
							session.setAttribute("SESSION_ACTIVIDAD_DEFAULT", tu.getActividadEconomica());
							session.setAttribute("SESSION_PARAM_TIPO_DOC", tu.getTipoDeDocumento());
							session.setAttribute("SESSION_PARAM_INCLUIR_OMITIR_RECEPTOR",
									tu.getIncluirOmitirReceptor());
							session.setAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO", tu.getTipoBusquedaProducto());
							session.setAttribute("SESSION_PARAM_MODIFICA_PRECIO_FACTURACION",
									tu.getModificaPrecioFacturacion());
							session.setAttribute("SESSION_PARAM_APLICA_DESCUENTO", tu.getAplicaDescuentoFacturacion());
							session.setAttribute("SESSION_PARAM_MONTO_MAX_DESCUENTO", tu.getDescuentoFacturacion());
							session.setAttribute("SESSION_PARAM_MONEDA_USUARIO", tu.getMoneda().getId());
						}
					}
				}
				session.setAttribute("SESSION_IDENTIFICACION_EMPRESA", emisor.getIdentificacion());
				List<Planes> listaPlanes = this._planesService.findAll();
				List<CProvincia> listaProvincias = this._provinciaService.findAll();
				List<CCanton> listaCantones = this._cantonService.findAllByIdProvincia(
						Long.valueOf((emisor.getProvincia() != null) ? emisor.getProvincia().getId().longValue()
								: Long.parseLong("0")));
				List<CDistrito> listaDistritos = this._distritoService.findAllByIdCanton(Long.valueOf(
						(emisor.getCanton() != null) ? emisor.getCanton().getId().longValue() : Long.parseLong("0")));
				List<CBarrio> listaBarrios = this._barrioService.findAllByIdDistrito(
						Long.valueOf((emisor.getDistrito() != null) ? emisor.getDistrito().getId().longValue()
								: Long.parseLong("0")));
				List<CActividadEconomica> listaActividades = this._actividadService.findAll();
				List<CSucursal> listaSucursales = this._sucursalService.findAllByEmisorId(id);
				model.addAttribute("listaPlanes", listaPlanes);
				model.addAttribute("listaProvincias", listaProvincias);
				model.addAttribute("listaCantones", listaCantones);
				model.addAttribute("listaDistritos", listaDistritos);
				model.addAttribute("listaBarrios", listaBarrios);
				model.addAttribute("listaSucursales", listaSucursales);
				model.addAttribute("listaActividades", listaActividades);
				model.addAttribute("BASE_URL_LOGO", this.pathUploadFilesApi + "/logo/");
				return "/emisor/form";
			}
			return "redirect:/emisor/";
		}
		return "redirect:/login";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping({ "/actividades" })
	public String getActividad(Model model, Authentication auth, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<EmisorActividades> actividadesEmisor = this._emisorActividad.findAllByEmisorId(emisorId);
			model.addAttribute("actividadesEmisor", actividadesEmisor);
			return "/emisor/actividades-economicas/index";
		}
		return "";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping({ "/actividades/save" })
	public ResponseEntity<?> saveActividad(EmisorActividades emisorActividades, Model model, Authentication auth,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			try {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Emisor emisor = this._emisorService.findById(emisorId);
				emisorActividades.setEmisor(emisor);
				emisorActividades.setEstado("A");
				this._emisorActividad.save(emisorActividades);
				response.put("response", Integer.valueOf(200));
			} catch (Exception e) {
				response.put("response", Integer.valueOf(201));
			}
		} else {
			response.put("response", Integer.valueOf(401));
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping({ "/registrar-empresa/" })
	public String NuevaEmpresa(Model model, Authentication auth, HttpSession session) {
		Emisor emisor = new Emisor();
		List<CProvincia> listaProvincias = this._provinciaService.findAll();
		model.addAttribute("listaProvincias", listaProvincias);
		model.addAttribute("emisor", emisor);
		return "/emisor/nueva-empresa";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping({ "/registrar-empresa/" })
	public String RegistrarNuevaEmpresa(Model model, Emisor emisor, Authentication auth, HttpSession session,
			SessionStatus status) {
		try {
			String tokenAccess = UUID.randomUUID().toString();
			Usuario UsuarioId = this._usuarioService.findByUsername(auth.getName());
			emisor.setUsuario(UsuarioId);
			emisor.setFechaCreacion(new Date());
			emisor.setTokenAccess(tokenAccess);
			emisor.setUserApi(emisor.getUserApi().trim());
			emisor.setPwApi(emisor.getPwApi().trim());
			this._emisorService.save(emisor);
			status.setComplete();
			this.log.info("pase por aqui");
			Usuario UsuarioAdmin = this._usuarioService.findById(Long.valueOf(Long.parseLong("1")));
			EmpresaUsuarioAcceso euAdmin = new EmpresaUsuarioAcceso();
			euAdmin.setUsuario(UsuarioAdmin);
			euAdmin.setEmisor(emisor);
			euAdmin.setStatus("1");
			euAdmin.setNivelAcceso(1);
			euAdmin.setFechaCreacion(new Date());
			this._empresaUsuarioAccesoService.save(euAdmin);
			this.log.info("Se asigno el emisor al administrador");
			if (UsuarioId.getId().longValue() != 1L) {
				EmpresaUsuarioAcceso eu = new EmpresaUsuarioAcceso();
				eu.setUsuario(UsuarioId);
				eu.setEmisor(emisor);
				eu.setStatus("1");
				eu.setNivelAcceso(1);
				eu.setFechaCreacion(new Date());
				this._empresaUsuarioAccesoService.save(eu);
				this.log.info("Se asigno el emisor al usuario registrado");
			}
			File identificacion = new File(this.pathUploadFilesApi + emisor.getIdentificacion());
			File cert = new File(this.pathUploadFilesApi + emisor.getIdentificacion() + "/cert");
			if (!identificacion.exists())
				if (identificacion.mkdir()) {
					this.log.info("Directorio " + identificacion + " se ha creado con éxito");
				} else {
					this.log.info("No se pudo crear el directorio " + identificacion);
				}
			if (!cert.exists())
				if (cert.mkdir()) {
					this.log.info("Directorio " + cert + " se ha creado con éxito");
				} else {
					this.log.info("No se pudo crear el directorio " + cert);
				}
			return "redirect:/emisor/edit/" + emisor.getId() + "?exito";
		} catch (Exception e) {
			e.printStackTrace();
			List<CProvincia> listaProvincias = this._provinciaService.findAll();
			model.addAttribute("listaProvincias", listaProvincias);
			model.addAttribute("emisor", emisor);
			model.addAttribute("response", "2");
			List<CCanton> listaCantones = this._cantonService.findAllByIdProvincia(Long.valueOf(
					(emisor.getProvincia() != null) ? emisor.getProvincia().getId().longValue() : Long.parseLong("0")));
			List<CDistrito> listaDistritos = this._distritoService.findAllByIdCanton(Long.valueOf(
					(emisor.getCanton() != null) ? emisor.getCanton().getId().longValue() : Long.parseLong("0")));
			List<CBarrio> listaBarrios = this._barrioService.findAllByIdDistrito(Long.valueOf(
					(emisor.getDistrito() != null) ? emisor.getDistrito().getId().longValue() : Long.parseLong("0")));
			model.addAttribute("listaProvincias", listaProvincias);
			model.addAttribute("listaCantones", listaCantones);
			model.addAttribute("listaDistritos", listaDistritos);
			model.addAttribute("listaBarrios", listaBarrios);
			return "/emisor/nueva-empresa";
		}
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER", "ROLE_USER_COBRADOR", "ROLE_CLIENTES" })
	@GetMapping({ "/ingresar/{id}" })
	public String AccesarEmpresa(Model model, HttpSession session, @PathVariable("id") Long id, Authentication auth)
			throws ParseException {
		Emisor emisor = null;
		String resp = "";
		if (id.longValue() > 0L) {
			session.setAttribute("SESSION_EMPRESA_ID", id);
			emisor = this._emisorService.findById(id);
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Date today = sdf.parse(sdf.format(new Date()));
			int res = emisor.getFechaCaducidadCert().compareTo(today);
			if (res > 0) {
				this.log.info("Puede seguir facturando tranquilo" + res);
			} else {
				this.log.info("Debe actualizar la llave criptográfica para continuar facturando" + res);
				return "redirect:/emisor/edit/" + emisor.getId() + "?cambiarLlave";
			}
			Usuario usuario = this._usuarioService.findByUsername(auth.getName());
			List<CTerminalUsuario> sucursales = this._terminalUsuarioService.findAllByEmisorAndSucursal(emisor.getId(),
					usuario.getId());
			if (sucursales.size() == 1) {
				Long sucursal = null;
				Long terminal = null;
				for (CTerminalUsuario j : sucursales)
					sucursal = j.getTerminal().getSucursal().getId();
				List<CTerminalUsuario> terminales = this._terminalUsuarioService.findAllBySucursalByUsuario(sucursal,
						usuario.getId());
				if (terminales.size() == 1) {
					for (CTerminalUsuario j : terminales)
						terminal = j.getTerminal().getId();
					CTerminalUsuario tu = this._terminalUsuarioService
							.findSucursalTerminalBySucursalByTerminal(sucursal, terminal, usuario.getId());
					if (tu != null) {
						session.setAttribute("SESSION_SUCURSAL_ID", sucursal);
						session.setAttribute("SESSION_TERMINAL_ID", terminal);
						session.setAttribute("SESSION_NUMERO_SUCURSAL",
								Integer.valueOf(tu.getTerminal().getSucursal().getSucursal()));
						session.setAttribute("SESSION_NUMERO_TERMINAL",
								Integer.valueOf(tu.getTerminal().getTerminal()));
						session.setAttribute("SESSION_SUCURSAL", tu.getTerminal().getSucursal().getNombreSucursal());
						session.setAttribute("SESSION_TERMINAL", tu.getTerminal().getNombreTerminal());
						session.setAttribute("SESSION_ACTIVIDAD_DEFAULT", tu.getActividadEconomica());
						session.setAttribute("SESSION_PARAM_TIPO_DOC", tu.getTipoDeDocumento());
						session.setAttribute("SESSION_PARAM_INCLUIR_OMITIR_RECEPTOR", tu.getIncluirOmitirReceptor());
						session.setAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO", tu.getTipoBusquedaProducto());
						session.setAttribute("SESSION_PARAM_MODIFICA_PRECIO_FACTURACION",
								tu.getModificaPrecioFacturacion());
						session.setAttribute("SESSION_PARAM_APLICA_DESCUENTO", tu.getAplicaDescuentoFacturacion());
						session.setAttribute("SESSION_PARAM_MONTO_MAX_DESCUENTO", tu.getDescuentoFacturacion());
						session.setAttribute("SESSION_PARAM_MONEDA_USUARIO", tu.getMoneda().getId());
					}
				}
			}
			session.setAttribute("SESSION_DATOS_EMPRESA_IDENTIFICACION", emisor.getIdentificacion());
			session.setAttribute("SESSION_DATOS_EMPRESA_NOMBRE", emisor.getNombreRazonSocial());
			session.setAttribute("SESSION_LOGO_EMPRESA", emisor.getLogoEmpresa());
			session.setAttribute("SESSION_CONTROL_CAJAS", emisor.getControlCajas());
			session.setAttribute("SESSION_CONTROL_IMPRESION", emisor.getImpresion());
			session.setAttribute("SESSION_DECIMALES", emisor.getDecimales());
			session.setAttribute("SESSION_APLICA_DEVOLUCION_IVA", emisor.getDevolucionIva());
			session.setAttribute("SESSION_SISTEMA_FARMACIA", emisor.getSistemaFarmacias());
			session.setAttribute("SESSION_TIPO_CLIENTE", emisor.getTipoCliente());
			model.addAttribute("emisor", emisor);
			session.setAttribute("SESSION_IDENTIFICACION_EMPRESA", emisor.getIdentificacion());
			resp = "redirect:/";
		}
		return resp;
	}

	@PostMapping(value = { "/save" }, produces = { "application/json" })
	public ResponseEntity<?> Save(@Valid Emisor emisor, BindingResult result, Model model, SessionStatus status,
			@RequestParam("llaveCriptografica") MultipartFile llaveCriptografica,
			@RequestParam("logoEmpresa") MultipartFile logoEmpresa) {
		Map<String, Object> resp = new HashMap<>();
		String llave = "";
		String logo = "";
		String certificado = "";
		try {
			if (llaveCriptografica != null && !llaveCriptografica.isEmpty()) {
				llave = uploadLlaveCriptografica(llaveCriptografica, emisor.getIdentificacion());
				emisor.setCertificado(llave);
				resp.put("llave", llave);
				try {
					certificado = this.pathUploadFilesApi + emisor.getIdentificacion() + "/cert/" + llave;
					File f = new File(certificado);
					if (f.exists()) {
						KeyStore p12 = KeyStore.getInstance("PKCS12");
						FileInputStream fi = null;
						fi = new FileInputStream(certificado);
						p12.load(fi, emisor.getPingApi().toCharArray());
						Enumeration<?> e = p12.aliases();
						while (e.hasMoreElements()) {
							String alias = (String) e.nextElement();
							X509Certificate c = (X509Certificate) p12.getCertificate(alias);
							emisor.setFechaEmisionCert(c.getNotBefore());
							emisor.setFechaCaducidadCert(c.getNotAfter());
						}
					}
					resp.put("resp_llave", Integer.valueOf(1));
				} catch (Exception e) {
					this.log.info("Mensaje generado en el metodo save de EmisorController: " + e.getMessage());
					resp.put("resp_llave", Integer.valueOf(2));
				}
			}
			if (logoEmpresa != null && !logoEmpresa.isEmpty()) {
				logo = uploadLogoEmpresa(logoEmpresa);
				emisor.setLogoEmpresa(logo);
				resp.put("logo", logo);
			}
			this._emisorService.save(emisor);
			resp.put("resp", "1");
		} catch (Exception e) {
			e.printStackTrace();
			resp.put("resp", "2");
		}
		return new ResponseEntity(resp, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping(value = { "/update-dates-criptografica" }, produces = { "application/json" })
	public ResponseEntity<?> updateDatesCriptografica(Model model, HttpSession session) {
		Map<String, Object> resp = new HashMap<>();
		String certificado = "";
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Emisor emisor = this._emisorService.findById(emisorId);
				certificado = this.pathUploadFilesApi + emisor.getIdentificacion() + "/cert/" + emisor.getCertificado();
				SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
				File f = new File(certificado);
				if (f.exists()) {
					KeyStore p12 = KeyStore.getInstance("PKCS12");
					FileInputStream fi = null;
					fi = new FileInputStream(certificado);
					p12.load(fi, emisor.getPingApi().toCharArray());
					Enumeration<?> e = p12.aliases();
					while (e.hasMoreElements()) {
						String alias = (String) e.nextElement();
						X509Certificate c = (X509Certificate) p12.getCertificate(alias);
						this._emisorService.updateFechaCriptografica(c.getNotAfter(), c.getNotBefore(), emisorId);
						resp.put("msj", "Fecha de emisión: " + dt.format(c.getNotBefore()) + " \nFecha de caducidad: "
								+ dt.format(c.getNotAfter()) + " \n Emisor: " + emisor.getNombreRazonSocial());
					}
				}
				resp.put("resp", Integer.valueOf(1));
			} else {
				resp.put("msj", "");
				resp.put("resp", Integer.valueOf(2));
			}
		} catch (Exception e) {
			this.log.info("Mensaje generado en el metodo save de EmisorController: " + e.getMessage());
			e.printStackTrace();
			resp.put("msj", e.getMessage());
			resp.put("resp", Integer.valueOf(3));
		}
		return new ResponseEntity(resp, HttpStatus.CREATED);
	}

	public String uploadLlaveCriptografica(MultipartFile llaveCriptorafica, String emisor) throws IOException {
		String nombreLlave = UUID.randomUUID().toString() + "-" + llaveCriptorafica.getOriginalFilename();
		Files.copy(llaveCriptorafica.getInputStream(), Paths
				.get(this.pathUploadFilesApi + emisor + "/cert", new String[0]).resolve(nombreLlave).toAbsolutePath(),
				new java.nio.file.CopyOption[0]);
		return nombreLlave;
	}

	public String uploadLogoEmpresa(MultipartFile logoEmpresa) throws IOException {
		String nameLogo = UUID.randomUUID().toString() + "-" + logoEmpresa.getOriginalFilename();
		Files.copy(logoEmpresa.getInputStream(),
				Paths.get(this.pathUploadFilesApi + "/logo", new String[0]).resolve(nameLogo).toAbsolutePath(),
				new java.nio.file.CopyOption[0]);
		return nameLogo;
	}
}
