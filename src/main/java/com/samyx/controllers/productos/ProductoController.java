package com.samyx.controllers.productos;

import com.samyx.models.entity.CCodigosTarifasIva;
import com.samyx.models.entity.CImpuesto;
import com.samyx.models.entity.CProductoImpuesto;
import com.samyx.models.entity.CTipoDocumentoExoneracionOAutorizacion;
import com.samyx.models.entity.CTipoProducto;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Producto;
import com.samyx.models.entity.ProductoFamilia;
import com.samyx.models.entity.UnidadDeMedida;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICCodigosTarifasIvaService;
import com.samyx.service.interfaces.ICImpuestoService;
import com.samyx.service.interfaces.ICProductoImpuestoService;
import com.samyx.service.interfaces.ICProductoService;
import com.samyx.service.interfaces.ICTipoDocumentoExoneracionOAutorizacionService;
import com.samyx.service.interfaces.ICTipoProductoService;
import com.samyx.service.interfaces.IClienteService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.service.interfaces.IProductoFamiliaService;
import com.samyx.service.interfaces.IUnidadDeMedidaService;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping({ "/productos" })
@SessionAttributes({ "producto" })
@Secured({ "ROLE_ADMIN", "ROLE_USER" })
public class ProductoController {
	@Autowired
	private ICProductoService _productoService;

	@Autowired
	private IClienteService _clienteService;

	@Autowired
	private IUsuarioService _usuarioService;

	@Autowired
	private IMonedaService _monedaService;

	@Autowired
	private IUnidadDeMedidaService _unidadDeMedidaService;

	@Autowired
	private ICImpuestoService _impuestoService;

	@Autowired
	private ICProductoImpuestoService _productoImpuestoService;

	@Autowired
	private ICTipoProductoService _tipoProductoService;

	@Autowired
	private IEmisorService _emisorService;

	@Autowired
	private ICCodigosTarifasIvaService _codigosTarifasIvaService;

	@Autowired
	private ICTipoDocumentoExoneracionOAutorizacionService _tipoDocumentoExoneracionOAutorizacionService;

	@Autowired
	public DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IProductoFamiliaService _familiaService;

