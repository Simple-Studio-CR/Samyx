package com.samyx.controllers.cxc;

import com.samyx.models.entity.CMedioDePago;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.FEFacturaRegistroPagosCXC;
import com.samyx.models.entity.FEFacturasCXC;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICMedioDePagoService;
import com.samyx.service.interfaces.ICTipoDeIdentificacionService;
import com.samyx.service.interfaces.IClienteService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IFEFacturaRegistroPagosCXCService;
import com.samyx.service.interfaces.IFEFacturasCXCServices;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/cxc" })
public class CuentasPorCobrarController {
	@Autowired
	private IFEFacturasCXCServices _cxcFacturasService;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	public DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ICTipoDeIdentificacionService _tipoDeIdentificacionService;

	@Autowired
	private IClienteService _clienteService;

	@Autowired
	private IFEFacturaRegistroPagosCXCService _cxcService;

	@Autowired
	private ICMedioDePagoService _medioDePagoService;

	@Autowired
	private IEmisorService _emisorService;

	@GetMapping({ "/" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q,
			@RequestParam(name = "e", defaultValue = "A") String e, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<FEFacturasCXC> ListaFacturas = null;
			if (e.equalsIgnoreCase("C") || e.equalsIgnoreCase("N")) {
				ListaFacturas = this._cxcFacturasService.findAllCXCCanceladas(emisorId, e.toUpperCase(),
						q.toUpperCase(), (Pageable) pageRequest);
				model.addAttribute("tipoAcceso", Integer.valueOf(0));
			} else {
				ListaFacturas = this._cxcFacturasService.findAllCXC(emisorId, e.toUpperCase(), q.toUpperCase(),
						(Pageable) pageRequest);
				model.addAttribute("tipoAcceso", Integer.valueOf(1));
			}
			PageRender<FEFacturasCXC> pageRender = new PageRender("/cxc/", ListaFacturas);
			model.addAttribute("ListaFacturas", ListaFacturas);
			model.addAttribute("page", pageRender);
			return "/cuentas-por-cobrar/clientes/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/new" })
	public String newCXC(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			FEFacturasCXC cxc = new FEFacturasCXC();
			model.addAttribute("cxc", cxc);
			return "/cuentas-por-cobrar/clientes/new-cxc";
		}
		return "redirect:/";
	}

	@PostMapping({ "/new" })
	public String postCXC(FEFacturasCXC entity, Model model, HttpSession session, Authentication authentication) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
			Emisor emisor = this._emisorService.findById(emisorId);
			entity.setUsuario(UsuarioId);
			entity.setEmisor(emisor);
			entity.setEstadoPago("A");
			entity.setFechaEmisionFe(new Date());
			this._cxcFacturasService.save(entity);
			return "redirect:/cxc/";
		}
		return "redirect:/cxc/";
	}

	@GetMapping({ "/view/{id}" })
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Cliente cliente = this._clienteService.findByIdByUserId(id, emisorId, "C");
			List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
			List<Long> mediosDePago = new ArrayList<>();
			mediosDePago.add(Long.valueOf(Long.parseLong("1")));
			mediosDePago.add(Long.valueOf(Long.parseLong("2")));
			mediosDePago.add(Long.valueOf(Long.parseLong("3")));
			mediosDePago.add(Long.valueOf(Long.parseLong("4")));
			mediosDePago.add(Long.valueOf(Long.parseLong("100")));
			mediosDePago.add(Long.valueOf(Long.parseLong("101")));
			List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAllIn(mediosDePago);
			model.addAttribute("listaMedioDePago", listaMedioDePago);
			List<FEFacturasCXC> listaFacturasPendientes = this._cxcFacturasService.findAllCXCDetalle(emisorId,
					cliente.getId(), "A");
			model.addAttribute("listaFacturasPendientes", listaFacturasPendientes);
			model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
			model.addAttribute("cliente", cliente);
			model.addAttribute("clienteId", cliente.getId());
			model.addAttribute("listaMedioDePago", listaMedioDePago);
			model.addAttribute("standardDate", new Date());
			return "/cuentas-por-cobrar/clientes/form";
		}
		return "redirect:/";
	}

	@PostMapping({ "/facturas-pendientes" })
	public String getAbonos(Model model, HttpSession session, @RequestParam(name = "c", required = false) Long c,
			@RequestParam(name = "e", required = false) String e) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<FEFacturasCXC> listaFacturasPendientes = this._cxcFacturasService.findAllCXCDetalle(emisorId, c, e);
			model.addAttribute("listaFacturasPendientes", listaFacturasPendientes);
			return "/cuentas-por-cobrar/clientes/facturas-pendientes";
		}
		return "";
	}

	@PostMapping({ "/tramitar-factura" })
	public ResponseEntity<?> tramitarFactura(HttpSession session, @RequestParam("id") Long id,
			@RequestParam("et") String et) {
		Map<String, Object> response = new HashMap<>();
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			String tramitarFactura = "";
			if (et.equalsIgnoreCase("S")) {
				tramitarFactura = "N";
			} else {
				tramitarFactura = "S";
			}
			this.jdbcTemplate = new JdbcTemplate(this.dataSource);
			String sql = "UPDATE fe_factura_cxc SET en_tramite=? WHERE id = ?";
			Object[] params = { tramitarFactura, id };
			int rows = this.jdbcTemplate.update(sql, params);
			if (rows > 0) {
				response.put("response", Integer.valueOf(200));
			} else {
				response.put("response", Integer.valueOf(400));
			}
		} else {
			response.put("response", Integer.valueOf(400));
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	@PostMapping({ "/cargar-factura-cxc/" })
	public String cargarFacturaCxc(Model model, @RequestParam(name = "c", required = false) Long c,
			@RequestParam(name = "e", required = false) String e,
			@RequestParam(name = "id", defaultValue = "0") Long idFactura, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			List<FEFacturasCXC> listaFacturasPendientes = this._cxcFacturasService.findAllCXCDetalle(emisorId, c, "A");
			model.addAttribute("listaFacturasPendientes", listaFacturasPendientes);
			List<Long> mediosDePago = new ArrayList<>();
			mediosDePago.add(Long.valueOf(Long.parseLong("1")));
			mediosDePago.add(Long.valueOf(Long.parseLong("2")));
			mediosDePago.add(Long.valueOf(Long.parseLong("3")));
			mediosDePago.add(Long.valueOf(Long.parseLong("4")));
			mediosDePago.add(Long.valueOf(Long.parseLong("100")));
			mediosDePago.add(Long.valueOf(Long.parseLong("101")));
			List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAllIn(mediosDePago);
			model.addAttribute("listaMedioDePago", listaMedioDePago);
			return "/cuentas-por-cobrar/clientes/cargar-factuar-cancelar-cxc";
		}
		return "redirect:/";
	}

	@PostMapping({ "/abonar-factura" })
	public ResponseEntity<?> saveAbono(FEFacturaRegistroPagosCXC entity, Model model, Authentication authentication,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
				Emisor emisor = this._emisorService.findById(emisorId);
				entity.setUsuario(UsuarioId);
				entity.setFechaRegistroAbondo(new Date());
				entity.setEmisor(emisor);
				FEFacturaRegistroPagosCXC numeroCXC = this._cxcService.findMaxFacturaByEmisor(emisorId);
				if (numeroCXC != null) {
					entity.setNumeroCXC(Long.valueOf(numeroCXC.getNumeroCXC().longValue() + 1L));
				} else {
					entity.setNumeroCXC(Long.valueOf(Long.parseLong("1")));
				}
				this._cxcService.save(entity);
				if (truncateTo(entity.getFacturaCXC().getDeudaActualCXC().doubleValue(), 2) == 0.0D)
					this._cxcFacturasService.modificaEstadoFacturaCXC("C", emisorId, entity.getFacturaCXC().getId());
				response.put("response", "1");
				response.put("c", entity.getCliente().getId());
				response.put("f", entity.getFacturaCXC().getId());
			} else {
				response.put("response", "0");
				response.put("c", "0");
				response.put("f", "0");
			}
		} catch (Exception e) {
			response.put("response", "2");
			response.put("c", "0");
			response.put("f", "0");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping(value = { "/abonos" }, produces = { "application/json" })
	@ResponseBody
	public List<FEFacturaRegistroPagosCXC> loadAbonos(@RequestParam(name = "c", required = false) Long c,
			@RequestParam(name = "f", required = false) Long f, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			return this._cxcService.findAllPagosCXC(emisorId, c, f);
		}
		return null;
	}

	@PostMapping({ "/estado-cliente" })
	@ResponseBody
	public Map<String, Object> estadoCliente(@RequestParam(name = "c", defaultValue = "0") Long clienteId,
			HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			this.jdbcTemplate = new JdbcTemplate(this.dataSource);
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("emisorId",
					Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString())));
			parameters.addValue("clienteId", clienteId);
			System.out.println("clienteId " + clienteId);
			String sql = "SELECT facturas_pendientes, total_deuda, fecha_actual, fecha_vencimiento_fe  FROM cxc_pendientes WHERE emisor_id=:emisorId AND cliente_id=:clienteId";
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
					(JdbcOperations) this.jdbcTemplate);
			try {
				Map<String, Object> listaObjetos = namedParameterJdbcTemplate.queryForMap(sql,
						(SqlParameterSource) parameters);
				return listaObjetos;
			} catch (Exception e) {
				Map<String, Object> response = new HashMap<>();
				response.put("facturas_pendientes", Integer.valueOf(0));
				return response;
			}
		}
		return null;
	}

	@PostMapping({ "/deuda-cxc" })
	@ResponseBody
	public Map<String, Object> deudaCxcActual(@RequestParam(name = "c", defaultValue = "0") Long clienteId,
			HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			this.jdbcTemplate = new JdbcTemplate(this.dataSource);
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("emisorId",
					Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString())));
			String sql = "SELECT SUM(facturas_pendientes) facturas, sum(deuda_inicial) deuda_inicial, sum(total_movimientos) total_movimientos, sum(total_deuda) total_deuda FROM cxc_pendientes WHERE emisor_id=:emisorId";
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
					(JdbcOperations) this.jdbcTemplate);
			try {
				Map<String, Object> deudaActualCxc = namedParameterJdbcTemplate.queryForMap(sql,
						(SqlParameterSource) parameters);
				return deudaActualCxc;
			} catch (Exception e) {
				e.printStackTrace();
				Map<String, Object> response = new HashMap<>();
				response.put("facturas", Integer.valueOf(0));
				response.put("total_deuda", Integer.valueOf(0));
				return response;
			}
		}
		return null;
	}

	@PostMapping({ "/abonar-cancelar" })
	public ResponseEntity<?> abonarCancelar(Model model,
			@RequestParam(name = "idLinea[]", required = false) Long[] idLinea,
			@RequestParam(name = "montoPagar") Double montoPagar, @RequestParam(name = "medioDePago") Long medioDePago,
			@RequestParam(name = "numeroTarjeta", required = false) String numeroTarjeta,
			@RequestParam(name = "numeroAutorizacion", required = false) String numeroAutorizacion,
			@RequestParam(name = "numDocumento", required = false) String numDocumento,
			@RequestParam(name = "fechaPago", required = false) String fechaPago, Authentication authentication,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				Usuario usuario = this._usuarioService.findByUsername(authentication.getName());
				String[] _f1 = fechaPago.split("/");
				String fechaRegistroPago = _f1[2] + "-" + _f1[1] + "-" + _f1[0];
				Double montoAbono = Double.valueOf(0.0D);
				int count = 0;
				Long facturaId = Long.valueOf(0L);
				Long numeroCXC = Long.valueOf(0L);
				for (int i = 0; i < idLinea.length; i++) {
					facturaId = idLinea[i];
					if (i == 0)
						montoAbono = montoPagar;
					System.out.println("montoAbono inicial ============================= " + montoAbono);
					if (montoAbono.doubleValue() > 0.0D) {
						this.jdbcTemplate = new JdbcTemplate(this.dataSource);
						SimpleJdbcCall simpleJdbcCall = (new SimpleJdbcCall(this.jdbcTemplate))
								.withProcedureName("spAbonarCancelarCXC");
						Map<String, Object> parameters = new HashMap<>();
						parameters.put("_emisor_id_", emisorId);
						parameters.put("_user_id_", usuario.getId());
						parameters.put("_factura_id_", facturaId);
						parameters.put("_banco_id_", Integer.valueOf(0));
						parameters.put("_monto_pagar_", montoAbono);
						parameters.put("_medio_pago_id_", medioDePago);
						parameters.put("_numero_tarjeta_", numeroTarjeta);
						parameters.put("_numero_autorizacion_", numeroAutorizacion);
						parameters.put("_numero_documento_", numDocumento);
						parameters.put("_fecha_registro_pago_", fechaRegistroPago);
						parameters.put("_numero_cxc_", numeroCXC);
						MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(parameters);
						Map<String, Object> returnSp = simpleJdbcCall
								.execute((SqlParameterSource) mapSqlParameterSource);
						if (returnSp.get("response").toString().equalsIgnoreCase("1")) {
							montoAbono = Double.valueOf(Double.parseDouble(returnSp.get("dinero_sobro").toString()));
							System.out.println("montoAbono ============================= " + montoAbono);
							System.out.println("dinero_sobro ============================= " + montoAbono);
							numeroCXC = Long.valueOf(Long.parseLong(returnSp.get("_sec_numero_cxc_").toString()));
							count++;
						} else {
							response.put("response", Integer.valueOf(400));
						}
					}
				}
				response.put("proveedor", facturaId);
				response.put("response", "1");
				response.put("recibos", Integer.valueOf(count));
				response.put("numeroCXC", numeroCXC);
			} else {
				response.put("response", "0");
				response.put("recibos", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("response", "2");
			response.put("recibos", "0");
			response.put("f", "0");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping({ "/anular-abono-cxc" })
	public ResponseEntity<?> anularAbonoCxc(HttpSession session, @RequestParam("c") Long c, @RequestParam("f") Long f,
			@RequestParam("fid") Long fid) {
		Map<String, Object> response = new HashMap<>();
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			this.jdbcTemplate = new JdbcTemplate(this.dataSource);
			SimpleJdbcCall simpleJdbcCall = (new SimpleJdbcCall(this.jdbcTemplate))
					.withProcedureName("spAnularReciboCXC");
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("_numero_cxc_", f);
			parameters.put("_factura_cxc_id_", fid);
			parameters.put("_cliente_id_", c);
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(parameters);
			Map<String, Object> returnSp = simpleJdbcCall.execute((SqlParameterSource) mapSqlParameterSource);
			if (returnSp.get("response").toString().equalsIgnoreCase("1")) {
				response.put("response", Integer.valueOf(200));
			} else {
				response.put("response", Integer.valueOf(400));
			}
		} else {
			response.put("response", Integer.valueOf(400));
		}
		return new ResponseEntity(response, HttpStatus.OK);
	}

	private double truncateTo(double unroundedNumber, int decimalPlaces) {
		int truncatedNumberInt = (int) (unroundedNumber * Math.pow(10.0D, decimalPlaces));
		double truncatedNumber = truncatedNumberInt / Math.pow(10.0D, decimalPlaces);
		return truncatedNumber;
	}
}
