package com.samyx.controllers;

import com.samyx.models.entity.CCodigoReferencia;
import com.samyx.models.entity.CCodigosTarifasIva;
import com.samyx.models.entity.CCondicionDeVenta;
import com.samyx.models.entity.CImpuesto;
import com.samyx.models.entity.CMedioDePago;
import com.samyx.models.entity.COtrosCargosTipoDocumento;
import com.samyx.models.entity.CProvincia;
import com.samyx.models.entity.CTipoDeCambio;
import com.samyx.models.entity.CTipoDeDocumentoReferencia;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.models.entity.CTipoDocumentoExoneracionOAutorizacion;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.UnidadDeMedida;
import com.samyx.models.entity.Usuario;
import com.samyx.service.impl.COtrosCargosTipoDocumentoImpl;
import com.samyx.service.interfaces.ICCodigoReferenciaService;
import com.samyx.service.interfaces.ICCodigosTarifasIvaService;
import com.samyx.service.interfaces.ICCondicionDeVentaService;
import com.samyx.service.interfaces.ICImpuestoService;
import com.samyx.service.interfaces.ICMedioDePagoService;
import com.samyx.service.interfaces.ICProvinciaService;
import com.samyx.service.interfaces.ICTipoDeCambioService;
import com.samyx.service.interfaces.ICTipoDeDocumentoReferenciaService;
import com.samyx.service.interfaces.ICTipoDeIdentificacionService;
import com.samyx.service.interfaces.ICTipoDocumentoExoneracionOAutorizacionService;
import com.samyx.service.interfaces.IControlCajaService;
import com.samyx.service.interfaces.IEmisorActividadesService;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.service.interfaces.IUnidadDeMedidaService;
import com.samyx.service.interfaces.IUsuarioService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/facturar"})
public class FacturadorController {
  @Autowired
  private IMonedaService _monedaService;
  
  @Autowired
  private ICCondicionDeVentaService _condicionDeVentaService;
  
  @Autowired
  private IUnidadDeMedidaService _unidadDeMedidaService;
  
  @Autowired
  private ICMedioDePagoService _medioDePagoService;
  
  @Autowired
  private ICImpuestoService _impuestoService;
  
  @Autowired
  private ICCodigoReferenciaService _codigoReferenciaService;
  
  @Autowired
  private IUsuarioService _usuarioService;
  
  @Autowired
  private ICTipoDeCambioService _tipoDeCambioService;
  
  @Autowired
  private ICTipoDeIdentificacionService _tipoDeIdentificacionService;
  
  @Autowired
  private COtrosCargosTipoDocumentoImpl _otorsCargosService;
  
  @Autowired
  private ICProvinciaService _provinciaService;
  
  @Autowired
  public ICTipoDeDocumentoReferenciaService tipoDocReferenciaService;
  
  @Autowired
  public IEmisorActividadesService _actividadesService;
  
  @Autowired
  private IControlCajaService _cajaService;
  
  @Autowired
  private ICTipoDocumentoExoneracionOAutorizacionService _exoneraTipoDocService;
  
  @Autowired
  private ICCodigosTarifasIvaService _codigosTarifasIvaService;
  
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  @Value("${api.jmata.recepcion}")
  private String apiJmataRecepcion;
  
  @GetMapping({"/"})
  public String form(Model model, HttpSession session, Authentication auth, @RequestParam(name = "f", defaultValue = "") String f, HttpServletRequest request) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      if (session.getAttribute("SESSION_SUCURSAL_ID") != null && session.getAttribute("SESSION_TERMINAL_ID") != null) {
        Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
        Usuario usuario = this._usuarioService.findByUsername(auth.getName());
        if (request.isUserInRole("ROLE_USER_COBRADOR"))
          if (session.getAttribute("SESSION_CONTROL_CAJAS") != null && session.getAttribute("SESSION_CONTROL_CAJAS").toString().equalsIgnoreCase("S")) {
            Integer c = this._cajaService.countByUsuarioAndEmpresa(emisorId, usuario.getId());
            if (c == null || c.intValue() <= 0)
              return "redirect:/control-cajas/form/?cajaCerrada"; 
          }  
        List<Moneda> listaMonedas = this._monedaService.findAll();
        List<UnidadDeMedida> listaUnidadDeMedida = this._unidadDeMedidaService.findAll();
        List<CCondicionDeVenta> listaCondicionDeVenta = this._condicionDeVentaService.findAll();
        List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAll();
        List<CImpuesto> listaImpuestos = this._impuestoService.findAllImpuestos();
        List<CCodigoReferencia> listaCodigoReferencia = this._codigoReferenciaService.findAll();
        List<CTipoDeCambio> listaTiposDeCambio = this._tipoDeCambioService.findAllDivisas();
        List<COtrosCargosTipoDocumento> listaTipoDocOtrosCargos = this._otorsCargosService.findAll();
        List<CTipoDeDocumentoReferencia> listaTipoDocReferencia = this.tipoDocReferenciaService.findAll();
        List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
        List<CTipoDocumentoExoneracionOAutorizacion> listaTipoDocsExonera = this._exoneraTipoDocService.findAll();
        List<CCodigosTarifasIva> listaCodigosTarifasIva = this._codigosTarifasIvaService.findAll();
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
        model.addAttribute("listaImpuestos", listaImpuestos);
        model.addAttribute("listaCodigosTarifasIva", listaCodigosTarifasIva);
        model.addAttribute("URL_TIPO_DOCUMENTO", "/facturar/emitir-factura");
        model.addAttribute("SESSION_CONTROL_IMPRESION", Character.valueOf((char) (session.getAttribute("SESSION_CONTROL_IMPRESION").toString().equals("1") ? 67 : 84)));
        if (f != null && f.equalsIgnoreCase("all")) {
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
          return "/facturacion/facturador_avanzado";
        } 
        model.addAttribute("V_FACTURADOR", "V2");
        return "/facturacion/facturador";
      } 
      return "redirect:/proformas/seleccionar-terminal?urlRetorno=/facturar/?f=" + f;
    } 
    return "redirect:/";
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
}