	@GetMapping({ "/" })
	public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			PageRequest pageRequest = PageRequest.of(page, 15);
			Page<Producto> ListaProductos = null;
			ListaProductos = this._productoService.findAllByIdEmpresaId(emisorId, q.toUpperCase(), q.toUpperCase(),
					(Pageable) pageRequest);
			PageRender<Producto> pageRender = new PageRender("/productos/", ListaProductos);
			model.addAttribute("ListaProductos", ListaProductos);
			model.addAttribute("page", pageRender);
			return "/catalogos/productos/index";
		}
		return "redirect:/";
	}

	@GetMapping({ "/form" })
	public String form(Model model, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Producto producto = new Producto();
			List<Moneda> listaMonedas = this._monedaService.findAll();
			List<UnidadDeMedida> listaUnidadDeMedida = this._unidadDeMedidaService.findAll();
			List<CTipoProducto> listaTipoProducto = this._tipoProductoService.findAll();
			List<ProductoFamilia> listaFamilias = this._familiaService.findAllByEmisorId(emisorId);
			List<Cliente> listaProveedores = this._clienteService.findAllClienteProveedor(emisorId, "P");
			List<CImpuesto> listaImpuestos = this._impuestoService.findAllImpuestos();
			List<CCodigosTarifasIva> listaCodigosTarifasIva = this._codigosTarifasIvaService.findAll();
			model.addAttribute("listaFamilias", listaFamilias);
			model.addAttribute("listaMonedas", listaMonedas);
			model.addAttribute("listaUnidadDeMedida", listaUnidadDeMedida);
			model.addAttribute("listaTipoProducto", listaTipoProducto);
			model.addAttribute("producto", producto);
			model.addAttribute("listaProveedores", listaProveedores);
			model.addAttribute("listaImpuestos", listaImpuestos);
			model.addAttribute("listaCodigosTarifasIva", listaCodigosTarifasIva);
			if (session.getAttribute("SESSION_TIPO_CLIENTE").toString().equalsIgnoreCase("A"))
				return "/catalogos/productos/formCantidad";
			return "/catalogos/productos/formFraccion";
		}
		return "redirect:/";
	}

	@GetMapping({ "/edit/{id}" })
	public String edit(Model model, @PathVariable("id") Long id, HttpSession session, Authentication authentication) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
			List<ProductoFamilia> listaFamilias = this._familiaService.findAllByEmisorId(emisorId);
			List<CImpuesto> listaImpuestos = this._impuestoService.findAllImpuestos();
			Producto producto = null;
			producto = this._productoService.findByIdByEmpresaId(id, emisorId);
			List<Moneda> listaMonedas = this._monedaService.findAll();
			List<UnidadDeMedida> listaUnidadDeMedida = this._unidadDeMedidaService.findAll();
			List<CTipoProducto> listaTipoProducto = this._tipoProductoService.findAll();
			List<CCodigosTarifasIva> listaCodigosTarifasIva = this._codigosTarifasIvaService.findAll();
			List<Cliente> listaProveedores = this._clienteService.findAllClienteProveedor(emisorId, "P");
			model.addAttribute("listaFamilias", listaFamilias);
			model.addAttribute("listaMonedas", listaMonedas);
			model.addAttribute("listaUnidadDeMedida", listaUnidadDeMedida);
			model.addAttribute("listaTipoProducto", listaTipoProducto);
			model.addAttribute("producto", producto);
			model.addAttribute("listaImpuestos", listaImpuestos);
			model.addAttribute("listaCodigosTarifasIva", listaCodigosTarifasIva);
			model.addAttribute("UsuarioId", UsuarioId);
			model.addAttribute("listaProveedores", listaProveedores);
			session.setAttribute("SESSION_PRODUCTO_ID", producto.getId());
			session.setAttribute("SESSION_PRODUCTO_TIPO_VENTA", producto.getTipoVenta());
			session.setAttribute("SESSION_PRODUCTO_FRACCIONES_POR_UNIDAD", producto.getFraccionesPorUnidad());
			model.addAttribute("productoId", id);
			if (session.getAttribute("SESSION_TIPO_CLIENTE").toString().equalsIgnoreCase("A"))
				return "/catalogos/productos/formCantidad";
			return "/catalogos/productos/formFraccion";
		}
		return "redirect:/";
	}

	@PostMapping({ "/form/save" })
	public ResponseEntity<?> save(Producto producto, Model model, Authentication authentication, HttpSession session,
			HttpServletRequest request, @RequestParam(name = "impuesto", defaultValue = "") Long impuesto,
			@RequestParam(name = "codigosTarifasIva", defaultValue = "") Long codigosTarifasIva,
			@RequestParam(name = "porcentaje", defaultValue = "") Double q, SessionStatus status) {
		Map<String, Object> response = new HashMap<>();
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Usuario UsuarioId = this._usuarioService.findByUsername(authentication.getName());
			Emisor emisorId = this._emisorService
					.findById(Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString())));
			try {
				if (producto.getId() == null) {
					response.put("registro", "N");
				} else {
					response.put("registro", "E");
				}
				if (producto.getId() == null)
					producto.setPrecioPromediado(producto.getPrecioCompra());
				producto.setUsuario(UsuarioId);
				producto.setEmisor(emisorId);
				if (producto.getFraccionesPorUnidad() == null)
					producto.setFraccionesPorUnidad(Double.valueOf(0.0D));
				if (producto.getUtilidadFraccion() == null)
					producto.setUtilidadFraccion(Double.valueOf(0.0D));
				if (producto.getPrecioFraccion() == null)
					producto.setPrecioFraccion(Double.valueOf(0.0D));
				if (producto.getTipoVenta() == null)
					producto.setTipoVenta("U");
				if (session.getAttribute("SESSION_PRODUCTO_TIPO_VENTA") == null || !session
						.getAttribute("SESSION_PRODUCTO_TIPO_VENTA").toString().equals(producto.getTipoVenta()))
					if (producto.getTipoVenta() != null && producto.getTipoVenta().equalsIgnoreCase("F")) {
						producto.setEntradas(Double.valueOf(producto.getEntradas().doubleValue()
								* producto.getFraccionesPorUnidad().doubleValue()));
					} else {
						try {
							producto.setEntradas(
									Double.valueOf(producto.getEntradas().doubleValue() / Double.parseDouble(session
											.getAttribute("SESSION_PRODUCTO_FRACCIONES_POR_UNIDAD").toString())));
						} catch (Exception e) {
							producto.setEntradas(producto.getEntradas());
						}
					}
				this._productoService.save(producto);
				model.addAttribute("UsuarioId", UsuarioId);
				if (impuesto != null && codigosTarifasIva != null) {
					CProductoImpuesto productoImpuesto = new CProductoImpuesto();
					CImpuesto _impuesto = this._impuestoService.findById(impuesto);
					CCodigosTarifasIva _codigosTarifasIva = this._codigosTarifasIvaService.findById(codigosTarifasIva);
					productoImpuesto.setProducto(producto);
					productoImpuesto.setImpuesto(_impuesto);
					productoImpuesto.setCodigosTarifasIva(_codigosTarifasIva);
					productoImpuesto
							.setPorcentaje(Double.valueOf(Double.parseDouble(request.getParameter("porcentaje"))));
					this._productoImpuestoService.save(productoImpuesto);
				}
				response.put("id", producto.getId());
				response.put("response", Integer.valueOf(200));
				response.put("msj", "Registro guardado con éxito.");
			} catch (Exception e) {
				response.put("id", Integer.valueOf(0));
				response.put("response", Integer.valueOf(404));
				response.put("msj", "Error.");
			}
		} else {
			response.put("id", Integer.valueOf(0));
			response.put("response", Integer.valueOf(301));
			response.put("msj", "Redirección");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping({ "/kardex" })
	public String kardex(Producto producto, Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			String sql = "";
			this.jdbcTemplate = new JdbcTemplate(this.dataSource);
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("emisorId", emisorId);
			parameters.addValue("productoId", producto.getId());
			NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
					(JdbcOperations) this.jdbcTemplate);
			sql = "SELECT * FROM v_kardex WHERE emisor_id=:emisorId AND producto_id=:productoId ORDER BY fecha_movimiento DESC";
			List<Map<String, Object>> listaKardex = namedParameterJdbcTemplate.queryForList(sql,
					(SqlParameterSource) parameters);
			model.addAttribute("listaKardex", listaKardex);
			return "/catalogos/productos/kardex";
		}
		return null;
	}

	@PostMapping({ "/impuesto/save" })
	public ResponseEntity<?> saveImpuesto(CProductoImpuesto productoImpuesto, Model model,
			Authentication authentication) {
		Map<String, Object> response = new HashMap<>();
		try {
			response.put("response", Integer.valueOf(200));
			this._productoImpuestoService.save(productoImpuesto);
			response.put("id", productoImpuesto.getProducto().getId());
		} catch (Exception e) {
			response.put("response", Integer.valueOf(404));
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping({ "/impuesto/delete" })
	public ResponseEntity<?> deleteImpuesto(@RequestParam(name = "id", required = false) Long id,
			@RequestParam(name = "productoId", required = false) Long productoId) {
		Map<String, Object> response = new HashMap<>();
		try {
			response.put("response", Integer.valueOf(200));
			this._productoImpuestoService.deleteById(id);
			response.put("id", productoId);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("response", Integer.valueOf(404));
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}

	@PostMapping({ "/impuesto/get-all" })
	public String getAllImpuestos(Model model, @RequestParam(name = "id", required = false) Long id) {
		List<CProductoImpuesto> listaImpuestos = this._productoImpuestoService.findAllByIdProducto(id);
		model.addAttribute("listaImpuestos", listaImpuestos);
		return "/catalogos/productos/impuestos";
	}

	@PostMapping({ "/impuesto/get-all-facturador" })
	public String getAllImpuestosFacturador(Model model, @RequestParam(name = "id", required = false) Long id) {
		List<CProductoImpuesto> listaImpuestos = this._productoImpuestoService.findAllByIdProducto(id);
		List<CTipoDocumentoExoneracionOAutorizacion> listaExoneracionOAutorizacion = this._tipoDocumentoExoneracionOAutorizacionService
				.findAll();
		model.addAttribute("listaImpuestos", listaImpuestos);
		model.addAttribute("listaExoneracionOAutorizacion", listaExoneracionOAutorizacion);
		model.addAttribute("productoId", id);
		return "/catalogos/productos/impuestos-facturador";
	}

	@PostMapping({ "/delete" })
	public ResponseEntity<?> deleteProducto(@RequestParam(name = "id", required = false) Long id, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
				Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
				this._productoService.deleteByIdByEmpresaId(id, emisorId);
				response.put("response", Integer.valueOf(1));
				response.put("msj", "Registro eliminado con éxito");
			} else {
				response.put("response", Integer.valueOf(2));
				response.put("msj", "No existe la empresa.");
			}
		} catch (Exception e) {
			response.put("response", Integer.valueOf(2));
			response.put("msj", "Este registro tiene impuestos ligados, eliminelos primero!!!");
		}
		return new ResponseEntity(response, HttpStatus.CREATED);
	}
}
