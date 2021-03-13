package com.samyx.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samyx.models.entity.CCodigoReferencia;
import com.samyx.models.entity.CCodigosTarifasIva;
import com.samyx.models.entity.CCondicionDeVenta;
import com.samyx.models.entity.CImpuesto;
import com.samyx.models.entity.CMedioDePago;
import com.samyx.models.entity.COtrosCargosTipoDocumento;
import com.samyx.models.entity.CProductoImpuesto;
import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CSucursal;
import com.samyx.models.entity.CTerminal;
import com.samyx.models.entity.CTerminalUsuario;
import com.samyx.models.entity.CTipoDeCambio;
import com.samyx.models.entity.CTipoDeDocumentoReferencia;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.CTipoDocumentoExoneracionOAutorizacion;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.FEBitacora;
import com.samyx.models.entity.FEExoneracionImpuestoItemFactura;
import com.samyx.models.entity.FEFactura;
import com.samyx.models.entity.FEFacturaItem;
import com.samyx.models.entity.FEFacturaOtrosCargos;
import com.samyx.models.entity.FEFacturaReferencia;
import com.samyx.models.entity.FEFacturaRegistroPagosCXC;
import com.samyx.models.entity.FEFacturasCXC;
import com.samyx.models.entity.FEImpuestosItemFactura;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Producto;
import com.samyx.models.entity.UnidadDeMedida;
import com.samyx.models.entity.Usuario;
import com.samyx.service.impl.COtrosCargosTipoDocumentoImpl;
import com.samyx.service.impl.FuncionesService;
import com.samyx.service.interfaces.ICCodigoReferenciaService;
import com.samyx.service.interfaces.ICCodigosTarifasIvaService;
import com.samyx.service.interfaces.ICCondicionDeVentaService;
import com.samyx.service.interfaces.ICImpuestoService;
import com.samyx.service.interfaces.ICMedioDePagoService;
import com.samyx.service.interfaces.ICProductoImpuestoService;
import com.samyx.service.interfaces.ICProductoService;
import com.samyx.service.interfaces.ICProvinciaService;
import com.samyx.service.interfaces.ICSucursalService;
import com.samyx.service.interfaces.ICTerminalService;
import com.samyx.service.interfaces.ICTerminalUsuarioService;
import com.samyx.service.interfaces.ICTipoDeCambioService;
import com.samyx.service.interfaces.ICTipoDeDocumentoReferenciaService;
import com.samyx.service.interfaces.ICTipoDeIdentificacionService;
import com.samyx.service.interfaces.ICTipoDocumentoExoneracionOAutorizacionService;
import com.samyx.service.interfaces.IClienteService;
import com.samyx.service.interfaces.IEmisorActividadesService;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IFEFacturaRegistroPagosCXCService;
import com.samyx.service.interfaces.IFEFacturaService;
import com.samyx.service.interfaces.IFEFacturasCXCServices;
import com.samyx.service.interfaces.IFEItemFacturaService;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.service.interfaces.IUnidadDeMedidaService;
import com.samyx.service.interfaces.IUsuarioService;
import com.samyx.util.PageRender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/proformas"})
public class ProformasController {
  @Autowired
  private ICProductoService _productoService;
  
  @Autowired
  private IClienteService _clienteService;
  
  @Autowired
  private IMonedaService _monedaService;
  
  @Autowired
  private FuncionesService _funcionesService;
  
  @Autowired
  private ICCondicionDeVentaService _condicionDeVentaService;
  
  @Autowired
  private IUnidadDeMedidaService _unidadDeMedidaService;
  
  @Autowired
  private ICMedioDePagoService _medioDePagoService;
  
  @Autowired
  private ICImpuestoService _impuestoService;
  
  @Autowired
  private ICProductoImpuestoService _productoImpuestoService;
  
  @Autowired
  private ICCodigoReferenciaService _codigoReferenciaService;
  
  @Autowired
  private IEmisorService _emisorService;
  
  @Autowired
  private ICTerminalUsuarioService _terminalUsuarioService;
  
  @Autowired
  private IUsuarioService _usuarioService;
  
  @Autowired
  private IFEFacturaService _facturaService;
  
  @Autowired
  private ICSucursalService _sucursalService;
  
  @Autowired
  private ICTerminalService _terminalService;
  
  @Autowired
  private ICTipoDeCambioService _tipoDeCambioService;
  
  @Autowired
  private ICTipoDeIdentificacionService _tipoDeIdentificacionService;
  
  @Autowired
  private COtrosCargosTipoDocumentoImpl _otorsCargosService;
  
  @Autowired
  private ICTipoDocumentoExoneracionOAutorizacionService _exoneraTipoDocService;
  
  @Autowired
  private ICProvinciaService _provinciaService;
  
  @Autowired
  private IFEFacturasCXCServices _cxcService;
  
  @Autowired
  private ICCodigosTarifasIvaService _tarifasIvaService;
  
  @Autowired
  public JavaMailSender emailSender;
  
  @Autowired
  public ICTipoDeDocumentoReferenciaService tipoDocReferenciaService;
  
  @Autowired
  public IEmisorActividadesService _actividadesService;
  
  @Autowired
  private IFEItemFacturaService _itemFacturaService;
  
  @Autowired
  private ICCodigosTarifasIvaService _codigosTarifasIvaService;
  
  @Value("${path.upload.files.api}")
  private String pathUploadFilesApi;
  
  @Value("${url.qr}")
  private String urlQr;
  
  @Value("${correo.de.distribucion}")
  private String correoDistribucion;
  
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  @Value("${api.jmata.recepcion}")
  private String apiJmataRecepcion;
  
  @Autowired
  public DataSource dataSource;
  
  private JdbcTemplate jdbcTemplate;
  
  @Autowired
  private IFEFacturaRegistroPagosCXCService _cxcRegistroPagosService;
  
  @Autowired
  private IFEFacturasCXCServices _cxcFacturasService;
  
  private String ACTION_FORM = "/proformas/edit/";
  
  private String TIPO_FORM = "";
  
  private String MENSAJE_FORM1 = "Proforma guardada con éxito!";
  
  private String MENSAJE_FORM2 = "¡Proforma guardada con éxito, redireccionando a la proforma guardada!";
  
  private String MENSAJE_FORM3 = "¡Solo se pueden guardar si el tipo de documento corresponde a una proforma!";
  
