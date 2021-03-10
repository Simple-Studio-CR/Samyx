package com.samyx.controllers;

import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.EmpresaUsuarioAcceso;
import com.samyx.models.entity.Role;
import com.samyx.models.entity.RolesUsuario;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICProvinciaService;
import com.samyx.service.interfaces.ICTipoDeIdentificacionService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IEmpresaUsuarioAccesoService;
import com.samyx.service.interfaces.IRoleService;
import com.samyx.service.interfaces.IRolesUsuarioService;
import com.samyx.service.interfaces.IUsuarioService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ "usuario" })
public class UsuarioController {
	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IRoleService _roleService;

	@Autowired
	private IRolesUsuarioService _rolesUsuarioService;

	@Autowired
	private IEmisorService _emisorService;

	@Autowired
	private IEmpresaUsuarioAccesoService _empresaUsuarioAccesoService;

	@Autowired
	private ICProvinciaService _provinciaService;

	@Autowired
	private ICTipoDeIdentificacionService _tipoDeIdentificacionService;

	@Value("${correo.no.reply}")
	private String correoNoReply;

	@Value("${path.upload.files.api}")
	private String pathUploadFilesApi;

	@Autowired
	public JavaMailSender emailSender;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping({ "/catalogos/usuarios" })
	public String Home(Model model, HttpSession session, Authentication auth) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Usuario usuario = this._usuarioService.findByUsername(auth.getName());
			if (usuario.getId().longValue() == 1L) {
				model.addAttribute("listUsuarios",
						this._empresaUsuarioAccesoService.findUsuariosByEmpresaAdmin(emisorId));
			} else {
				model.addAttribute("listUsuarios", this._empresaUsuarioAccesoService.findUsuariosByEmpresa(emisorId));
			}
			return "catalogos/usuarios/index";
		}
		return "redirect:/";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping({ "/catalogos/usuario/form" })
	public String form(Model model) {
		Usuario usuario = new Usuario();
		List<RolesUsuario> listRoles = this._rolesUsuarioService.findAll();
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("titulo", "Agregar nuevo registro");
		model.addAttribute("usuario", usuario);
		return "catalogos/usuarios/form";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping({ "/catalogos/usuario/save" })
	public String save(@Valid Usuario usuario, BindingResult result, Model model, SessionStatus status,
			HttpSession session) {
		String form = "";
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				if (usuario.getId() != null && usuario.getId().longValue() > 0L) {
					this._usuarioService.save(usuario);
				} else {
					Role rol = new Role();
					rol.setAuthority("ROLE_USER");
					usuario.addRoleUsuario(rol);
					usuario.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
					this._usuarioService.save(usuario);
					Emisor emisor = this._emisorService.findById(emisorId);
					EmpresaUsuarioAcceso eu = new EmpresaUsuarioAcceso();
					eu.setUsuario(usuario);
					eu.setEmisor(emisor);
					eu.setStatus("1");
					eu.setNivelAcceso(1);
					eu.setFechaCreacion(new Date());
					this._empresaUsuarioAccesoService.save(eu);
				}
				status.setComplete();
				form = "redirect:/catalogos/usuarios";
			} else {
				return "redirect:/";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("titulo", "Agregar nuevo registro");
			model.addAttribute("usuario", usuario);
			model.addAttribute("response", Integer.valueOf(2));
			return "catalogos/usuarios/form";
		}
		return form;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@GetMapping({ "/catalogos/usuario/edit/{id}" })
	public String edit(@PathVariable("id") Long id, Model model, Authentication authentication) {
		Usuario usuario = null;
		List<RolesUsuario> listRoles = this._rolesUsuarioService.findAll();
		if (id.longValue() > 0L) {
			usuario = this._usuarioService.findById(id);
		} else {
			return "redirect:/catalogos/usuarios";
		}
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("formRol", Boolean.valueOf(true));
		model.addAttribute("titulo", "Actualizar el usuario " + usuario.getUsername());
		model.addAttribute("usuario", usuario);
		return "catalogos/usuarios/form";
	}

	@PostMapping({ "/usuarios/cambiar-password" })
	public ResponseEntity<?> changePassword(Usuario usuario, Authentication authentication) throws IOException {
		this.log.info("obteniendo passswored " + usuario.getPassword().trim());
		Map<String, Object> response = new HashMap<>();
		List<String> listErros = new ArrayList<>();
		try {
			Usuario userId = this._usuarioService.findByUsername(authentication.getName());
			this._usuarioService.updatePassword(this.passwordEncoder.encode(usuario.getPassword().trim()),
					userId.getId());
			response.put("message", "OK");
			return new ResponseEntity(response, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "ERROR");
			listErros.add(e.getMessage());
			response.put("errors", listErros);
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({ "ROLE_ADMIN" })
	@GetMapping({ "/catalogos/usuario/role-delete/{id}/{id_user}" })
	public String deleteRole(@PathVariable("id") Long id, @PathVariable("id_user") Long id_user) {
		this._roleService.deleteById(id);
		return "redirect:/catalogos/usuario/edit/" + id_user;
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping({ "/catalogos/usuario/save-role" })
	public String addRole(@RequestParam(value = "rol", required = false) String rol,
			@RequestParam(value = "id", required = false) Long idUser) {
		this._roleService.addRole(rol, idUser);
		return "redirect:/catalogos/usuario/edit/" + idUser;
	}

	@GetMapping({ "/nuevo-registro" })
	public String RegistroUsuarios(Model model) {
		Usuario usuario = new Usuario();
		List<RolesUsuario> listRoles = this._rolesUsuarioService.findAll();
		List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
		List<CProvincia> listaProvincias = this._provinciaService.findAll();
		model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
		model.addAttribute("listaProvincias", listaProvincias);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("titulo", "Nuevo usuario");
		model.addAttribute("usuario", usuario);
		return "catalogos/usuarios/registro";
	}

	@PostMapping({ "/nuevo-registro" })
	public String RegistroUsuariosSave(Usuario usuario, BindingResult result, SessionStatus status, Model model) {
		try {
			Role rol = new Role();
			rol.setAuthority("ROLE_USER");
			usuario.addRoleUsuario(rol);
			usuario.setPassword(this.passwordEncoder.encode(usuario.getPassword()));
			usuario.setEnabled(Boolean.valueOf(true));
			this._usuarioService.save(usuario);
			status.setComplete();
			return "redirect:/login?exito";
		} catch (Exception e) {
			e.printStackTrace();
			status.setComplete();
			model.addAttribute("response", Integer.valueOf(2));
			model.addAttribute("usuario", usuario);
			return "catalogos/usuarios/registro";
		}
	}

	@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping(value = { "/catalogos/usuario/registrar-rol" }, produces = { "application/json" })
	public ResponseEntity<?> RegistroRolUsuarios(@RequestParam(name = "id") Long userId,
			@RequestParam(name = "rol", defaultValue = "0") Integer rol, Model model) {
		Map<String, Object> resp = new HashMap<>();
		String rolAsignado = "";
		switch (rol.intValue()) {
		case 1:
			rolAsignado = "ROLE_SUPER_ADMIN";
			break;
		case 2:
			rolAsignado = "ROLE_ADMIN";
			break;
		case 3:
			rolAsignado = "ROLE_USER";
			break;
		case 4:
			rolAsignado = "ROLE_USER_COBRADOR";
			break;
		case 5:
			rolAsignado = "ROLE_CAJAS_FULL";
			break;
		default:
			rolAsignado = "";
			break;
		}
		try {
			this._roleService.addRole(rolAsignado, userId);
			resp.put("response", Integer.valueOf(200));
			resp.put("msj", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			resp.put("response", Integer.valueOf(404));
			resp.put("msj", "El usuario ya tiene asignado el ROL");
		}
		return new ResponseEntity(resp, HttpStatus.CREATED);
	}

	@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping(value = { "/catalogos/usuario/delete-rol" }, produces = { "application/json" })
	public ResponseEntity<?> EliminarRolUsuarios(@RequestParam(name = "id") Long id, Model model) {
		Map<String, Object> resp = new HashMap<>();
		try {
			this._roleService.deleteById(id);
			resp.put("response", Integer.valueOf(200));
			resp.put("msj", "ok");
		} catch (Exception e) {
			e.printStackTrace();
			resp.put("response", Integer.valueOf(404));
			resp.put("msj", "Error al intentar eliminar el ROL");
		}
		return new ResponseEntity(resp, HttpStatus.CREATED);
	}

	@PostMapping(value = { "/account/forgot" }, produces = { "application/json" })
	public ResponseEntity<?> RegistroUsuariosSave(@RequestParam("term") String term) throws MessagingException {
		Map<String, Object> resp = new HashMap<>();
		Usuario usuario = this._usuarioService.findByUsuarioOrEmail(term.toUpperCase().trim());
		if (usuario != null) {
			String resetPw = UUID.randomUUID().toString();
			this._usuarioService.updatePassword(this.passwordEncoder.encode(resetPw), usuario.getId());
			MimeMessage message = this.emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			String msj = null;
			msj = "<p style=\"font-family: Arial;\">Estimad@ <b>" + usuario.getNombre() + "</b></p>";
			msj = msj + "<p style=\"font-family: Arial;\">Estos son las credenciales para ingresar a su cuenta.</p>";
			msj = msj + "<p style=\"font-family: Arial;\">Su usuario es: <b>" + usuario.getUsername() + "</b> </p>";
			msj = msj + "<p style=\"font-family: Arial;\">Su contrasenueva es: <b>" + resetPw + "</b> </p>";
			msj = msj + "<p style=\"font-family: Arial;\">Ingrese al sistema y csi aslo desea.</p>";
			msj = msj
					+ "<p style=\"font-family: Arial;\"><i>Copie la contraseen un blog de notas, esto porque se pueden agregar espacios al final y no le permitiringresar al sistema.</i></p>";
			msj = msj + "<p style=\"font-family: Arial;\">Saludos,</p>";
			msj = msj + "<p style=\"font-family: Arial;\"><b>Soluciones Informaticas Mata</b></p>";
			helper.setFrom(this.correoNoReply);
			helper.setTo(usuario.getEmail());
			helper.setSubject("Password Reset, Soluciones Informaticas Mata");
			helper.setText(msj, true);
			try {
				this.emailSender.send(message);
				this.log.info("Se envun mail a " + usuario.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
				this.log.info("No se pudo enviar el correo a " + usuario.getEmail());
			}
			resp.put("response", Integer.valueOf(1));
			resp.put("msj", "Revise su correo, si el usuario o el correo electrexisten estaren su bandeja de entrada.");
		} else {
			resp.put("response", Integer.valueOf(1));
			resp.put("msj",
					"Revise su correo, si el usuario o el correo electrexisten estaren su bandeja de entrada..");
		}
		return new ResponseEntity(resp, HttpStatus.CREATED);
	}
}