  @GetMapping({"/"})
  public String home(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "q", defaultValue = "") String q, HttpSession session) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      if (session.getAttribute("SESSION_SUCURSAL_ID") != null && session.getAttribute("SESSION_TERMINAL_ID") != null) {
        Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
        PageRequest pageRequest = PageRequest.of(page, 15);
        Page<FEFactura> ListaDocumentos = null;
        ListaDocumentos = this._facturaService.findProformasByEmisorId(emisorId, q.toUpperCase(), (Pageable)pageRequest);
        PageRender<FEFactura> pageRender = new PageRender("/proformas/", ListaDocumentos);
        model.addAttribute("ListaDocumentos", ListaDocumentos);
        model.addAttribute("page", pageRender);
        return "/facturacion/proformas/index";
      } 
      return "redirect:/proformas/seleccionar-terminal?urlRetorno=/proformas/";
    } 
    return "redirect:/";
  }
  
  @GetMapping({"/nueva"})
  public String form(Model model, HttpSession session, Authentication auth) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
      if (session.getAttribute("SESSION_SUCURSAL_ID") != null && session.getAttribute("SESSION_TERMINAL_ID") != null) {
        List<Moneda> listaMonedas = this._monedaService.findAll();
        List<UnidadDeMedida> listaUnidadDeMedida = this._unidadDeMedidaService.findAll();
        List<CCondicionDeVenta> listaCondicionDeVenta = this._condicionDeVentaService.findAll();
        List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAll();
        List<CImpuesto> listaImpuestos = this._impuestoService.findAllImpuestos();
        List<CCodigosTarifasIva> listaCodigosTarifasIva = this._codigosTarifasIvaService.findAll();
        List<CCodigoReferencia> listaCodigoReferencia = this._codigoReferenciaService.findAll();
        List<CTipoDeCambio> listaTiposDeCambio = this._tipoDeCambioService.findAllDivisas();
        List<COtrosCargosTipoDocumento> listaTipoDocOtrosCargos = this._otorsCargosService.findAll();
        List<CTipoDeDocumentoReferencia> listaTipoDocReferencia = this.tipoDocReferenciaService.findAll();
        List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
        List<CTipoDocumentoExoneracionOAutorizacion> listaTipoDocsExonera = this._exoneraTipoDocService.findAll();
        Cliente cliente = new Cliente();
        List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
        List<CProvincia> listaProvincias = this._provinciaService.findAll();
        FEFactura factura = new FEFactura();
        model.addAttribute("listaActividades", listaActividades);
        model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
        model.addAttribute("listaProvincias", listaProvincias);
        model.addAttribute("cliente", cliente);
        model.addAttribute("listaTipoDocsExonera", listaTipoDocsExonera);
        model.addAttribute("MODIFICA_PRECIO_FACTURACION", session.getAttribute("SESSION_PARAM_MODIFICA_PRECIO_FACTURACION").toString());
        model.addAttribute("APLICA_DESCUENTO", session.getAttribute("SESSION_PARAM_APLICA_DESCUENTO").toString());
        model.addAttribute("MONTO_MAXIMO_DESCUENTO", session.getAttribute("SESSION_PARAM_MONTO_MAX_DESCUENTO").toString());
        model.addAttribute("listaMonedas", listaMonedas);
        model.addAttribute("listaUnidadDeMedida", listaUnidadDeMedida);
        model.addAttribute("listaCondicionDeVenta", listaCondicionDeVenta);
        model.addAttribute("listaMedioDePago", listaMedioDePago);
        model.addAttribute("listaImpuestos", listaImpuestos);
        model.addAttribute("listaCodigosTarifasIva", listaCodigosTarifasIva);
        model.addAttribute("listaCodigoReferencia", listaCodigoReferencia);
        model.addAttribute("listaTiposDeCambio", listaTiposDeCambio);
        model.addAttribute("listaTipoDocOtrosCargos", listaTipoDocOtrosCargos);
        model.addAttribute("listaTipoDocReferencia", listaTipoDocReferencia);
        model.addAttribute("TIPO_DOC_USUARIO", session.getAttribute("SESSION_PARAM_TIPO_DOC").toString());
        if (session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO") != null && session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO").toString().equalsIgnoreCase("S")) {
          model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "S");
        } else {
          model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "M");
        } 
        if (session.getAttribute("SESSION_PARAM_MONEDA_USUARIO") != null) {
          model.addAttribute("MONEDA_USUARIO", session.getAttribute("SESSION_PARAM_MONEDA_USUARIO").toString());
        } else {
          model.addAttribute("MONEDA_USUARIO", Integer.valueOf(1));
        } 
        model.addAttribute("INCLUIR_OMITIR_RECEPTOR", session.getAttribute("SESSION_PARAM_INCLUIR_OMITIR_RECEPTOR").toString());
        model.addAttribute("SUCURSAL_ID", session.getAttribute("SESSION_SUCURSAL_ID"));
        model.addAttribute("TERMINAL_ID", session.getAttribute("SESSION_TERMINAL_ID"));
        model.addAttribute("SESSION_SUCURSAL", session.getAttribute("SESSION_SUCURSAL"));
        model.addAttribute("SESSION_TERMINAL", session.getAttribute("SESSION_TERMINAL"));
        model.addAttribute("URL_TIPO_DOCUMENTO", "/proformas/emitir-factura");
        model.addAttribute("factura", factura);
        model.addAttribute("ACTION_FORM", this.ACTION_FORM);
        model.addAttribute("TIPO_FORM", this.TIPO_FORM);
        model.addAttribute("MENSAJE_FORM1", this.MENSAJE_FORM1);
        model.addAttribute("MENSAJE_FORM2", this.MENSAJE_FORM2);
        model.addAttribute("MENSAJE_FORM3", this.MENSAJE_FORM3);
        model.addAttribute("V_FACTURADOR", "V1");
        if (session.getAttribute("SESSION_TIPO_CLIENTE").toString().equalsIgnoreCase("A")) {
          model.addAttribute("TXT_CANTIDAD_UNIDAD", "Cantidad");
          model.addAttribute("TXT_FRACCIONES", "Fracciones");
          model.addAttribute("TXT_PRECIO_UNIDAD", "Precio");
          model.addAttribute("TXT_PRECIO_FRACCIONES", "Precio");
          model.addAttribute("TIPO_VENTA", "A");
        } else {
          model.addAttribute("TXT_CANTIDAD_UNIDAD", "Unidades");
          model.addAttribute("TXT_FRACCIONES", "Fracciones");
          model.addAttribute("TXT_PRECIO_UNIDAD", "Precio unidad");
          model.addAttribute("TXT_PRECIO_FRACCIONES", "Precio fracción");
          model.addAttribute("TIPO_VENTA", "B");
        } 
        return "/facturacion/proformas/facturador";
      } 
      return "redirect:/proformas/seleccionar-terminal?urlRetorno=/proformas/";
    } 
    return "redirect:/";
  }
  
  @GetMapping({"/edit/{id}"})
  public String formEdit(Model model, @PathVariable("id") Long id, HttpSession session, Authentication auth) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
      FEFactura _factura = this._facturaService.findByEmisorById(emisorId, id);
      if (_factura.getClave() != null && _factura.getClave().length() > 40) {
        model.addAttribute("tipoDocNumerico", Long.valueOf(Long.parseLong(_factura.getClave().substring(30, 31))));
        String[] _FFe = _factura.getFechaEmisionFe().toString().substring(0, 10).split("-");
        model.addAttribute("fechaEmisionFe", _FFe[2] + "/" + _FFe[1] + "/" + _FFe[0]);
      } 
      List<Moneda> listaMonedas = this._monedaService.findAll();
      List<UnidadDeMedida> listaUnidadDeMedida = this._unidadDeMedidaService.findAll();
      List<CCondicionDeVenta> listaCondicionDeVenta = this._condicionDeVentaService.findAll();
      List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAll();
      List<CImpuesto> listaImpuestos = this._impuestoService.findAllImpuestos();
      List<CCodigosTarifasIva> listaCodigosTarifasIva = this._codigosTarifasIvaService.findAll();
      List<CCodigoReferencia> listaCodigoReferencia = this._codigoReferenciaService.findAll();
      List<CTipoDeCambio> listaTiposDeCambio = this._tipoDeCambioService.findAllDivisas();
      List<COtrosCargosTipoDocumento> listaTipoDocOtrosCargos = this._otorsCargosService.findAll();
      List<CTipoDeDocumentoReferencia> listaTipoDocReferencia = this.tipoDocReferenciaService.findAll();
      List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
      List<CTipoDocumentoExoneracionOAutorizacion> listaTipoDocsExonera = this._exoneraTipoDocService.findAll();
      Cliente cliente = new Cliente();
      List<CTipoDeIdentificacion> listaTipoDeIdentificacion = this._tipoDeIdentificacionService.findAll();
      List<CProvincia> listaProvincias = this._provinciaService.findAll();
      model.addAttribute("listaActividades", listaActividades);
      model.addAttribute("listaTipoDeIdentificacion", listaTipoDeIdentificacion);
      model.addAttribute("listaProvincias", listaProvincias);
      model.addAttribute("cliente", cliente);
      model.addAttribute("listaTipoDocsExonera", listaTipoDocsExonera);
      model.addAttribute("listaMonedas", listaMonedas);
      model.addAttribute("listaUnidadDeMedida", listaUnidadDeMedida);
      model.addAttribute("listaCondicionDeVenta", listaCondicionDeVenta);
      model.addAttribute("listaMedioDePago", listaMedioDePago);
      model.addAttribute("listaImpuestos", listaImpuestos);
      model.addAttribute("listaCodigosTarifasIva", listaCodigosTarifasIva);
      model.addAttribute("listaCodigoReferencia", listaCodigoReferencia);
      model.addAttribute("listaTiposDeCambio", listaTiposDeCambio);
      model.addAttribute("listaTipoDocOtrosCargos", listaTipoDocOtrosCargos);
      model.addAttribute("listaTipoDocReferencia", listaTipoDocReferencia);
      model.addAttribute("TIPO_DOC_USUARIO", session.getAttribute("SESSION_PARAM_TIPO_DOC").toString());
      if (session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO") != null && session.getAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO").toString().equalsIgnoreCase("S")) {
        model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "S");
      } else {
        model.addAttribute("TIPO_BUSQUEDA_PRODUCTO", "M");
      } 
      model.addAttribute("MODIFICA_PRECIO_FACTURACION", session.getAttribute("SESSION_PARAM_MODIFICA_PRECIO_FACTURACION").toString());
      model.addAttribute("APLICA_DESCUENTO", session.getAttribute("SESSION_PARAM_APLICA_DESCUENTO").toString());
      model.addAttribute("MONTO_MAXIMO_DESCUENTO", session.getAttribute("SESSION_PARAM_MONTO_MAX_DESCUENTO").toString());
      if (session.getAttribute("SESSION_PARAM_MONEDA_USUARIO") != null) {
        model.addAttribute("MONEDA_USUARIO", session.getAttribute("SESSION_PARAM_MONEDA_USUARIO").toString());
      } else {
        model.addAttribute("MONEDA_USUARIO", Integer.valueOf(1));
      } 
      model.addAttribute("INCLUIR_OMITIR_RECEPTOR", session.getAttribute("SESSION_PARAM_INCLUIR_OMITIR_RECEPTOR").toString());
      model.addAttribute("SUCURSAL_ID", session.getAttribute("SESSION_SUCURSAL_ID"));
      model.addAttribute("TERMINAL_ID", session.getAttribute("SESSION_TERMINAL_ID"));
      model.addAttribute("SESSION_SUCURSAL", session.getAttribute("SESSION_SUCURSAL"));
      model.addAttribute("SESSION_TERMINAL", session.getAttribute("SESSION_TERMINAL"));
      model.addAttribute("SUCURSAL_ID", session.getAttribute("SESSION_SUCURSAL_ID"));
      model.addAttribute("TERMINAL_ID", session.getAttribute("SESSION_TERMINAL_ID"));
      model.addAttribute("SESSION_SUCURSAL", session.getAttribute("SESSION_SUCURSAL"));
      model.addAttribute("SESSION_TERMINAL", session.getAttribute("SESSION_TERMINAL"));
      model.addAttribute("URL_TIPO_DOCUMENTO", "/proformas/emitir-factura");
      model.addAttribute("ACTION_FORM", this.ACTION_FORM);
      model.addAttribute("TIPO_FORM", this.TIPO_FORM);
      model.addAttribute("MENSAJE_FORM1", this.MENSAJE_FORM1);
      model.addAttribute("MENSAJE_FORM2", this.MENSAJE_FORM2);
      model.addAttribute("MENSAJE_FORM3", this.MENSAJE_FORM3);
      model.addAttribute("V_FACTURADOR", "V1");
      if (session.getAttribute("SESSION_TIPO_CLIENTE").toString().equalsIgnoreCase("A")) {
        model.addAttribute("TXT_CANTIDAD_UNIDAD", "Cantidad");
        model.addAttribute("TXT_FRACCIONES", "Fracciones");
        model.addAttribute("TXT_PRECIO_UNIDAD", "Precio");
        model.addAttribute("TXT_PRECIO_FRACCIONES", "Precio");
        model.addAttribute("TIPO_VENTA", "A");
      } else {
        model.addAttribute("TXT_CANTIDAD_UNIDAD", "Unidades");
        model.addAttribute("TXT_FRACCIONES", "Fracciones");
        model.addAttribute("TXT_PRECIO_UNIDAD", "Precio unidad");
        model.addAttribute("TXT_PRECIO_FRACCIONES", "Precio fracción");
        model.addAttribute("TIPO_VENTA", "B");
      } 
      model.addAttribute("factura", _factura);
      return "/facturacion/proformas/facturador";
    } 
    return "redirect:/";
  }
  
  @GetMapping({"/seleccionar-terminal"})
  public String SeleccionarTerminal(Model model, HttpSession session, Authentication auth) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
      Usuario usuario = this._usuarioService.findByUsername(auth.getName());
      model.addAttribute("listaSucursales", this._terminalUsuarioService
          .findAllByEmisorAndSucursal(emisorId, usuario.getId()));
      return "/facturacion/proformas/asignar-sucursal-terminal";
    } 
    return "redirect:/";
  }
  
  @PostMapping({"/delete-item"})
  public ResponseEntity<?> eliminarItem(Model model, HttpSession session, Authentication auth, @RequestParam("id") Long id) {
    Map<String, Object> response = new HashMap<>();
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      this._itemFacturaService.deleteById(id);
      response.put("response", Integer.valueOf(200));
    } else {
      response.put("response", Integer.valueOf(400));
    } 
    return new ResponseEntity(response, HttpStatus.OK);
  }
  
  @PostMapping({"/sucursal-terminal"})
  public String SucursalTerminal(Model model, HttpSession session, Authentication auth, @RequestParam("id") Long id) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Usuario usuario = this._usuarioService.findByUsername(auth.getName());
      model.addAttribute("listaTerminales", this._terminalUsuarioService.findAllBySucursalByUsuario(id, usuario.getId()));
      return "/facturacion/proformas/sucursal-terminal";
    } 
    return "redirect:/";
  }
  
  @PostMapping({"/seleccionar-terminal"})
  public String AsignarSeleccionarTerminal(Model model, HttpSession session, Authentication auth, @RequestParam("sucursal") Long sucursal, @RequestParam("terminal") Long terminal, @RequestParam("urlRetorno") String urlRetorno) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      session.setAttribute("SESSION_SUCURSAL_ID", sucursal);
      session.setAttribute("SESSION_TERMINAL_ID", terminal);
      Usuario usuario = this._usuarioService.findByUsername(auth.getName());
      CTerminalUsuario tu = this._terminalUsuarioService.findSucursalTerminalBySucursalByTerminal(sucursal, terminal, usuario.getId());
      if (tu != null) {
        session.setAttribute("SESSION_NUMERO_SUCURSAL", Integer.valueOf(tu.getTerminal().getSucursal().getSucursal()));
        session.setAttribute("SESSION_NUMERO_TERMINAL", Integer.valueOf(tu.getTerminal().getTerminal()));
        session.setAttribute("SESSION_SUCURSAL", tu.getTerminal().getSucursal().getNombreSucursal());
        session.setAttribute("SESSION_TERMINAL", tu.getTerminal().getNombreTerminal());
        session.setAttribute("SESSION_ACTIVIDAD_DEFAULT", tu.getActividadEconomica());
        session.setAttribute("SESSION_PARAM_TIPO_DOC", tu.getTipoDeDocumento());
        session.setAttribute("SESSION_PARAM_INCLUIR_OMITIR_RECEPTOR", tu.getIncluirOmitirReceptor());
        session.setAttribute("SESSION_PARAM_TIPO_BUSQUEDA_PRODUCTO", tu.getTipoBusquedaProducto());
        session.setAttribute("SESSION_PARAM_MODIFICA_PRECIO_FACTURACION", tu.getModificaPrecioFacturacion());
        session.setAttribute("SESSION_PARAM_APLICA_DESCUENTO", tu.getAplicaDescuentoFacturacion());
        session.setAttribute("SESSION_PARAM_MONTO_MAX_DESCUENTO", tu.getDescuentoFacturacion());
        session.setAttribute("SESSION_PARAM_MONEDA_USUARIO", tu.getMoneda().getId());
      } 
      if (urlRetorno != null)
        return "redirect:" + urlRetorno; 
      return "/";
    } 
    return "redirect:/";
  }
  
  @GetMapping(value = {"/buscar-productos/{term}"}, produces = {"application/json"})
  @ResponseBody
  public List<Producto> cargarProductos(@PathVariable String term, HttpSession session, @RequestParam(name = "b", defaultValue = "m") String tipoBusqueda) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
      if (tipoBusqueda.equalsIgnoreCase("a"))
        return this._productoService.findByCodigoProductoIgnoreCase(emisorId, term.toString().toUpperCase()); 
      return this._productoService.findByCodigoAndNombreProductoIgnoreCase(emisorId, term.toString().toUpperCase(), term.toString().toUpperCase(), 30);
    } 
    return null;
  }
  
  @GetMapping(value = {"/buscar-clientes/{term}"}, produces = {"application/json"})
  @ResponseBody
  public List<Cliente> buscarClientes(@RequestBody @PathVariable String term, @RequestParam(name = "td", required = true) String tipoDocumento, HttpSession session) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
      if (tipoDocumento != null && tipoDocumento.equalsIgnoreCase("FEC"))
        return this._clienteService.findByIdentificacionOrNombreCompleto(empresaId, "P", term.toString().toUpperCase()); 
      return this._clienteService.findByIdentificacionOrNombreCompleto(empresaId, "C", term.toString().toUpperCase());
    } 
    return null;
  }
  
  @PostMapping(value = {"/get-impuestos"}, produces = {"application/json"})
  @ResponseBody
  public List<CProductoImpuesto> getAllImpuestos(Model model, @RequestParam(name = "id", required = false) Long id) {
    return this._productoImpuestoService.findAllByIdProducto(id);
  }
  
  @PostMapping(value = {"/emitir-factura"}, produces = {"application/json"})
  public ResponseEntity<?> emiteFactura(FEFactura factura, @RequestParam(name = "idLinea[]", required = false) Long[] idLinea, @RequestParam(name = "item_id[]", required = false) Long[] itemId, @RequestParam(name = "cantidad[]", required = false) Double[] cantidad, @RequestParam(name = "partidaArancelaria[]", required = false) String[] partidaArancelaria, @RequestParam(name = "detalleProducto[]", required = false) String[] detalleProducto, @RequestParam(name = "codigoProducto1[]", required = false) String[] codigoProducto1, @RequestParam(name = "codigoProducto2[]", required = false) String[] codigoProducto2, @RequestParam(name = "unidadDeMedida[]", required = false) String[] unidadDeMedida, @RequestParam(name = "nombreProducto[]", required = false) String[] nombreProducto, @RequestParam(name = "precioUnitario[]", required = false) Double[] precioUnitario, @RequestParam(name = "montoTotal[]", required = false) Double[] _montoTotal, @RequestParam(name = "descuentoTotal[]", required = false) Double[] descuentoTotal, @RequestParam(name = "subtotal[]", required = false) Double[] subTotal, @RequestParam(name = "impuestoTotal[]", required = false) Double[] impuestoTotal, @RequestParam(name = "totalLinea[]", required = false) Double[] totalLinea, @RequestParam(name = "impuestoNeto[]", required = false) String[] impuestoNeto, @RequestParam(name = "correoCliente", required = false) String correoCliente, @RequestParam(name = "tipoVentaLinea[]", required = false) String[] tipoVentaLinea, @RequestParam(name = "fraccionesPorUnidad[]", required = false) Double[] fraccionesPorUnidad, @RequestParam(name = "fraccion[]", required = false) Double[] fraccion, @RequestParam(name = "precioFraccion[]", required = false) Double[] precioFraccion, HttpServletRequest request, Authentication authentication, HttpSession session) throws ClientProtocolException, IOException, ParseException {
    Map<String, Object> response = new HashMap<>();
    if (factura.getEstado() != null && factura.getEstado().equalsIgnoreCase("A")) {
      response.put("clave", "");
      response.put("consecutivo", "");
      response.put("fechaEmision", "");
      response.put("fileXmlSign", "");
      response.put("msj", "Esta factura ya esta aplicada, no puede volverla a aplicar.");
      response.put("response", Integer.valueOf(201));
      return new ResponseEntity(response, HttpStatus.CREATED);
    } 
    Double cantidadArticulos = Double.valueOf(0.0D);
    Double precioArticulos = Double.valueOf(0.0D);
    Double _fraccionesPorUnidad = Double.valueOf(0.0D);
    FEFacturasCXC cxc = new FEFacturasCXC();
    Cliente cliente = new Cliente();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Long empresaId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
    Emisor emisor = this._emisorService.findById(empresaId);
    String medioPago = "";
    String tipoDocumento = request.getParameter("tipoDocumento");
    String tipoClienteProveedor = "C";
    if (tipoDocumento.equalsIgnoreCase("FEC"))
      tipoClienteProveedor = "P"; 
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(this.apiJmataRecepcion);
    CloseableHttpResponse responseApi = null;
    HttpEntity entity2 = null;
    ObjectMapper objectMapper = null;
    JsonNode data = null;
    try {
      String j = "";
      String m = "";
      j = j + "{";
      j = j + "\"emisor\":\"" + emisor.getIdentificacion() + "\",";
      j = j + "\"tokenAccess\":\"" + emisor.getTokenAccess() + "\",";
      String codigoActividad = "";
      if (request.getParameter("codigoActividadEmisor") != null && request.getParameter("codigoActividadEmisor").length() > 0) {
        codigoActividad = request.getParameter("codigoActividadEmisor");
      } else {
        codigoActividad = request.getParameter("codigoActividadEmisor");
      } 
      j = j + "\"codigoActividadEmisor\":\"" + codigoActividad + "\",";
      j = j + "\"tipoDocumento\":\"" + tipoDocumento + "\",";
      j = j + "\"situacion\":\"" + request.getParameter("situacion") + "\",";
      j = j + "\"sucursal\":\"" + session.getAttribute("SESSION_NUMERO_SUCURSAL").toString() + "\",";
      j = j + "\"terminal\":\"" + session.getAttribute("SESSION_NUMERO_TERMINAL").toString() + "\",";
      j = j + "\"omitirReceptor\":\"" + request.getParameter("omitirReceptor") + "\",";
      factura.setEmisor(emisor);
      factura.setFechaEmisionFe(new Date());
      factura.setSituacion("normal");
      factura.setTipoDocumento(request.getParameter("tipoDocumento"));
      CSucursal sucursal = this._sucursalService.findById(Long.valueOf(Long.parseLong(session.getAttribute("SESSION_SUCURSAL_ID").toString())));
      factura.setSucursal(sucursal);
      CTerminal terminal = this._terminalService.findById(Long.valueOf(Long.parseLong(session.getAttribute("SESSION_TERMINAL_ID").toString())));
      factura.setTerminal(terminal);
      if (factura.getNumeroFactura() == null) {
        this.log.info("numero de factura esto fue lo ultimo que hice ");
        FEFactura numeroFactura = this._facturaService.findMaxFacturaByEmisor(empresaId);
        if (numeroFactura != null) {
          factura.setNumeroFactura(Long.valueOf(numeroFactura.getNumeroFactura().longValue() + 1L));
        } else {
          factura.setNumeroFactura(Long.valueOf(Long.parseLong("1")));
        } 
      } 
      Usuario usuario = this._usuarioService.findByUsername(authentication.getName());
      factura.setUsuario(usuario);
      if (request.getParameter("omitirReceptor").equalsIgnoreCase("false") && request.getParameter("receptor") != null && request.getParameter("receptor").length() > 0) {
        cliente = this._clienteService.findByIdByUserId(Long.valueOf(Long.parseLong(request.getParameter("receptor"))), empresaId, tipoClienteProveedor);
        if (tipoDocumento.equalsIgnoreCase("FEC")) {
          j = j + "\"codigoActividadEmisor\":\"" + cliente.getCodigoActividad() + "\",";
          factura.setCodigoActividadEmisor(cliente.getCodigoActividad());
        } 
        cxc.setCliente(cliente);
        factura.setCliente(cliente);
        j = j + "\"receptorNombre\":\"" + procesarTexto(cliente.getNombreCompleto()) + "\",";
        j = j + "\"receptorTipoIdentif\":\"" + cliente.getTipoDeIdentificacion().getId() + "\",";
        j = j + "\"receptorNumIdentif\":\"" + cliente.getIdentificacion() + "\",";
        if (cliente.getTipoDeIdentificacion().getId().longValue() == 5L) {
          j = j + "\"receptorOtrasSenas\":\"" + procesarTexto(cliente.getOtrasSenas()) + "\",";
        } else if (cliente.getProvincia() != null && cliente.getCanton() != null && cliente.getDistrito() != null && cliente.getOtrasSenas() != null) {
          j = j + "\"receptorProvincia\":\"" + cliente.getProvincia().getId() + "\",";
          j = j + "\"receptorCanton\":\"" + cliente.getCanton().getNumero_canton() + "\",";
          j = j + "\"receptorDistrito\":\"" + cliente.getDistrito().getNumeroDistrito() + "\",";
          if (cliente.getBarrio() != null)
            j = j + "\"receptorBarrio\":\"" + cliente.getBarrio().getNumeroBarrio() + "\","; 
          j = j + "\"receptorOtrasSenas\":\"" + procesarTexto(cliente.getOtrasSenas()) + "\",";
        } 
        j = j + "\"receptorCodPaisTel\":\"" + cliente.getCodigoPais() + "\",";
        j = j + "\"receptorTel\":\"" + cliente.getTelefono1() + "\",";
        if (cliente.getFax() != null && cliente.getFax().length() > 0) {
          j = j + "\"receptorCodPaisFax\":\"506\",";
          j = j + "\"receptorFax\":\"" + cliente.getFax() + "\",";
        } 
        if (!cliente.getCorreo1().equalsIgnoreCase(correoCliente))
          try {
            this._clienteService.updateCorreo(correoCliente, cliente.getId());
          } catch (Exception exception) {} 
        j = j + "\"receptorEmail\":\"" + procesarTexto(correoCliente) + "\",";
      } else {
        if (request.getParameter("inputClienteContado") != null && !request.getParameter("inputClienteContado").equals(""))
          j = j + "\"receptorNombre\":\"" + request.getParameter("inputClienteContado") + "\","; 
        if (request.getParameter("inputCorreoContado") != null && !request.getParameter("inputCorreoContado").equals(""))
          j = j + "\"receptorEmail\":\"" + procesarTexto(request.getParameter("inputCorreoContado")) + "\","; 
      } 
      j = j + "\"condVenta\":\"" + this._funcionesService.str_pad(request.getParameter("condVenta"), 2, "0", "STR_PAD_LEFT") + "\",";
      j = j + "\"plazoCredito\":\"" + request.getParameter("plazoCredito") + "\",";
      if ((request.getParameter("medioPago1") != null && Double.parseDouble(request.getParameter("medioPago1")) > 0.0D) || (request
        
        .getParameter("medioPagoDolar1") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar1")) > 0.0D) || (request
        .getParameter("medioPagoEuro1") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro1")) > 0.0D)) {
        medioPago = "1";
      } else if ((request.getParameter("medioPago2") != null && 
        Double.parseDouble(request.getParameter("medioPago2")) > 0.0D) || (request
        .getParameter("medioPagoDolar2") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar2")) > 0.0D) || (request
        .getParameter("medioPagoEuro2") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro2")) > 0.0D)) {
        medioPago = "2";
      } else if ((request.getParameter("medioPago3") != null && 
        Double.parseDouble(request.getParameter("medioPago3")) > 0.0D) || (request
        .getParameter("medioPagoDolar3") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar3")) > 0.0D) || (request
        .getParameter("medioPagoEuro3") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro3")) > 0.0D)) {
        medioPago = "3";
      } else if ((request.getParameter("medioPago4") != null && 
        Double.parseDouble(request.getParameter("medioPago4")) > 0.0D) || (request
        .getParameter("medioPagoDolar4") != null && 
        Double.parseDouble(request.getParameter("medioPagoDolar4")) > 0.0D) || (request
        .getParameter("medioPagoEuro4") != null && 
        Double.parseDouble(request.getParameter("medioPagoEuro4")) > 0.0D)) {
        medioPago = "4";
      } else {
        medioPago = "1";
      } 
      j = j + "\"medioPago\":\"" + this._funcionesService.str_pad(medioPago, 2, "0", "STR_PAD_LEFT") + "\",";
      Moneda moneda = this._monedaService.findById(Long.valueOf(Long.parseLong(request.getParameter("moneda"))));
      j = j + "\"detalleLinea\":{";
      int linea = 0;
      String naturalezaDescuento = "";
      for (int i = 0; i < itemId.length; i++) {
        String detalleProductoFinal;
        linea++;
        Double montoTotal = _montoTotal[i];
        Double descuento = Double.valueOf((descuentoTotal[i].toString() != null) ? Double.parseDouble(descuentoTotal[i].toString()) : 0.0D);
        Double subTotalLinea = subTotal[i];
        try {
          detalleProductoFinal = detalleProducto[i].toString();
        } catch (Exception e) {
          detalleProductoFinal = "";
        } 
        if (descuento.doubleValue() > 0.0D)
          naturalezaDescuento = "Cliente Frecuente."; 
        FEFacturaItem item = new FEFacturaItem();
        Producto producto = this._productoService.findById(Long.valueOf(Long.parseLong(itemId[i].toString())));
        try {
          item.setId(idLinea[i]);
        } catch (Exception e) {
          this.log.info("Es una nueva linea de la factura");
        } 
        item.setNumeroLinea(linea);
        item.setProducto(producto);
        item.setDetalleProducto(" " + detalleProductoFinal);
        cantidadArticulos = Double.valueOf(0.0D);
        try {
          if (!tipoVentaLinea[i].isEmpty() && tipoVentaLinea[i].toString().equals("F")) {
            _fraccionesPorUnidad = fraccionesPorUnidad[i];
            try {
              if (Double.parseDouble(cantidad[i].toString()) > 0.0D)
                cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[i].toString()) * _fraccionesPorUnidad.doubleValue()); 
            } catch (Exception e) {
              e.printStackTrace();
            } 
            try {
              if (fraccion[i].doubleValue() > 0.0D)
                cantidadArticulos = Double.valueOf(cantidadArticulos.doubleValue() + fraccion[i].doubleValue()); 
            } catch (Exception e) {
              e.printStackTrace();
            } 
            item.setUnidades(Double.valueOf(Double.parseDouble(cantidad[i].toString())));
            item.setTipoVentaLinea("F");
            item.setFraccionesPorUnidad(_fraccionesPorUnidad);
            item.setFraccion(fraccion[i]);
            item.setPrecioFraccion(precioFraccion[i]);
            precioArticulos = Double.valueOf(Double.parseDouble(montoTotal.toString()) / cantidadArticulos.doubleValue());
            item.setCantidad(cantidadArticulos);
            item.setPrecioUnitario(Double.valueOf(Double.parseDouble(procesarNumeros(precioArticulos.toString(), "#0.00"))));
          } else {
            item.setUnidades(Double.valueOf(0.0D));
            item.setTipoVentaLinea("U");
            item.setFraccionesPorUnidad(Double.valueOf(0.0D));
            item.setFraccion(Double.valueOf(0.0D));
            item.setPrecioFraccion(Double.valueOf(0.0D));
            cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[i].toString()));
            precioArticulos = Double.valueOf(Double.parseDouble(precioUnitario[i].toString()));
            item.setCantidad(cantidadArticulos);
            item.setPrecioUnitario(precioArticulos);
          } 
        } catch (Exception e) {
          item.setUnidades(Double.valueOf(0.0D));
          item.setTipoVentaLinea("U");
          item.setFraccionesPorUnidad(Double.valueOf(0.0D));
          item.setFraccion(Double.valueOf(0.0D));
          item.setPrecioFraccion(Double.valueOf(0.0D));
          cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[i].toString()));
          precioArticulos = Double.valueOf(Double.parseDouble(precioUnitario[i].toString()));
          item.setCantidad(cantidadArticulos);
          item.setPrecioUnitario(precioArticulos);
        } 
        item.setMontoTotal(Double.valueOf(Double.parseDouble(montoTotal.toString())));
        item.setSubTotal(subTotalLinea);
        item.setMontoDescuento(Double.valueOf(Double.parseDouble(descuento.toString())));
        item.setNaturalezaDescuento(naturalezaDescuento);
        item.setMontoTotalLinea(Double.valueOf(Double.parseDouble(totalLinea[i].toString())));
        item.setImpuestoNeto(Double.valueOf(Double.parseDouble(impuestoNeto[i].toString())));
        m = m + "\"" + linea + "\":{";
        m = m + "\"numeroLinea\":\"" + linea + "\",";
        if (producto.getEmisorCabys() != null)
          m = m + "\"codigo\":\"" + producto.getEmisorCabys().getCodigoHacienda() + "\","; 
        try {
          m = m + "\"partidaArancelaria\":\"" + partidaArancelaria[i].toString() + "\",";
        } catch (Exception exception) {}
        m = m + "\"codigoComercial\":{";
        m = m + "\"0\":{";
        m = m + "\"tipo\":\"" + this._funcionesService.str_pad(producto.getTipoProducto().getId().toString(), 2, "0", "STR_PAD_LEFT") + "\",";
        m = m + "\"codigo\":\"" + producto.getCodigo() + "\"";
        m = m + "}";
        m = m + "},";
        m = m + "\"cantidad\":\"" + procesarNumeros(cantidadArticulos.toString(), "#0.00") + "\",";
        m = m + "\"unidadMedida\":\"" + unidadDeMedida[i].toString() + "\",";
        m = m + "\"detalle\":\"" + procesarTexto(nombreProducto[i].toString() + " " + detalleProductoFinal) + "\",";
        m = m + "\"precioUnitario\":\"" + procesarNumeros(precioArticulos.toString(), "#0.00") + "\",";
        m = m + "\"montoTotal\":\"" + procesarNumeros(montoTotal.toString(), "#0.00") + "\",";
        m = m + "\"descuentos\":{";
        m = m + "\"0\":{";
        m = m + "\"montoDescuento\":\"" + procesarNumeros(descuento.toString(), "#0.00") + "\",";
        m = m + "\"naturalezaDescuento\":\"" + procesarTexto(naturalezaDescuento) + "\"";
        m = m + "}";
        m = m + "},";
        m = m + "\"subTotal\":\"" + procesarNumeros(subTotalLinea.toString(), "#0.00") + "\",";
        String[] impuestosId = request.getParameterValues("impuestosId" + itemId[i] + "[]");
        String[] impuestoTarifaIva = request.getParameterValues("impuestoTarifaIva" + itemId[i] + "[]");
        String[] Impuestoimpuestos = request.getParameterValues("Impuestoimpuestos" + itemId[i] + "[]");
        String[] impuestosMonto = request.getParameterValues("impuestosMonto" + itemId[i] + "[]");
        String[] checkBoxExonera = request.getParameterValues("exoneraCheckBox" + itemId[i] + "[]");
        String exoneraTipoDocumento = request.getParameter("exoneraTipoDocumentoExonera");
        String exoneraNumero = request.getParameter("exoneraNumero");
        String exoneraInstitucion = request.getParameter("exoneraInstitucion");
        String exoneraFechaEmision = request.getParameter("exoneraFechaEmision");
        String[] montoExoneracion = request.getParameterValues("montoExoneracion" + itemId[i] + "[]");
        String[] exoneraPorcentajeExoneracion = request.getParameterValues("exoneraPorcentajeExoneracion" + itemId[i] + "[]");
        if (impuestosId != null && impuestosId.length > 0) {
          String coma = "";
          m = m + "\"impuestos\":{";
          for (int q = 0; q < impuestosId.length; q++) {
            FEImpuestosItemFactura iif = new FEImpuestosItemFactura();
            try {
              String[] idLineaImpuesto = request.getParameterValues("idLineaImpuesto" + idLinea[i] + "[]");
              iif.setId(Long.valueOf(Long.parseLong(idLineaImpuesto[q])));
            } catch (Exception exception) {}
            try {
              CImpuesto impuesto = this._impuestoService.findById(Long.valueOf(Long.parseLong(impuestosId[q])));
              iif.setImpuesto(impuesto);
            } catch (Exception e) {
              this.log.info("CImpuesto no esta presente.");
            } 
            try {
              CCodigosTarifasIva tiva = this._tarifasIvaService.findById(Long.valueOf(Long.parseLong(impuestoTarifaIva[q].substring(1, 2))));
              iif.setCodigoTarifaIva(tiva);
            } catch (Exception e) {
              this.log.info("CodigosTarifasIva no esta presente.");
            } 
            iif.setTarifa(Double.valueOf(Double.parseDouble(Impuestoimpuestos[q])));
            iif.setMonto(Double.valueOf(Double.parseDouble(impuestosMonto[q])));
            if (q + 1 == impuestosId.length) {
              coma = "";
            } else {
              coma = ",";
            } 
            m = m + "\"" + q + "\":{";
            m = m + "\"codigo\":\"" + impuestosId[q] + "\",";
            m = m + "\"codigoTarifa\":\"" + impuestoTarifaIva[q] + "\",";
            m = m + "\"tarifa\":\"" + procesarNumeros(Impuestoimpuestos[q], "#0.00") + "\",";
            m = m + "\"monto\":\"" + procesarNumeros(impuestosMonto[q], "#0.00") + "\"";
            if (!tipoDocumento.equals("FEE"))
              try {
                if (checkBoxExonera[q] != null && !checkBoxExonera[q].toString().equals("") && checkBoxExonera[q].length() > 0) {
                  m = m + ",\"exoneracion\":{";
                  if (exoneraTipoDocumento != null && exoneraTipoDocumento.length() > 0)
                    m = m + "\"tipoDocumento\":\"" + exoneraTipoDocumento + "\","; 
                  if (exoneraNumero != null && exoneraNumero.length() > 0)
                    m = m + "\"numeroDocumento\":\"" + exoneraNumero + "\","; 
                  if (exoneraInstitucion != null && exoneraInstitucion.length() > 0)
                    m = m + "\"nombreInstitucion\":\"" + exoneraInstitucion + "\","; 
                  if (exoneraFechaEmision != null && exoneraFechaEmision.length() > 0)
                    m = m + "\"fechaEmision\":\"" + format.format((new SimpleDateFormat("dd/MM/yyyy"))
                        .parse(exoneraFechaEmision.toString())) + "\","; 
                  if (montoExoneracion[q] != null && montoExoneracion[q].length() > 0)
                    m = m + "\"montoExoneracion\":\"" + procesarNumeros(montoExoneracion[q], "#0.00") + "\","; 
                  if (exoneraPorcentajeExoneracion[q] != null && exoneraPorcentajeExoneracion[q].length() > 0)
                    m = m + "\"porcentajeExoneracion\":\"" + exoneraPorcentajeExoneracion[q] + "\""; 
                  m = m + "}";
                  FEExoneracionImpuestoItemFactura eiif = new FEExoneracionImpuestoItemFactura();
                  eiif.setTipoDocumento(exoneraTipoDocumento);
                  eiif.setNumeroDocumento(exoneraNumero);
                  eiif.setNombreInstitucion(exoneraInstitucion);
                  eiif.setFechaEmision(exoneraFechaEmision);
                  eiif.setMontoImpuesto(Double.valueOf(Double.parseDouble(montoExoneracion[q])));
                  eiif.setPorcentajeCompra(Integer.parseInt(exoneraPorcentajeExoneracion[q]));
                  iif.addItemFacturaImpuestosExoneracion(eiif);
                } 
              } catch (Exception exception) {} 
            item.addFEImpuestosItemFactura(iif);
            m = m + "}" + coma;
          } 
          m = m + "},";
        } 
        m = m + "\"impuestoNeto\":\"" + procesarNumeros(impuestoNeto[i].toString(), "#0.00") + "\",";
        m = m + "\"montoTotalLinea\":\"" + procesarNumeros(totalLinea[i].toString(), "#0.00") + "\"";
        m = m + "},";
        factura.addFEItemFactura(item);
      } 
      j = j + m.substring(0, m.length() - 1);
      j = j + "},";
      String[] otrosCargosTipoDoc = request.getParameterValues("otrosCargosTipoDoc[]");
      String[] otrosCargosIdentificacion = request.getParameterValues("otrosCargosIdentificacion[]");
      String[] otrosCargosNombre = request.getParameterValues("otrosCargosNombre[]");
      String[] otrosCargosDetalle = request.getParameterValues("otrosCargosDetalle[]");
      String[] otrosCargosPorcentaje = request.getParameterValues("otrosCargosPorcentaje[]");
      String[] otrosCargosMonto = request.getParameterValues("otrosCargosMonto[]");
      String oc = "";
      if (otrosCargosTipoDoc != null && otrosCargosTipoDoc.length > 0) {
        oc = oc + "\"otrosCargos\":{";
        String coma2 = "";
        for (int k = 0; k < otrosCargosTipoDoc.length; k++) {
          if (k + 1 == otrosCargosTipoDoc.length) {
            coma2 = "";
          } else {
            coma2 = ",";
          } 
          oc = oc + "\"" + k + "\":{";
          oc = oc + "\"tipoDocumento\":\"" + otrosCargosTipoDoc[k].toString() + "\",";
          oc = oc + "\"numeroIdentidadTercero\":\"" + otrosCargosIdentificacion[k].toString() + "\",";
          oc = oc + "\"nombreTercero\":\"" + otrosCargosNombre[k].toString() + "\",";
          oc = oc + "\"detalle\":\"" + otrosCargosDetalle[k].toString() + "\",";
          oc = oc + "\"porcentaje\":\"" + procesarTexto(otrosCargosPorcentaje[k].toString()) + "\",";
          oc = oc + "\"montoCargo\":\"" + procesarNumeros(otrosCargosMonto[k].toString(), "#0.00") + "\"";
          oc = oc + "}" + coma2;
          FEFacturaOtrosCargos foc = new FEFacturaOtrosCargos();
          foc.setTipoDocumento(otrosCargosTipoDoc[k].toString());
          foc.setNumeroIdentidadTercero(otrosCargosIdentificacion[k].toString());
          foc.setNombreTercero(otrosCargosNombre[k].toString());
          foc.setDetalle(procesarTexto(otrosCargosDetalle[k].toString()));
          foc.setPorcentaje(procesarTexto(otrosCargosPorcentaje[k].toString()));
          foc.setMontoCargo(procesarNumeros(otrosCargosMonto[k].toString(), "#0.00"));
          factura.addOtrosCargos(foc);
        } 
        oc = oc + "},";
      } 
      j = j + oc;
      String[] referenciaTipoDoc = request.getParameterValues("referenciaTipoDoc[]");
      String[] referenciaClaveDocumento = request.getParameterValues("referenciaClaveDocumento[]");
      String[] referenciaFechaDeEmision = request.getParameterValues("referenciaFechaDeEmision[]");
      String[] referenciaCodigoDocumento = request.getParameterValues("referenciaCodigoDocumento[]");
      String[] referenciaRazonDocumento = request.getParameterValues("referenciaRazonDocumento[]");
      String r = "";
      if (referenciaClaveDocumento != null && referenciaClaveDocumento.length > 0) {
        r = r + "\"referencias\":{";
        String coma2 = "";
        for (int k = 0; k < referenciaClaveDocumento.length; k++) {
          if (k + 1 == referenciaClaveDocumento.length) {
            coma2 = "";
          } else {
            coma2 = ",";
          } 
          String fechaReferencia = formatDate.format((new SimpleDateFormat("dd/MM/yyyy")).parse(referenciaFechaDeEmision[k].toString())) + "-06:00";
          r = r + "\"" + k + "\":{";
          r = r + "\"tipoDoc\":\"" + referenciaTipoDoc[k].toString() + "\",";
          r = r + "\"numero\":\"" + referenciaClaveDocumento[k].toString() + "\",";
          r = r + "\"fechaEmision\":\"" + fechaReferencia + "\",";
          r = r + "\"codigo\":\"" + referenciaCodigoDocumento[k].toString() + "\",";
          r = r + "\"razon\":\"" + procesarTexto(referenciaRazonDocumento[k].toString()) + "\"";
          r = r + "}" + coma2;
          FEFacturaReferencia fr = new FEFacturaReferencia();
          CCodigoReferencia codigoReferencia = this._codigoReferenciaService.findById(Long.valueOf(Long.parseLong(referenciaCodigoDocumento[k].toString())));
          fr.setTipoDoc(referenciaTipoDoc[k].toString());
          fr.setNumero(referenciaClaveDocumento[k].toString());
          fr.setFechaEmision(fechaReferencia);
          fr.setCodigoReferencia(codigoReferencia);
          fr.setRazon(referenciaRazonDocumento[k].toString());
          factura.addReferenciaFactura(fr);
        } 
        r = r + "},";
      } 
      j = j + r;
      j = j + "\"codMoneda\":\"" + moneda.getSimboloMoneda() + "\",";
      j = j + "\"tipoCambio\":\"" + procesarNumeros(request.getParameter("tipoCambio"), "#0.00") + "\",";
      j = j + "\"totalServGravados\":\"" + procesarNumeros(request.getParameter("totalServiciosGravados"), "#0.00") + "\",";
      j = j + "\"totalServExentos\":\"" + procesarNumeros(request.getParameter("totalServiciosExentos"), "#0.00") + "\",";
      if (!tipoDocumento.equals("FEE"))
        j = j + "\"totalServExonerado\":\"" + procesarNumeros(request.getParameter("totalServiciosExonerado"), "#0.00") + "\","; 
      j = j + "\"totalMercGravadas\":\"" + procesarNumeros(request.getParameter("totalMercanciaGravadas"), "#0.00") + "\",";
      j = j + "\"totalMercExentas\":\"" + procesarNumeros(request.getParameter("totalMercanciasExentas"), "#0.00") + "\",";
      if (!tipoDocumento.equals("FEE"))
        j = j + "\"totalMercExonerada\":\"" + procesarNumeros(request.getParameter("totalMercanciasExonerada"), "#0.00") + "\","; 
      j = j + "\"totalGravados\":\"" + procesarNumeros(request.getParameter("totalGravados"), "#0.00") + "\",";
      j = j + "\"totalExentos\":\"" + procesarNumeros(request.getParameter("totalExentos"), "#0.00") + "\",";
      j = j + "\"totalExonerado\":\"" + procesarNumeros(request.getParameter("totalExonerado"), "#0.00") + "\",";
      j = j + "\"totalVentas\":\"" + procesarNumeros(request.getParameter("totalVenta"), "#0.00") + "\",";
      j = j + "\"totalDescuentos\":\"" + procesarNumeros(request.getParameter("descuentos"), "#0.00") + "\",";
      j = j + "\"totalVentasNeta\":\"" + procesarNumeros(request.getParameter("totalVentaNeta"), "#0.00") + "\",";
      j = j + "\"totalImp\":\"" + procesarNumeros(request.getParameter("totalImpuestos"), "#0.00") + "\",";
      if (!tipoDocumento.equals("FEE") && !tipoDocumento.equals("FEC"))
        j = j + "\"totalIVADevuelto\":\"" + procesarNumeros(request.getParameter("totalIVADevuelto"), "#0.00") + "\","; 
      j = j + "\"totalOtrosCargos\":\"" + procesarNumeros(request.getParameter("totalOtrosCargos"), "#0.00") + "\",";
      j = j + "\"totalComprobante\":\"" + procesarNumeros(request.getParameter("totalFactura"), "#0.00") + "\",";
      j = j + "\"otros\":\"" + procesarTexto(request.getParameter("notaFactura")) + "\",";
      j = j + "\"numeroFactura\":\"" + factura.getNumeroFactura() + "\"";
      j = j + "}";
      if (!tipoDocumento.equalsIgnoreCase("PR")) {
        this.log.info("JSON GENERADO ===================> " + j);
        String json = j;
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setEntity((HttpEntity)entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        responseApi = client.execute((HttpUriRequest)httpPost);
        entity2 = responseApi.getEntity();
        String responseString = EntityUtils.toString(entity2, "UTF-8");
        objectMapper = new ObjectMapper();
        data = objectMapper.readTree(responseString);
      } 
      factura.setCondVenta(request.getParameter("condVenta"));
      factura.setClienteContado(procesarTexto(request.getParameter("inputClienteContado")));
      factura.setPlazoCredito(request.getParameter("plazoCredito"));
      factura.setMedioPago(medioPago);
      factura.setMoneda(moneda);
      factura.setTipoCambio(Double.valueOf(Double.parseDouble(request.getParameter("tipoCambio"))));
      factura.setTipoCambioDolar(Double.valueOf(Double.parseDouble(request.getParameter("tp-2"))));
      factura.setTipoCambioEuro(Double.valueOf(Double.parseDouble(request.getParameter("tp-3"))));
      factura.setTotalServGravados(Double.valueOf(Double.parseDouble(request.getParameter("totalServiciosGravados"))));
      factura.setTotalServExentos(Double.valueOf(Double.parseDouble(request.getParameter("totalServiciosExentos"))));
      factura.setTotalServExonerado(Double.valueOf(Double.parseDouble(request.getParameter("totalServiciosExonerado"))));
      factura.setTotalMercGravadas(Double.valueOf(Double.parseDouble(request.getParameter("totalMercanciaGravadas"))));
      factura.setTotalMercExentas(Double.valueOf(Double.parseDouble(request.getParameter("totalMercanciasExentas"))));
      factura.setTotalMercExonerada(Double.valueOf(Double.parseDouble(request.getParameter("totalMercanciasExonerada"))));
      factura.setTotalGravados(Double.valueOf(Double.parseDouble(request.getParameter("totalGravados"))));
      factura.setTotalExentos(Double.valueOf(Double.parseDouble(request.getParameter("totalExentos"))));
      factura.setTotalExonerado(Double.valueOf(Double.parseDouble(request.getParameter("totalExonerado"))));
      factura.setTotalVentas(Double.valueOf(Double.parseDouble(request.getParameter("totalVenta"))));
      factura.setTotalDescuentos(Double.valueOf(Double.parseDouble(request.getParameter("descuentos"))));
      factura.setTotalVentaNeta(Double.valueOf(Double.parseDouble(request.getParameter("totalVentaNeta"))));
      factura.setTotalImp(Double.valueOf(Double.parseDouble(request.getParameter("totalImpuestos"))));
      if (request.getParameter("totalIVADevuelto") != null && request.getParameter("totalIVADevuelto").length() > 0 && Double.parseDouble(request.getParameter("totalIVADevuelto")) > 0.0D) {
        factura.setTotalIVADevuelto(Double.valueOf(Double.parseDouble(request.getParameter("totalIVADevuelto"))));
      } else {
        factura.setTotalIVADevuelto(Double.valueOf(0.0D));
      } 
      factura.setTotalOtrosCargos(Double.valueOf(Double.parseDouble(request.getParameter("totalOtrosCargos"))));
      factura.setTotalComprobante(Double.valueOf(Double.parseDouble(request.getParameter("totalFactura"))));
      factura.setOtros(request.getParameter("notaFactura"));
      factura.setNumeroTarjeta(procesarTexto(request.getParameter("numeroTarjeta")));
      factura.setNumeroAutorizacion(procesarTexto(request.getParameter("numeroAutorizacion")));
      if (tipoDocumento.equalsIgnoreCase("PR")) {
        factura.setClave("");
        factura.setFechaEmision(format.format(new Date()));
        factura.setConsecutivo("");
      } else {
        factura.setClave(data.path("clave").asText());
        factura.setFechaEmision(data.path("fechaEmision").asText());
        factura.setConsecutivo(data.path("consecutivo").asText());
      } 
      factura.setEfectivo(Double.valueOf((request.getParameter("medioPago1") != null) ? Double.parseDouble(request.getParameter("medioPago1")) : 0.0D));
      factura.setTarjeta(Double.valueOf((request.getParameter("medioPago2") != null) ? Double.parseDouble(request.getParameter("medioPago2")) : 0.0D));
      factura.setNumeroTarjeta(procesarTexto(request.getParameter("numeroTarjeta")));
      factura.setNumeroAutorizacion(procesarTexto(request.getParameter("numeroAutorizacion")));
      factura.setCheque(Double.valueOf((request.getParameter("medioPago3") != null) ? Double.parseDouble(request.getParameter("medioPago3")) : 0.0D));
      factura.setTransferencia(Double.valueOf((request.getParameter("medioPago4") != null) ? Double.parseDouble(request.getParameter("medioPago4")) : 0.0D));
      factura.setSuVueltro(Double.valueOf(Double.parseDouble(request.getParameter("suVuelto"))));
      factura.setEfectivoDolares(Double.valueOf((request.getParameter("medioPagoDolar1") != null) ? Double.parseDouble(request.getParameter("medioPagoDolar1")) : 0.0D));
      factura.setTarjetaDolares(Double.valueOf((request.getParameter("medioPagoDolar2") != null) ? Double.parseDouble(request.getParameter("medioPagoDolar2")) : 0.0D));
      factura.setNumeroTarjetaDolares(procesarTexto(request.getParameter("numeroTarjetaDolar")));
      factura.setNumeroAutorizacionDolares(procesarTexto(request.getParameter("numeroAutorizacionDolar")));
      factura.setChequeDolares(Double.valueOf((request.getParameter("medioPagoDolar3") != null) ? Double.parseDouble(request.getParameter("medioPagoDolar3")) : 0.0D));
      factura.setTransferenciaDolares(Double.valueOf((request.getParameter("medioPagoDolar4") != null) ? Double.parseDouble(request.getParameter("medioPagoDolar4")) : 0.0D));
      factura.setSuVueltoDolares(Double.valueOf(Double.parseDouble(request.getParameter("suVueltoDolar"))));
      factura.setEfectivoEuros(Double.valueOf((request.getParameter("medioPagoEuro1") != null) ? Double.parseDouble(request.getParameter("medioPagoEuro1")) : 0.0D));
      factura.setTarjetaEuros(Double.valueOf((request.getParameter("medioPagoEuro2") != null) ? Double.parseDouble(request.getParameter("medioPagoEuro2")) : 0.0D));
      factura.setNumeroTarjetaEuros(procesarTexto(request.getParameter("numeroTarjetaDolar")));
      factura.setNumeroAutorizacionEuros(procesarTexto(request.getParameter("numeroAutorizacionDolar")));
      factura.setChequeEuros(Double.valueOf((request.getParameter("medioPagoEuro3") != null) ? Double.parseDouble(request.getParameter("medioPagoEuro3")) : 0.0D));
      factura.setTransferenciaEuros(Double.valueOf((request.getParameter("medioPagoEuro4") != null) ? Double.parseDouble(request.getParameter("medioPagoEuro4")) : 0.0D));
      factura.setSuVueltoEuros(Double.valueOf(Double.parseDouble(request.getParameter("suVueltoEuro"))));
      factura.setTipoTarjeta((request.getParameter("tipoTarjeta") != null) ? request.getParameter("tipoTarjeta") : "A");
      if (tipoDocumento.equalsIgnoreCase("PR")) {
        factura.setEstado("P");
      } else {
        factura.setEstado("A");
        FEBitacora feb = new FEBitacora();
        feb.setClave(data.path("clave").asText());
        feb.setTipoDocumento(request.getParameter("tipoDocumento"));
        feb.setResponseCode(data.path("response").asInt());
        feb.setNameXmlEnviado(data.path("fileXmlSign").asText());
        feb.setFechaEmision(data.path("fechaEmision").asText());
        factura.addDocumentoABitacora(feb);
      } 
      this._facturaService.save(factura);
      if (tipoDocumento.equalsIgnoreCase("PR")) {
        response.put("clave", "");
        response.put("consecutivo", "");
        response.put("fechaEmision", "");
        response.put("fileXmlSign", "");
        response.put("msj", "");
        response.put("factura_id", factura.getId());
        response.put("response", Integer.valueOf(200));
      } else {
        if (request.getParameter("condVenta") != null && request.getParameter("condVenta").equals("2") && !tipoDocumento.equalsIgnoreCase("NC")) {
          cxc.setNumeroFactura(factura.getNumeroFactura());
          cxc.setTotalDeuda(Double.parseDouble(request.getParameter("totalFactura")));
          cxc.setMoneda(factura.getMoneda().getSimboloMoneda());
          cxc.setClave(factura.getClave());
          cxc.setEmisor(emisor);
          cxc.setFechaEmisionFe(factura.getFechaEmisionFe());
          cxc.setUsuario(usuario);
          cxc.setEstadoPago("A");
          Calendar c = Calendar.getInstance();
          c.setTime(factura.getFechaEmisionFe());
          Double plazoCredito = Double.valueOf(Double.parseDouble(factura.getPlazoCredito()));
          cxc.setPlazoCredito(plazoCredito.intValue() + "");
          if (factura.getPlazoCredito() != null) {
            c.add(5, plazoCredito.intValue());
          } else {
            c.add(5, 0);
          } 
          cxc.setFechaVencimientoFe(c.getTime());
          this._cxcService.save(cxc);
        } 
        if (request.getParameter("condVenta") != null && request.getParameter("condVenta").equals("2") && tipoDocumento.equalsIgnoreCase("NC")) {
          FEFacturaRegistroPagosCXC facturaRegistroPagosCXC = new FEFacturaRegistroPagosCXC();
          facturaRegistroPagosCXC.setUsuario(usuario);
          facturaRegistroPagosCXC.setFechaRegistroAbondo(new Date());
          facturaRegistroPagosCXC.setFechaPagoAbondo(new Date());
          facturaRegistroPagosCXC.setEmisor(emisor);
          facturaRegistroPagosCXC.setCliente(cliente);
          FEFacturasCXC facturaCXC = this._cxcFacturasService.obtenerIdFECXCPorClave(request.getParameter("claveFeOld"));
          if (facturaCXC != null) {
            facturaRegistroPagosCXC.setFacturaCXC(facturaCXC);
            facturaRegistroPagosCXC.setMontoAbono(factura.getTotalComprobante().doubleValue());
            facturaRegistroPagosCXC.setMontoActual(facturaCXC.getDeudaActualCXC().doubleValue() - Double.parseDouble(request.getParameter("totalFactura")));
            facturaRegistroPagosCXC.setMedioDePago(this._medioDePagoService.findById(Long.valueOf(100L)));
            facturaRegistroPagosCXC.setNumDocumento(factura.getConsecutivo());
            FEFacturaRegistroPagosCXC numeroCXC = this._cxcRegistroPagosService.findMaxFacturaByEmisor(emisor.getId());
            if (numeroCXC != null) {
              facturaRegistroPagosCXC.setNumeroCXC(Long.valueOf(numeroCXC.getNumeroCXC().longValue() + 1L));
            } else {
              facturaRegistroPagosCXC.setNumeroCXC(Long.valueOf(Long.parseLong("1")));
            } 
            this._cxcRegistroPagosService.save(facturaRegistroPagosCXC);
            this.log.info("Cancelando la factura con una nota de crédito: " + facturaCXC.getDeudaActualCXC() + " " + Double.parseDouble(request.getParameter("totalFactura")));
            if (facturaCXC.getDeudaActualCXC().doubleValue() == Double.parseDouble(request.getParameter("totalFactura")))
              this._cxcFacturasService.modificaEstadoFacturaCXC("C", emisor.getId(), facturaCXC.getId()); 
          } 
        } 
        for (int k = 0; k < itemId.length; k++) {
          Producto producto = this._productoService.findById(Long.valueOf(Long.parseLong(itemId[k].toString())));
          if (producto.getAfectaInventario().equals("1")) {
            cantidadArticulos = Double.valueOf(0.0D);
            try {
              if (!tipoVentaLinea[k].isEmpty() && tipoVentaLinea[k].toString().equals("F")) {
                _fraccionesPorUnidad = fraccionesPorUnidad[k];
                try {
                  if (Double.parseDouble(cantidad[k].toString()) > 0.0D)
                    cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[k].toString()) * _fraccionesPorUnidad.doubleValue()); 
                } catch (Exception e) {
                  e.printStackTrace();
                } 
                try {
                  if (fraccion[k].doubleValue() > 0.0D)
                    cantidadArticulos = Double.valueOf(cantidadArticulos.doubleValue() + fraccion[k].doubleValue()); 
                } catch (Exception e) {
                  e.printStackTrace();
                } 
              } else {
                cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[k].toString()));
              } 
            } catch (Exception e) {
              cantidadArticulos = Double.valueOf(Double.parseDouble(cantidad[k].toString()));
            } 
            if (tipoDocumento.equalsIgnoreCase("NC")) {
              this._productoService.registroEntradasInvent(cantidadArticulos, producto.getId());
            } else if (!tipoDocumento.equalsIgnoreCase("FEC")) {
              this._productoService.registroSalidasInvent(cantidadArticulos, producto.getId());
            } 
          } 
        } 
        try {
          this.jdbcTemplate = new JdbcTemplate(this.dataSource);
          SimpleJdbcCall simpleJdbcCall = (new SimpleJdbcCall(this.jdbcTemplate)).withProcedureName("spSegregaImpuestosVentasPorFactura");
          Map<String, Object> parameters = new HashMap<>();
          parameters.put("_factura_id_", factura.getId());
          MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(parameters);
          Map<String, Object> returnSp = simpleJdbcCall.execute((SqlParameterSource)mapSqlParameterSource);
          if (returnSp.get("response").toString().equalsIgnoreCase("1"));
        } catch (Exception e) {
          e.printStackTrace();
        } 
        response.put("clave", data.path("clave").asText());
        response.put("consecutivo", data.path("consecutivo").asText());
        response.put("fechaEmision", data.path("fechaEmision").asText());
        response.put("fileXmlSign", data.path("fileXmlSign").asText());
        response.put("msj", "");
        response.put("response", Integer.valueOf(200));
      } 
    } catch (Exception e) {
      e.printStackTrace();
      response.put("clave", "");
      response.put("consecutivo", "");
      response.put("fechaEmision", "");
      response.put("fileXmlSign", "");
      response.put("msj", procesarTexto(e.getMessage()));
      response.put("response", Integer.valueOf(401));
    } finally {
      if (responseApi != null)
        responseApi.close(); 
      entity2 = null;
      objectMapper = null;
      data = null;
    } 
    return new ResponseEntity(response, HttpStatus.CREATED);
  }
  
  @PostMapping(value = {"/enviar-proforma"}, produces = {"application/json"})
  public ResponseEntity<?> enviarProforma(@RequestParam(name = "correo", required = false) String email, @RequestParam(name = "factura", required = true) Long facturaId, HttpSession session) throws JRException, IOException, SQLException, MessagingException {
    Map<String, Object> response = new HashMap<>();
    String emailEnviar = "";
    Connection db = this.dataSource.getConnection();
    InputStream reportfile = getClass().getResourceAsStream("/proforma/facturas.jasper");
    URL base = getClass().getResource("/proforma/");
    String baseUrl = base.toString();
    try {
      if (facturaId != null && facturaId.longValue() > 0L) {
        Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
        FEFactura factura = this._facturaService.findByEmisorById(emisorId, facturaId);
        if (factura != null) {
          System.out.println("correo " + factura.getCliente().getCorreo1());
          if (email != null && email.length() > 0) {
            emailEnviar = email;
          } else {
            emailEnviar = factura.getCliente().getCorreo1();
          } 
          Emisor e = this._emisorService.findById(emisorId);
          String td = tipoDocumento(factura.getTipoDocumento());
          String logo = e.getLogoEmpresa();
          if (logo != null && !logo.equals("") && logo.length() > 0) {
            logo = this.pathUploadFilesApi + "logo/" + logo;
          } else {
            logo = this.pathUploadFilesApi + "logo/default.png";
          } 
          Map<String, Object> parameter = new HashMap<>();
          parameter.put("BASE_URL", baseUrl);
          parameter.put("BASE_URL_LOGO", logo);
          parameter.put("CLAVE_FACTURA", factura.getId());
          parameter.put("TIPO_DOCUMENTO", tipoDocumento(factura.getTipoDocumento()));
          parameter.put("RESOLUCION", factura.getEmisor().getNotaValidezProforma());
          parameter.put("NOTA_FACTURA", factura.getOtros());
          parameter.put("URL_QR", this.urlQr + factura.getId());
          byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
          if (bytes != null && bytes.length > 0) {
            ByteArrayDataSource pdfBytes = new ByteArrayDataSource(bytes, "application/pdf");
            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            String msj = null;
            String clienteFinal = (factura.getCliente() != null && factura.getCliente().getNombreCompleto().length() > 0) ? factura.getCliente().getNombreCompleto() : "";
            msj = "<p style=\"font-family: Arial;\">Estimado cliente,</p>";
            msj = msj + "<p style=\"font-family: Arial;\">Le hacemos llegar el comprobante de <b>" + td + "</b> con el número de consecutivo <b>" + factura.getNumeroFactura() + "</b>, generada por <b>" + e.getNombreRazonSocial() + "</b></p><p><b><i>" + e.getNotaValidezProforma() + "</i></b></p>";
            msj = msj + "<p style=\"font-family: Arial;\">Saludos,</p>";
            msj = msj + "<p style=\"font-family: Arial;\"><b>" + e.getNombreComercial() + "</b></p>";
            helper.setTo(new InternetAddress(emailEnviar, clienteFinal));
            helper.setReplyTo(e.getEmail(), e.getNombreComercial());
            helper.setFrom(new InternetAddress(this.correoDistribucion, td));
            helper.setSubject(td + " - " + e.getNombreComercial());
            helper.setText(msj, true);
            helper.addAttachment("factura-proforma-numero-" + factura.getNumeroFactura() + ".pdf", (javax.activation.DataSource)pdfBytes);
            this.emailSender.send(message);
            response.put("msj", "Se envío un correo con la factura a: " + emailEnviar);
            response.put("response", Integer.valueOf(200));
            return new ResponseEntity(response, HttpStatus.CREATED);
          } 
          response.put("msj", "Error contacte al desarrollador del sistema.");
          response.put("response", Integer.valueOf(200));
          return new ResponseEntity(response, HttpStatus.CREATED);
        } 
        response.put("msj", "El documento que desea reenviar no existe.");
        response.put("response", Integer.valueOf(200));
        return new ResponseEntity(response, HttpStatus.CREATED);
      } 
      response.put("msj", "El número de factura es requerido!!!");
      response.put("response", Integer.valueOf(200));
      return new ResponseEntity(response, HttpStatus.CREATED);
    } catch (Exception ex) {
      response.put("msj", "Error al intentar enviar el correo a " + email + ", error generado: " + ex
          .getMessage());
      response.put("response", Integer.valueOf(204));
      return new ResponseEntity(response, HttpStatus.NO_CONTENT);
    } finally {
      reportfile.close();
      try {
        if (db != null)
          db.close(); 
      } catch (SQLException e) {
        System.out.println("Error: desconectando la base de datos.");
      } 
    } 
  }
  
  public String procesarNumeros(String j, String decimales) {
    NumberFormat formatter = new DecimalFormat(decimales);
    String r = "";
    r = (j != null && !j.equals("")) ? j : "0.00000";
    r = formatter.format(Double.parseDouble(r));
    r = r.replaceAll(",", ".");
    return r;
  }
  
  public String procesarTexto(String j) {
    String r = "";
    r = StringEscapeUtils.escapeJson(j);
    return r;
  }
  
  public String procesarTextoInput(String j) {
    String r = "";
    r = StringEscapeUtils.escapeJava(j);
    return r;
  }
  
  public String tipoDocumento(String td) {
    String resp = "";
    switch (td) {
      case "FE":
        resp = "Factura Electrónica";
        break;
      case "ND":
        resp = "Nota de débito Electrónica";
        break;
      case "NC":
        resp = "Nota de crédito Electrónica";
        break;
      case "TE":
        resp = "Tiquete Electrónico";
        break;
      case "FEC":
        resp = "Factura Electrónicade Compra";
        break;
      case "FEE":
        resp = "Factura Electrónicade Exportación";
        break;
      case "PR":
        resp = "Factura Proforma";
        break;
    } 
    return resp;
  }
}



