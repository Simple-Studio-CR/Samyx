package com.samyx.controllers;

import com.samyx.models.entity.CCondicionDeVenta;
import com.samyx.models.entity.CMedioDePago;
import com.samyx.models.entity.CTipoDeCambio;
import com.samyx.models.entity.Cliente;
import com.samyx.models.entity.EmisorActividades;
import com.samyx.models.entity.Moneda;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.ICCondicionDeVentaService;
import com.samyx.service.interfaces.ICMedioDePagoService;
import com.samyx.service.interfaces.ICTerminalUsuarioService;
import com.samyx.service.interfaces.ICTipoDeCambioService;
import com.samyx.service.interfaces.IClienteService;
import com.samyx.service.interfaces.IEmisorActividadesService;
import com.samyx.service.interfaces.IEmpresaUsuarioAccesoService;
import com.samyx.service.interfaces.IMonedaService;
import com.samyx.service.interfaces.IUsuarioService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.ReportExportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.XlsxReportConfiguration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@Controller
@RequestMapping({"/reportes"})
public class ReportesController {
  @Autowired
  public DataSource dataSource;
  
  private JdbcTemplate jdbcTemplate;
  
  @Autowired
  private IClienteService _clienteService;
  
  @Autowired
  private ICCondicionDeVentaService _condicionDeVentaService;
  
  @Autowired
  private ICMedioDePagoService _medioDePagoService;
  
  @Autowired
  private IMonedaService _monedaService;
  
  @Autowired
  private IEmpresaUsuarioAccesoService _empresaUsuarioAccesoService;
  
  @Autowired
  private ICTerminalUsuarioService _terminalUsuarioService;
  
  @Autowired
  public IEmisorActividadesService _actividadesService;
  
  @Autowired
  private IUsuarioService _usuarioService;
  
  @Autowired
  private ICTipoDeCambioService _tipoDeCambioService;
  
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  @GetMapping({"/ventas"})
  public String ventas(Model model, HttpSession session, Authentication auth) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
      List<CCondicionDeVenta> listaCondicionDeVenta = this._condicionDeVentaService.findAll();
      List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAll();
      List<Moneda> listaMonedas = this._monedaService.findAll();
      List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
      Usuario usuario = this._usuarioService.findByUsername(auth.getName());
      model.addAttribute("listaSucursales", this._terminalUsuarioService.findAllByEmisorAndSucursal(emisorId, usuario.getId()));
      model.addAttribute("listUsuarios", this._empresaUsuarioAccesoService.findUsuariosByEmpresa(emisorId));
      model.addAttribute("listaActividades", listaActividades);
      model.addAttribute("listaCondicionDeVenta", listaCondicionDeVenta);
      model.addAttribute("listaMedioDePago", listaMedioDePago);
      model.addAttribute("listaMonedas", listaMonedas);
      return "/reportes/ventas/index";
    } 
    return "redirect:/";
  }
  
  @PostMapping({"/ventas"})
  public View ventasExcel(Model model, HttpServletRequest request, final HttpSession session, @RequestParam String cr, @RequestParam final String f1, @RequestParam final String f2, @RequestParam final String td, @RequestParam final String e, @RequestParam final String cv, @RequestParam final String mp, @RequestParam final String m, @RequestParam final String actividad, @RequestParam(name = "user", defaultValue = "0") final Long user, @RequestParam(name = "vendedor", defaultValue = "0") final Long vendedor, @RequestParam(name = "sucursal", defaultValue = "0") final Long sucursal, @RequestParam(name = "terminal", defaultValue = "0") final Long terminal, @RequestParam final String sr) {
    return (View)new AbstractXlsxView() {
        protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
          if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
            response.setContentType("application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"reporte-de-ventas.xlsx\"");
            String nombreHoja = "Reporte de ventas";
            Sheet sheet = workbook.createSheet(nombreHoja);
            Cell cell = null;
            Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
            XSSFFont font = (XSSFFont)workbook.createFont();
            XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
            XSSFCellStyle styleBackground = (XSSFCellStyle)workbook.createCellStyle();
            styleBackground.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            styleBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setBold(true);
            style.setFont((Font)font);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Número de factura");
            header.createCell(1).setCellValue("Cliente");
            header.createCell(2).setCellValue("Tipo Identificación");
            header.createCell(3).setCellValue("Identificación");
            header.createCell(4).setCellValue("Tipo de documento");
            header.createCell(5).setCellValue("Clave");
            header.createCell(6).setCellValue("Consecutivo");
            header.createCell(7).setCellValue("Fecha emisión");
            header.createCell(8).setCellValue("Condición venta");
            header.createCell(9).setCellValue("Plazo de Crédito");
            header.createCell(10).setCellValue("Medio de pago");
            header.createCell(11).setCellValue("Moneda");
            header.createCell(12).setCellValue("Convertido a ");
            header.createCell(13).setCellValue("Tipo de cambio");
            header.createCell(14).setCellValue("Monto exento");
            header.createCell(15).setCellValue("No sujeto");
            header.createCell(16).setCellValue("IVA reducida 1%");
            header.createCell(17).setCellValue("Monto reducida 1%");
            header.createCell(18).setCellValue("IVA reducida 2%");
            header.createCell(19).setCellValue("Monto reducida 2%");
            header.createCell(20).setCellValue("IVA reducida 4%");
            header.createCell(21).setCellValue("Monto reducida 4%");
            header.createCell(22).setCellValue("IVA Transitorio 0%");
            header.createCell(23).setCellValue("Monto Transitorio 0%");
            header.createCell(24).setCellValue("IVA Transitorio 4%");
            header.createCell(25).setCellValue("Monto Transitorio 4%");
            header.createCell(26).setCellValue("IVA Transitorio 8%");
            header.createCell(27).setCellValue("Monto Transitorio 8%");
            header.createCell(28).setCellValue("IVA Tarifa general 13%");
            header.createCell(29).setCellValue("Monto Tarifa general 13%");
            header.createCell(30).setCellValue("Total serv. gravados");
            header.createCell(31).setCellValue("Total serv. exentos");
            header.createCell(32).setCellValue("Total ser. exonerado");
            header.createCell(33).setCellValue("Total mer gravadas");
            header.createCell(34).setCellValue("Total mer exenta");
            header.createCell(35).setCellValue("Total mer exonerada");
            header.createCell(36).setCellValue("Total gravados");
            header.createCell(37).setCellValue("Total exentos");
            header.createCell(38).setCellValue("Total exonerado");
            header.createCell(39).setCellValue("Total Venta");
            header.createCell(40).setCellValue("Descuentos");
            header.createCell(41).setCellValue("Total venta neta");
            header.createCell(42).setCellValue("Total impuestos");
            header.createCell(43).setCellValue("Total IVA devuelto");
            header.createCell(44).setCellValue("Total otros cargos");
            header.createCell(45).setCellValue("Total Comprobante");
            header.createCell(46).setCellValue("Código actividad económica");
            header.getCell(0).setCellStyle((CellStyle)style);
            header.getCell(1).setCellStyle((CellStyle)style);
            header.getCell(2).setCellStyle((CellStyle)style);
            header.getCell(3).setCellStyle((CellStyle)style);
            header.getCell(4).setCellStyle((CellStyle)style);
            header.getCell(5).setCellStyle((CellStyle)style);
            header.getCell(6).setCellStyle((CellStyle)style);
            header.getCell(7).setCellStyle((CellStyle)style);
            header.getCell(8).setCellStyle((CellStyle)style);
            header.getCell(9).setCellStyle((CellStyle)style);
            header.getCell(10).setCellStyle((CellStyle)style);
            header.getCell(11).setCellStyle((CellStyle)style);
            header.getCell(12).setCellStyle((CellStyle)style);
            header.getCell(13).setCellStyle((CellStyle)style);
            header.getCell(14).setCellStyle((CellStyle)style);
            header.getCell(15).setCellStyle((CellStyle)style);
            header.getCell(16).setCellStyle((CellStyle)style);
            header.getCell(17).setCellStyle((CellStyle)style);
            header.getCell(18).setCellStyle((CellStyle)style);
            header.getCell(19).setCellStyle((CellStyle)style);
            header.getCell(20).setCellStyle((CellStyle)style);
            header.getCell(21).setCellStyle((CellStyle)style);
            header.getCell(22).setCellStyle((CellStyle)style);
            header.getCell(23).setCellStyle((CellStyle)style);
            header.getCell(24).setCellStyle((CellStyle)style);
            header.getCell(25).setCellStyle((CellStyle)style);
            header.getCell(26).setCellStyle((CellStyle)style);
            header.getCell(27).setCellStyle((CellStyle)style);
            header.getCell(28).setCellStyle((CellStyle)style);
            header.getCell(29).setCellStyle((CellStyle)style);
            header.getCell(30).setCellStyle((CellStyle)style);
            header.getCell(31).setCellStyle((CellStyle)style);
            header.getCell(32).setCellStyle((CellStyle)style);
            header.getCell(33).setCellStyle((CellStyle)style);
            header.getCell(34).setCellStyle((CellStyle)style);
            header.getCell(35).setCellStyle((CellStyle)style);
            header.getCell(36).setCellStyle((CellStyle)style);
            header.getCell(37).setCellStyle((CellStyle)style);
            header.getCell(38).setCellStyle((CellStyle)style);
            header.getCell(39).setCellStyle((CellStyle)style);
            header.getCell(40).setCellStyle((CellStyle)style);
            header.getCell(41).setCellStyle((CellStyle)style);
            header.getCell(42).setCellStyle((CellStyle)style);
            header.getCell(43).setCellStyle((CellStyle)style);
            header.getCell(44).setCellStyle((CellStyle)style);
            header.getCell(45).setCellStyle((CellStyle)style);
            header.getCell(46).setCellStyle((CellStyle)style);
            Double TotalServGravados = Double.valueOf(0.0D);
            Double TotalServExentos = Double.valueOf(0.0D);
            Double TotalServExonerados = Double.valueOf(0.0D);
            Double TotalMerGravadas = Double.valueOf(0.0D);
            Double TotalMerExenta = Double.valueOf(0.0D);
            Double TotalMerExonerada = Double.valueOf(0.0D);
            Double TotalGravados = Double.valueOf(0.0D);
            Double TotalExentos = Double.valueOf(0.0D);
            Double TotalExonerado = Double.valueOf(0.0D);
            Double TotalVenta = Double.valueOf(0.0D);
            Double TotalDescuentos = Double.valueOf(0.0D);
            Double TotalVentaNeta = Double.valueOf(0.0D);
            Double TotalImpuestos = Double.valueOf(0.0D);
            Double TotalIvaDevuelto = Double.valueOf(0.0D);
            Double TotalOtrosCargos = Double.valueOf(0.0D);
            Double TotalComprobante = Double.valueOf(0.0D);
            String cliente = "";
            String clienteContado = "";
            String tipoIdentificacion = "";
            String identificacion = "";
            List<String> tipoDoc = new ArrayList<>();
            switch (td) {
              case "1":
                tipoDoc.add("FE");
                tipoDoc.add("TE");
                tipoDoc.add("FEE");
                tipoDoc.add("NC");
                break;
              case "2":
                tipoDoc.add("FE");
                break;
              case "3":
                tipoDoc.add("ND");
                break;
              case "4":
                tipoDoc.add("NC");
                break;
              case "5":
                tipoDoc.add("TE");
                break;
              case "6":
                tipoDoc.add("FEC");
                break;
              case "7":
                tipoDoc.add("FEE");
                break;
              case "8":
                tipoDoc.add("FE");
                tipoDoc.add("TE");
                tipoDoc.add("ND");
                tipoDoc.add("NC");
                tipoDoc.add("FEC");
                tipoDoc.add("FEE");
                break;
            } 
            List<String> condVenta = new ArrayList<>();
            switch (cv) {
              case "1":
                condVenta.add("1");
                break;
              case "2":
                condVenta.add("2");
                break;
              case "3":
                condVenta.add("3");
                break;
              case "4":
                condVenta.add("4");
                break;
              case "5":
                condVenta.add("5");
                break;
              case "6":
                condVenta.add("6");
                break;
              case "7":
                condVenta.add("7");
                break;
              case "8":
                condVenta.add("8");
                break;
              case "9":
                condVenta.add("9");
                break;
              case "99":
                condVenta.add("99");
                break;
              case "100":
                condVenta.add("1");
                condVenta.add("2");
                condVenta.add("3");
                condVenta.add("4");
                condVenta.add("5");
                condVenta.add("6");
                condVenta.add("7");
                condVenta.add("8");
                condVenta.add("9");
                condVenta.add("99");
                break;
            } 
            List<String> medPago = new ArrayList<>();
            switch (mp) {
              case "1":
                medPago.add("1");
                break;
              case "2":
                medPago.add("2");
                break;
              case "3":
                medPago.add("3");
                break;
              case "4":
                medPago.add("4");
                break;
              case "5":
                medPago.add("5");
                break;
              case "99":
                medPago.add("99");
                break;
              case "100":
                medPago.add("1");
                medPago.add("2");
                medPago.add("3");
                medPago.add("4");
                medPago.add("5");
                medPago.add("99");
                break;
            } 
            List<String> monedas = new ArrayList<>();
            switch (m) {
              case "1":
                monedas.add("CRC");
                break;
              case "2":
                monedas.add("USD");
                break;
              case "3":
                monedas.add("EUR");
                break;
              case "100":
                monedas.add("CRC");
                monedas.add("USD");
                monedas.add("EUR");
                break;
            } 
            List<String> estados = new ArrayList<>();
            switch (e) {
              case "A":
                estados.add("A");
                break;
              case "N":
                estados.add("N");
                break;
              case "*":
                estados.add("A");
                estados.add("N");
                break;
            } 
            Date _f2 = (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US)).parse(f2 + " 11:59:59 PM");
            ReportesController.this.jdbcTemplate = new JdbcTemplate(ReportesController.this.dataSource);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("emisorId", emisorId);
            String[] _f1 = f1.split("/");
            parameters.addValue("f1", _f1[2] + "-" + _f1[1] + "-" + _f1[0]);
            parameters.addValue("f2", _f2);
            parameters.addValue("td", tipoDoc);
            parameters.addValue("estado", estados);
            parameters.addValue("cv", condVenta);
            parameters.addValue("mp", medPago);
            parameters.addValue("m", monedas);
            parameters.addValue("actividad", actividad);
            parameters.addValue("usuario", user);
            Double montoExento = Double.valueOf(0.0D);
            Double montoSujeto = Double.valueOf(0.0D);
            Double ivaReducida1 = Double.valueOf(0.0D);
            Double ivaReducida2 = Double.valueOf(0.0D);
            Double ivaReducida4 = Double.valueOf(0.0D);
            Double ivatransitorio0 = Double.valueOf(0.0D);
            Double ivatransitorio4 = Double.valueOf(0.0D);
            Double ivatransitorio8 = Double.valueOf(0.0D);
            Double ivageneral13 = Double.valueOf(0.0D);
            Double montoIvaReducida1 = Double.valueOf(0.0D);
            Double montoIvaReducida2 = Double.valueOf(0.0D);
            Double montoIvaReducida4 = Double.valueOf(0.0D);
            Double montoIvatransitorio0 = Double.valueOf(0.0D);
            Double montoIvatransitorio4 = Double.valueOf(0.0D);
            Double montoIvatransitorio8 = Double.valueOf(0.0D);
            Double montoIvageneral13 = Double.valueOf(0.0D);
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate((JdbcOperations)ReportesController.this.jdbcTemplate);
            String queryVendedor = "";
            if (vendedor.longValue() > 0L)
              queryVendedor = " AND vendedor_id = " + vendedor; 
            String sql = "";
            if (user.longValue() == Long.parseLong("0")) {
              if (sucursal.longValue() == 0L || terminal.longValue() == 0L) {
                sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m)) AND codigo_actividad_emisor=:actividad" + queryVendedor;
                if (td.equalsIgnoreCase("6") || actividad.equalsIgnoreCase("ALL"))
                  sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m))" + queryVendedor; 
              } else {
                sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId AND s.id=:sucursal AND t.id=:terminal AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m)) AND codigo_actividad_emisor=:actividad" + queryVendedor;
                if (td.equalsIgnoreCase("6") || actividad.equalsIgnoreCase("ALL"))
                  sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId AND s.id=:sucursal AND t.id=:terminal AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m))" + queryVendedor; 
              } 
            } else if (sucursal.longValue() == 0L || terminal.longValue() == 0L) {
              sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m)) AND codigo_actividad_emisor=:actividad AND user_id = :usuario" + queryVendedor;
              if (td.equalsIgnoreCase("6") || actividad.equalsIgnoreCase("ALL"))
                sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m)) AND user_id = :usuario" + queryVendedor; 
            } else {
              sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId  AND c.sucursal.id=:sucursal AND c.terminal.id=:terminal AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m)) AND codigo_actividad_emisor=:actividad AND user_id = :usuario" + queryVendedor;
              if (td.equalsIgnoreCase("6") || actividad.equalsIgnoreCase("ALL"))
                sql = "SELECT * FROM v_reporte_ventas_encabezado WHERE emisor_id = :emisorId  AND c.sucursal.id=:sucursal AND c.terminal.id=:terminal AND (fecha_limite BETWEEN :f1 AND :f2 AND tipo_documento IN(:td) AND estado IN(:estado) AND cond_venta IN(:cv) AND medio_pago IN(:mp) AND simbolo_moneda IN(:m)) AND user_id = :usuario" + queryVendedor; 
            } 
            String clave = "";
            int rownum = 1;
            String fechaEmisionDoc = "";
            List<Map<String, Object>> reporteVentas = namedParameterJdbcTemplate.queryForList(sql, (SqlParameterSource)parameters);
            for (Map<String, Object> r : reporteVentas) {
              try {
                clave = r.get("clave").toString().substring(30, 31);
              } catch (Exception e2) {
                clave = "4";
              } 
              Row row = sheet.createRow(rownum++);
              cell = row.createCell(0);
              try {
                cell.setCellValue(Long.parseLong(r.get("numero_factura").toString()));
              } catch (Exception e2) {
                cell.setCellValue("");
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(1);
              try {
                cliente = r.get("nombre_completo").toString();
              } catch (Exception e2) {
                cliente = "";
              } 
              try {
                clienteContado = r.get("cliente_contado").toString();
              } catch (Exception e2) {
                clienteContado = "";
              } 
              cell.setCellValue(cliente + clienteContado);
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(2);
              try {
                tipoIdentificacion = r.get("tipo_de_identificacion").toString();
              } catch (Exception e) {
                tipoIdentificacion = "";
              } 
              cell.setCellValue(tipoIdentificacion);
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(3);
              try {
                identificacion = r.get("identificacion").toString();
              } catch (Exception e2) {
                identificacion = "";
              } 
              cell.setCellValue(identificacion);
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(4);
              cell.setCellValue(ReportesController.this.TipoDoc(r.get("tipo_documento").toString()));
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(5);
              try {
                cell.setCellValue(r.get("clave").toString());
              } catch (Exception e2) {
                cell.setCellValue("");
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(6);
              try {
                cell.setCellValue(r.get("consecutivo").toString());
              } catch (Exception e2) {
                cell.setCellValue("");
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(7);
              try {
                fechaEmisionDoc = r.get("fecha_limite").toString();
              } catch (Exception e2) {
                fechaEmisionDoc = "";
              } 
              cell.setCellValue(fechaEmisionDoc);
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(8);
              cell.setCellValue(ReportesController.this.CondicionVenta(r.get("cond_venta").toString()));
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(9);
              cell.setCellValue(r.get("plazo_credito").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(10);
              cell.setCellValue(ReportesController.this.MedioDePago(r.get("medio_pago").toString()));
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(11);
              cell.setCellValue(r.get("simbolo_moneda").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(12);
              cell.setCellValue(Double.parseDouble(r.get("tipo_cambio").toString()));
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(13);
              cell.setCellValue(ReportesController.this.convertidoA(sr));
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(14);
              try {
                montoExento = Double.valueOf(montoExento.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_exento").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, Double.valueOf(Double.parseDouble(r.get("monto_exento").toString())), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                e2.getStackTrace();
                montoExento = Double.valueOf(montoExento.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(15);
              try {
                montoSujeto = Double.valueOf(montoSujeto.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("exento0").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, Double.valueOf(Double.parseDouble(r.get("exento0").toString())), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                e2.getStackTrace();
                ivaReducida1 = Double.valueOf(ivaReducida1.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(16);
              try {
                ivaReducida1 = Double.valueOf(ivaReducida1.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("iva_reducida1").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, Double.valueOf(Double.parseDouble(r.get("iva_reducida1").toString())), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                e2.getStackTrace();
                ivaReducida1 = Double.valueOf(ivaReducida1.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(17);
              try {
                montoIvaReducida1 = Double.valueOf(montoIvaReducida1.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_iva_reducida1").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_iva_reducida1").toString()))), Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString())), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                e2.getStackTrace();
                montoIvaReducida1 = Double.valueOf(montoIvaReducida1.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(18);
              try {
                ivaReducida2 = Double.valueOf(ivaReducida2.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("iva_reducida2").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("iva_reducida2").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                e2.getStackTrace();
                ivaReducida2 = Double.valueOf(ivaReducida2.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(19);
              try {
                montoIvaReducida2 = Double.valueOf(montoIvaReducida2.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_iva_reducida2").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_iva_reducida2").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                e2.getStackTrace();
                montoIvaReducida2 = Double.valueOf(montoIvaReducida2.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(20);
              try {
                ivaReducida4 = Double.valueOf(ivaReducida4.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("iva_reducida4").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, Double.valueOf(Double.parseDouble(r.get("iva_reducida4").toString())), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                ivaReducida4 = Double.valueOf(ivaReducida4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(21);
              try {
                montoIvaReducida4 = Double.valueOf(montoIvaReducida4.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_iva_reducida4").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_iva_reducida4").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                montoIvaReducida4 = Double.valueOf(montoIvaReducida4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(22);
              try {
                ivatransitorio0 = Double.valueOf(ivatransitorio0.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio0").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio0").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                ivatransitorio0 = Double.valueOf(ivatransitorio0.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(23);
              try {
                montoIvatransitorio0 = Double.valueOf(montoIvatransitorio0.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivatransitorio0").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivatransitorio0").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                montoIvatransitorio0 = Double.valueOf(montoIvatransitorio0.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(24);
              try {
                ivatransitorio4 = Double.valueOf(ivatransitorio4.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio4").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio4").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                ivatransitorio4 = Double.valueOf(ivatransitorio4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(25);
              try {
                montoIvatransitorio4 = Double.valueOf(montoIvatransitorio4.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivatransitorio4").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivatransitorio4").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                montoIvatransitorio4 = Double.valueOf(montoIvatransitorio4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(26);
              try {
                ivatransitorio8 = Double.valueOf(ivatransitorio8.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio8").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio8").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                ivatransitorio8 = Double.valueOf(ivatransitorio8.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(27);
              try {
                montoIvatransitorio8 = Double.valueOf(montoIvatransitorio8.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivatransitorio8").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivatransitorio8").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                montoIvatransitorio8 = Double.valueOf(montoIvatransitorio8.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(28);
              try {
                ivageneral13 = Double.valueOf(ivageneral13.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivageneral13").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivageneral13").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                ivageneral13 = Double.valueOf(ivageneral13.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(29);
              try {
                montoIvageneral13 = Double.valueOf(montoIvageneral13.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivageneral13").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("monto_ivageneral13").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              } catch (Exception e2) {
                montoIvageneral13 = Double.valueOf(montoIvageneral13.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
              } 
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(30);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_gravados").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalServGravados = Double.valueOf(TotalServGravados.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_gravados").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(31);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exentos").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalServExentos = Double.valueOf(TotalServExentos.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exentos").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(32);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exonerado").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalServExonerados = Double.valueOf(TotalServExonerados.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exonerado").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(33);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_gravadas").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalMerGravadas = Double.valueOf(TotalMerGravadas.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_gravadas").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(34);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exentas").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalMerExenta = Double.valueOf(TotalMerExenta.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exentas").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(35);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exonerada").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalMerExonerada = Double.valueOf(TotalMerExonerada.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exonerada").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(36);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_gravados").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalGravados = Double.valueOf(TotalGravados.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_gravados").toString()))), Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString())), Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString()))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(37);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_exentos").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalExentos = Double.valueOf(TotalExentos.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_exentos").toString()))), Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString())), Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString()))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(38);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, Double.valueOf(Double.parseDouble(r.get("total_exonerado").toString())), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalExonerado = Double.valueOf(TotalExonerado.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_exonerado").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(39);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_ventas").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalVenta = Double.valueOf(TotalVenta.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_ventas").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(40);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_descuentos").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalDescuentos = Double.valueOf(TotalDescuentos.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_descuentos").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(41);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_venta_neta").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalVentaNeta = Double.valueOf(TotalVentaNeta.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_venta_neta").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(42);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_imp").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalImpuestos = Double.valueOf(TotalImpuestos.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_imp").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(43);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_iva_devuelto").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalIvaDevuelto = Double.valueOf(TotalIvaDevuelto.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_iva_devuelto").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(44);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_otros_cargos").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalOtrosCargos = Double.valueOf(TotalOtrosCargos.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_otros_cargos").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(45);
              cell.setCellValue(ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_comprobante").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              TotalComprobante = Double.valueOf(TotalComprobante.doubleValue() + ReportesController.this.tipoCambioReporte(r.get("moneda_id").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_comprobante").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_dolar").toString()))), ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("tipo_cambio_euro").toString())))).doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(46);
              cell.setCellValue(r.get("codigo_actividad_emisor").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
            } 
            Row fila = sheet.createRow(rownum);
            cell = fila.createCell(13);
            cell.setCellValue("Totales");
            fila.getCell(13).setCellStyle((CellStyle)style);
            cell = fila.createCell(14);
            cell.setCellValue(montoExento.doubleValue());
            fila.getCell(14).setCellStyle((CellStyle)style);
            cell = fila.createCell(15);
            cell.setCellValue(montoSujeto.doubleValue());
            fila.getCell(15).setCellStyle((CellStyle)style);
            cell = fila.createCell(16);
            cell.setCellValue(ivaReducida1.doubleValue());
            fila.getCell(16).setCellStyle((CellStyle)style);
            cell = fila.createCell(17);
            cell.setCellValue(montoIvaReducida1.doubleValue());
            fila.getCell(17).setCellStyle((CellStyle)style);
            cell = fila.createCell(18);
            cell.setCellValue(ivaReducida2.doubleValue());
            fila.getCell(18).setCellStyle((CellStyle)style);
            cell = fila.createCell(19);
            cell.setCellValue(montoIvaReducida2.doubleValue());
            fila.getCell(19).setCellStyle((CellStyle)style);
            cell = fila.createCell(20);
            cell.setCellValue(ivaReducida4.doubleValue());
            fila.getCell(20).setCellStyle((CellStyle)style);
            cell = fila.createCell(21);
            cell.setCellValue(montoIvaReducida4.doubleValue());
            fila.getCell(21).setCellStyle((CellStyle)style);
            cell = fila.createCell(22);
            cell.setCellValue(ivatransitorio0.doubleValue());
            fila.getCell(22).setCellStyle((CellStyle)style);
            cell = fila.createCell(23);
            cell.setCellValue(montoIvatransitorio0.doubleValue());
            fila.getCell(23).setCellStyle((CellStyle)style);
            cell = fila.createCell(24);
            cell.setCellValue(ivatransitorio4.doubleValue());
            fila.getCell(24).setCellStyle((CellStyle)style);
            cell = fila.createCell(25);
            cell.setCellValue(montoIvatransitorio4.doubleValue());
            fila.getCell(25).setCellStyle((CellStyle)style);
            cell = fila.createCell(26);
            cell.setCellValue(ivatransitorio8.doubleValue());
            fila.getCell(26).setCellStyle((CellStyle)style);
            cell = fila.createCell(27);
            cell.setCellValue(montoIvatransitorio8.doubleValue());
            fila.getCell(27).setCellStyle((CellStyle)style);
            cell = fila.createCell(28);
            cell.setCellValue(ivageneral13.doubleValue());
            fila.getCell(28).setCellStyle((CellStyle)style);
            cell = fila.createCell(29);
            cell.setCellValue(montoIvageneral13.doubleValue());
            fila.getCell(29).setCellStyle((CellStyle)style);
            cell = fila.createCell(30);
            cell.setCellValue(TotalServGravados.doubleValue());
            fila.getCell(30).setCellStyle((CellStyle)style);
            cell = fila.createCell(31);
            cell.setCellValue(TotalServExentos.doubleValue());
            fila.getCell(31).setCellStyle((CellStyle)style);
            cell = fila.createCell(32);
            cell.setCellValue(TotalServExonerados.doubleValue());
            fila.getCell(32).setCellStyle((CellStyle)style);
            cell = fila.createCell(33);
            cell.setCellValue(TotalMerGravadas.doubleValue());
            fila.getCell(33).setCellStyle((CellStyle)style);
            cell = fila.createCell(34);
            cell.setCellValue(TotalMerExenta.doubleValue());
            fila.getCell(34).setCellStyle((CellStyle)style);
            cell = fila.createCell(35);
            cell.setCellValue(TotalMerExonerada.doubleValue());
            fila.getCell(35).setCellStyle((CellStyle)style);
            cell = fila.createCell(36);
            cell.setCellValue(TotalGravados.doubleValue());
            fila.getCell(36).setCellStyle((CellStyle)style);
            cell = fila.createCell(37);
            cell.setCellValue(TotalExentos.doubleValue());
            fila.getCell(37).setCellStyle((CellStyle)style);
            cell = fila.createCell(38);
            cell.setCellValue(TotalExonerado.doubleValue());
            fila.getCell(38).setCellStyle((CellStyle)style);
            cell = fila.createCell(39);
            cell.setCellValue(TotalVenta.doubleValue());
            fila.getCell(39).setCellStyle((CellStyle)style);
            cell = fila.createCell(40);
            cell.setCellValue(TotalDescuentos.doubleValue());
            fila.getCell(40).setCellStyle((CellStyle)style);
            cell = fila.createCell(41);
            cell.setCellValue(TotalVentaNeta.doubleValue());
            fila.getCell(41).setCellStyle((CellStyle)style);
            cell = fila.createCell(42);
            cell.setCellValue(TotalImpuestos.doubleValue());
            fila.getCell(42).setCellStyle((CellStyle)style);
            cell = fila.createCell(43);
            cell.setCellValue(TotalIvaDevuelto.doubleValue());
            fila.getCell(43).setCellStyle((CellStyle)style);
            cell = fila.createCell(44);
            cell.setCellValue(TotalOtrosCargos.doubleValue());
            fila.getCell(44).setCellStyle((CellStyle)style);
            cell = fila.createCell(45);
            cell.setCellValue(TotalComprobante.doubleValue());
            fila.getCell(45).setCellStyle((CellStyle)style);
          } 
        }
      };
  }
  
  @GetMapping({"/ventas-productos"})
  public String ventaProductos(Model model, HttpSession session, Authentication auth) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
      List<CCondicionDeVenta> listaCondicionDeVenta = this._condicionDeVentaService.findAll();
      List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAll();
      List<Moneda> listaMonedas = this._monedaService.findAll();
      List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
      List<Cliente> listaProveedores = this._clienteService.findAllClienteProveedor(emisorId, "P");
      Usuario usuario = this._usuarioService.findByUsername(auth.getName());
      model.addAttribute("listaSucursales", this._terminalUsuarioService.findAllByEmisorAndSucursal(emisorId, usuario.getId()));
      model.addAttribute("listUsuarios", this._empresaUsuarioAccesoService.findUsuariosByEmpresa(emisorId));
      model.addAttribute("listaActividades", listaActividades);
      model.addAttribute("listaCondicionDeVenta", listaCondicionDeVenta);
      model.addAttribute("listaMedioDePago", listaMedioDePago);
      model.addAttribute("listaMonedas", listaMonedas);
      model.addAttribute("listaProveedores", listaProveedores);
      return "/reportes/ventas-productos/index";
    } 
    return "redirect:/";
  }
  
  @PostMapping({"/ventas-productos"})
  @ResponseBody
  public void reporteProductos(HttpServletResponse response, HttpSession session, @RequestParam(name = "actividad", required = false) String actividad, @RequestParam(name = "proveedor", required = false) String proveedor, @RequestParam(name = "C", required = false) String c, @RequestParam(name = "sucursal", required = false) String sucursal, @RequestParam(name = "terminal", required = false) String terminal, @RequestParam(name = "f1", required = false) String f1, @RequestParam(name = "f2", required = false) String f2) throws JRException, IOException, SQLException, ParseException {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Connection db = this.dataSource.getConnection();
      InputStream reportfile = null;
      if (session.getAttribute("SESSION_SISTEMA_FARMACIA") != null && session.getAttribute("SESSION_SISTEMA_FARMACIA").toString().equalsIgnoreCase("S")) {
        reportfile = getClass().getResourceAsStream("/reporte-productos/reporte_venta_productos_farmacia.jasper");
      } else {
        reportfile = getClass().getResourceAsStream("/reporte-productos/reporte_venta_productos.jasper");
      } 
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      Date _f1 = (new SimpleDateFormat("dd/MM/yyyy")).parse(f1);
      Date _f2 = (new SimpleDateFormat("dd/MM/yyyy")).parse(f2);
      Map<String, Object> parameter = new HashMap<>();
      parameter.put("C", c);
      parameter.put("F1", format.format(_f1));
      parameter.put("F2", format.format(_f2));
      parameter.put("ACTIVIDAD", actividad);
      parameter.put("PROVEEDOR_ID", (proveedor != null) ? proveedor : "*");
      parameter.put("SUCURSAL", (sucursal != null) ? sucursal : "*");
      parameter.put("TERMINAL", (terminal != null) ? terminal : "*");
      parameter.put("EMISOR_ID", session.getAttribute("SESSION_EMPRESA_ID").toString());
      try {
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameter, db);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        JRXlsxExporter jRXlsxExporter = new JRXlsxExporter((JasperReportsContext)DefaultJasperReportsContext.getInstance());
        jRXlsxExporter.setExporterInput((ExporterInput)new SimpleExporterInput(jasperPrint));
        ServletOutputStream servletOutputStream = response.getOutputStream();
        jRXlsxExporter.setExporterOutput((OutputStreamExporterOutput)new SimpleOutputStreamExporterOutput((OutputStream)servletOutputStream));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(Boolean.valueOf(false));
        configuration.setWhitePageBackground(Boolean.valueOf(false));
        jRXlsxExporter.setConfiguration((XlsxReportConfiguration)configuration);
        jRXlsxExporter.exportReport();
      } catch (JRException ex) {
        System.out.println("Error del reporte: " + ex.getMessage());
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
  }
  
  @GetMapping({"/compras"})
  public String compras(Model model, HttpSession session) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Long emisorId = Long.valueOf(session.getAttribute("SESSION_EMPRESA_ID").toString());
      List<EmisorActividades> listaActividades = this._actividadesService.findAllByEmisorId(emisorId);
      List<Moneda> listaMonedas = this._monedaService.findAll();
      model.addAttribute("listaActividades", listaActividades);
      model.addAttribute("listaMonedas", listaMonedas);
      return "/reportes/compras/index";
    } 
    return "redirect:/";
  }
  
  @PostMapping({"/compras"})
  public View comprasExcel(Model model, HttpServletRequest request, final HttpSession session, @RequestParam String cr, @RequestParam final String f1, @RequestParam final String f2, @RequestParam final String e, @RequestParam final String ci, @RequestParam final String m, @RequestParam final String cf, @RequestParam final String actividad, @RequestParam final String sr) {
    return (View)new AbstractXlsxView() {
        protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
          if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
            response.setHeader("Content-Disposition", "attachment; filename=\"reporte-de-compras-realizadas.xlsx\"");
            String nombreHoja = "Reporte de compras";
            String moneda = "";
            Sheet sheet = workbook.createSheet(nombreHoja);
            Cell cell = null;
            Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
            XSSFFont font = (XSSFFont)workbook.createFont();
            XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
            font.setBold(true);
            style.setFont((Font)font);
            DataFormat format = workbook.createDataFormat();
            XSSFCellStyle styleNumbersG = (XSSFCellStyle)workbook.createCellStyle();
            styleNumbersG.setDataFormat(format.getFormat("#,##0.0000"));
            XSSFCellStyle styleNumbers = (XSSFCellStyle)workbook.createCellStyle();
            styleNumbers.setDataFormat(format.getFormat("#,##0.0000"));
            styleNumbers.setFont((Font)font);
            XSSFCellStyle styleBackground = (XSSFCellStyle)workbook.createCellStyle();
            styleBackground.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            styleBackground.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Emisor");
            header.createCell(1).setCellValue("Identificación");
            header.createCell(2).setCellValue("Fecha emisión");
            header.createCell(3).setCellValue("Fecha registro sistema");
            header.createCell(4).setCellValue("Clave");
            header.createCell(5).setCellValue("Consecutivo");
            header.createCell(6).setCellValue("Estado hacienda");
            header.createCell(7).setCellValue("Moneda");
            header.createCell(8).setCellValue("Tipo cambio");
            header.createCell(9).setCellValue("Convertido a");
            header.createCell(10).setCellValue("IVA reducida 1%");
            header.createCell(11).setCellValue("Monto compra 1%");
            header.createCell(12).setCellValue("IVA reducida 2%");
            header.createCell(13).setCellValue("Monto compra 2%");
            header.createCell(14).setCellValue("IVA reducida 4%");
            header.createCell(15).setCellValue("Monto compra reducida 4%");
            header.createCell(16).setCellValue("IVA Transitorio 0%");
            header.createCell(17).setCellValue("Monto compra transitorio 0%");
            header.createCell(18).setCellValue("IVA Transitorio 4%");
            header.createCell(19).setCellValue("Monto compra transitorio 4%");
            header.createCell(20).setCellValue("IVA Transitorio 8%");
            header.createCell(21).setCellValue("Monto compra transitorio 8%");
            header.createCell(22).setCellValue("IVA Tarifa general 13%");
            header.createCell(23).setCellValue("Monto compra tarifa general 13%");
            header.createCell(24).setCellValue("Total serv. gravados");
            header.createCell(25).setCellValue("Total serv. exentos");
            header.createCell(26).setCellValue("Total ser. exonerado");
            header.createCell(27).setCellValue("Total mer gravadas");
            header.createCell(28).setCellValue("Total mer exenta");
            header.createCell(29).setCellValue("Total mer exonerada");
            header.createCell(30).setCellValue("Total gravados");
            header.createCell(31).setCellValue("Total exentos");
            header.createCell(32).setCellValue("Total exonerado");
            header.createCell(33).setCellValue("Total Venta");
            header.createCell(34).setCellValue("Descuentos");
            header.createCell(35).setCellValue("Total venta neta");
            header.createCell(36).setCellValue("Total Impuestos");
            header.createCell(37).setCellValue("Total Factura");
            header.createCell(38).setCellValue("Detalle aceptación");
            header.getCell(0).setCellStyle((CellStyle)style);
            header.getCell(1).setCellStyle((CellStyle)style);
            header.getCell(2).setCellStyle((CellStyle)style);
            header.getCell(3).setCellStyle((CellStyle)style);
            header.getCell(4).setCellStyle((CellStyle)style);
            header.getCell(5).setCellStyle((CellStyle)style);
            header.getCell(6).setCellStyle((CellStyle)style);
            header.getCell(7).setCellStyle((CellStyle)style);
            header.getCell(8).setCellStyle((CellStyle)style);
            header.getCell(9).setCellStyle((CellStyle)style);
            header.getCell(10).setCellStyle((CellStyle)style);
            header.getCell(11).setCellStyle((CellStyle)style);
            header.getCell(12).setCellStyle((CellStyle)style);
            header.getCell(13).setCellStyle((CellStyle)style);
            header.getCell(14).setCellStyle((CellStyle)style);
            header.getCell(15).setCellStyle((CellStyle)style);
            header.getCell(16).setCellStyle((CellStyle)style);
            header.getCell(17).setCellStyle((CellStyle)style);
            header.getCell(18).setCellStyle((CellStyle)style);
            header.getCell(19).setCellStyle((CellStyle)style);
            header.getCell(20).setCellStyle((CellStyle)style);
            header.getCell(21).setCellStyle((CellStyle)style);
            header.getCell(22).setCellStyle((CellStyle)style);
            header.getCell(23).setCellStyle((CellStyle)style);
            header.getCell(24).setCellStyle((CellStyle)style);
            header.getCell(25).setCellStyle((CellStyle)style);
            header.getCell(26).setCellStyle((CellStyle)style);
            header.getCell(27).setCellStyle((CellStyle)style);
            header.getCell(28).setCellStyle((CellStyle)style);
            header.getCell(29).setCellStyle((CellStyle)style);
            header.getCell(30).setCellStyle((CellStyle)style);
            header.getCell(31).setCellStyle((CellStyle)style);
            header.getCell(32).setCellStyle((CellStyle)style);
            header.getCell(33).setCellStyle((CellStyle)style);
            header.getCell(34).setCellStyle((CellStyle)style);
            header.getCell(35).setCellStyle((CellStyle)style);
            header.getCell(36).setCellStyle((CellStyle)style);
            header.getCell(37).setCellStyle((CellStyle)style);
            header.getCell(38).setCellStyle((CellStyle)style);
            Double TotalImpuestos = Double.valueOf(0.0D);
            Double TotalFactura = Double.valueOf(0.0D);
            Double impuestos = Double.valueOf(0.0D);
            List<String> estadoMH = new ArrayList<>();
            switch (e) {
              case "A":
                estadoMH.add("aceptado");
                break;
              case "R":
                estadoMH.add("rechazado");
                break;
              case "100":
                estadoMH.add("aceptado");
                estadoMH.add("rechazado");
                break;
            } 
            List<String> conIva = new ArrayList<>();
            switch (ci) {
              case "01":
                conIva.add("");
                conIva.add("01");
                break;
              case "02":
                conIva.add("");
                conIva.add("02");
                break;
              case "03":
                conIva.add("");
                conIva.add("03");
                break;
              case "04":
                conIva.add("");
                conIva.add("04");
                break;
              case "05":
                conIva.add("");
                conIva.add("05");
                break;
              case "100":
                conIva.add("");
                conIva.add("01");
                conIva.add("02");
                conIva.add("03");
                conIva.add("04");
                conIva.add("05");
                break;
            } 
            List<String> monedas = new ArrayList<>();
            switch (m) {
              case "1":
                monedas.add("CRC");
                break;
              case "2":
                monedas.add("USD");
                break;
              case "3":
                monedas.add("EUR");
                break;
              case "100":
                monedas.add("CRC");
                monedas.add("USD");
                monedas.add("EUR");
                break;
            } 
            ReportesController.this.jdbcTemplate = new JdbcTemplate(ReportesController.this.dataSource);
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("emisorId", emisorId);
            String[] _f1 = f1.split("/");
            parameters.addValue("f1", _f1[2] + "-" + _f1[1] + "-" + _f1[0]);
            String[] _f2 = f2.split("/");
            parameters.addValue("f2", _f2[2] + "-" + _f2[1] + "-" + _f2[0]);
            parameters.addValue("estadoHacienda", estadoMH);
            parameters.addValue("condicionImpuesto", conIva);
            parameters.addValue("moneda", monedas);
            String actividades = "";
            if (!actividad.equalsIgnoreCase("ALL")) {
              parameters.addValue("actividad", actividad);
              actividades = " AND codigo_actividad_emisor = :actividad";
            } 
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate((JdbcOperations)ReportesController.this.jdbcTemplate);
            String sql = "";
            if (cf.equalsIgnoreCase("1")) {
              sql = "SELECT *, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,1) exento0, fComprasRetornaMontoCompra(emisor_id,clave,1,1) montoExento0, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,2) ivaReducida1, fComprasRetornaMontoCompra(emisor_id,clave,1,2) montoIvaReducida1, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,3) ivaReducida2, fComprasRetornaMontoCompra(emisor_id,clave,1,3) montoIvaReducida2, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,4) ivaReducida4, fComprasRetornaMontoCompra(emisor_id,clave,1,4) montoIvaReducida4, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,5) ivatransitorio0, fComprasRetornaMontoCompra(emisor_id,clave,1,5) montoIvatransitorio0, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,6) ivatransitorio4, fComprasRetornaMontoCompra(emisor_id,clave,1,6) montoIvatransitorio4, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,7) ivatransitorio8, fComprasRetornaMontoCompra(emisor_id,clave,1,7) montoIvatransitorio8, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,8) ivageneral13, fComprasRetornaMontoCompra(emisor_id,clave,1,8) montoIvageneral13 FROM fe_mensaje_receptor WHERE emisor_id=:emisorId AND (DATE_FORMAT(fecha_creacion,'%Y-%m-%d') BETWEEN :f1 AND :f2  AND estado_hacienda IN(:estadoHacienda) AND condicion_impuesto IN(:condicionImpuesto) AND moneda IN(:moneda) " + actividades + " )  GROUP BY clave ORDER BY fecha_emision DESC";
            } else {
              sql = "SELECT *, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,1) exento0, fComprasRetornaMontoCompra(emisor_id,clave,1,1) montoExento0, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,2) ivaReducida1, fComprasRetornaMontoCompra(emisor_id,clave,1,2) montoIvaReducida1, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,3) ivaReducida2, fComprasRetornaMontoCompra(emisor_id,clave,1,3) montoIvaReducida2, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,4) ivaReducida4, fComprasRetornaMontoCompra(emisor_id,clave,1,4) montoIvaReducida4, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,5) ivatransitorio0, fComprasRetornaMontoCompra(emisor_id,clave,1,5) montoIvatransitorio0, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,6) ivatransitorio4, fComprasRetornaMontoCompra(emisor_id,clave,1,6) montoIvatransitorio4, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,7) ivatransitorio8, fComprasRetornaMontoCompra(emisor_id,clave,1,7) montoIvatransitorio8, fComprasRetornaMontoImpuestoPorTarifna(emisor_id,clave,1,8) ivageneral13, fComprasRetornaMontoCompra(emisor_id,clave,1,8) montoIvageneral13 FROM fe_mensaje_receptor WHERE emisor_id=:emisorId AND (DATE_FORMAT(fecha_emision,'%Y-%m-%d') BETWEEN :f1 AND :f2  AND estado_hacienda IN(:estadoHacienda) AND condicion_impuesto IN(:condicionImpuesto) AND moneda IN(:moneda) " + actividades + " )  GROUP BY clave ORDER BY fecha_emision DESC";
            } 
            Double tipoCambio = Double.valueOf(0.0D);
            Double tipoCambioMonedaFecha = Double.valueOf(0.0D);
            Date fechaEmision = null;
            Double ivaReducida1 = Double.valueOf(0.0D);
            Double ivaReducida2 = Double.valueOf(0.0D);
            Double ivaReducida4 = Double.valueOf(0.0D);
            Double ivatransitorio0 = Double.valueOf(0.0D);
            Double ivatransitorio4 = Double.valueOf(0.0D);
            Double ivatransitorio8 = Double.valueOf(0.0D);
            Double ivageneral13 = Double.valueOf(0.0D);
            Double montoIvaReducida1 = Double.valueOf(0.0D);
            Double montoIvaReducida2 = Double.valueOf(0.0D);
            Double montoIvaReducida4 = Double.valueOf(0.0D);
            Double montoIvatransitorio0 = Double.valueOf(0.0D);
            Double montoIvatransitorio4 = Double.valueOf(0.0D);
            Double montoIvatransitorio8 = Double.valueOf(0.0D);
            Double montoIvageneral13 = Double.valueOf(0.0D);
            Double totalServGravados = Double.valueOf(0.0D);
            Double totalServExentos = Double.valueOf(0.0D);
            Double totalSerExonerado = Double.valueOf(0.0D);
            Double totalMerGravadas = Double.valueOf(0.0D);
            Double totalMerExenta = Double.valueOf(0.0D);
            Double totalMerExonerada = Double.valueOf(0.0D);
            Double totalGravados = Double.valueOf(0.0D);
            Double totalExentos = Double.valueOf(0.0D);
            Double totalExonerado = Double.valueOf(0.0D);
            Double totalVenta = Double.valueOf(0.0D);
            Double descuentos = Double.valueOf(0.0D);
            Double totalVentaNeta = Double.valueOf(0.0D);
            String clave = "";
            int rownum = 1;
            List<Map<String, Object>> reporteCompras = namedParameterJdbcTemplate.queryForList(sql, (SqlParameterSource)parameters);
            for (Map<String, Object> r : reporteCompras) {
              clave = r.get("clave").toString().substring(30, 31);
              Row row = sheet.createRow(rownum++);
              fechaEmision = (new SimpleDateFormat("yyyy-MM-dd")).parse(r.get("fecha_emision").toString());
              CTipoDeCambio _tipoCambioMonedaFecha = ReportesController.this._tipoDeCambioService.tipoDeCambioPorMonedaAndFecha("USD", fechaEmision);
              if (_tipoCambioMonedaFecha != null)
                tipoCambioMonedaFecha = _tipoCambioMonedaFecha.getTipoDeCambio(); 
              cell = row.createCell(0);
              cell.setCellValue(r.get("emisor_mr").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(1);
              cell.setCellValue(r.get("identificacion_emisor_mr").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(2);
              cell.setCellValue(r.get("fecha_emision").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(3);
              cell.setCellValue(r.get("fecha_creacion").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(4);
              cell.setCellValue(r.get("clave").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(5);
              cell.setCellValue(r.get("consecutivo_generado").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(6);
              cell.setCellValue(r.get("estado_hacienda").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(7);
              moneda = (r.get("moneda") != null && !r.get("moneda").toString().equals("")) ? r.get("moneda").toString() : "CRC";
              cell.setCellValue(moneda);
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(8);
              tipoCambio = Double.valueOf((r.get("tipo_cambio") != null && !r.get("tipo_cambio").toString().equals("")) ? Double.parseDouble(r.get("tipo_cambio").toString()) : 1.0D);
              cell.setCellValue(tipoCambio.doubleValue());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(9);
              cell.setCellValue(ReportesController.this.convertidoA(sr));
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(10);
              try {
                ivaReducida1 = Double.valueOf(ivaReducida1.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivaReducida1").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivaReducida1").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                ivaReducida1 = Double.valueOf(ivaReducida1.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              ivaReducida1 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ivaReducida1, tipoCambioMonedaFecha);
              cell = row.createCell(11);
              try {
                montoIvaReducida1 = Double.valueOf(montoIvaReducida1.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvaReducida1").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvaReducida1").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                montoIvaReducida1 = Double.valueOf(montoIvaReducida1.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              montoIvaReducida1 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, montoIvaReducida1, tipoCambioMonedaFecha);
              cell = row.createCell(12);
              try {
                ivaReducida2 = Double.valueOf(ivaReducida2.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivaReducida2").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivaReducida2").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                ivaReducida2 = Double.valueOf(ivaReducida2.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              ivaReducida2 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ivaReducida2, tipoCambioMonedaFecha);
              cell = row.createCell(13);
              try {
                montoIvaReducida2 = Double.valueOf(montoIvaReducida2.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvaReducida2").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvaReducida2").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                montoIvaReducida2 = Double.valueOf(montoIvaReducida2.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              montoIvaReducida2 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, montoIvaReducida2, tipoCambioMonedaFecha);
              cell = row.createCell(14);
              try {
                ivaReducida4 = Double.valueOf(ivaReducida4.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivaReducida4").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivaReducida4").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                ivaReducida4 = Double.valueOf(ivaReducida4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              ivaReducida4 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ivaReducida4, tipoCambioMonedaFecha);
              cell = row.createCell(15);
              try {
                montoIvaReducida4 = Double.valueOf(montoIvaReducida4.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvaReducida4").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvaReducida4").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                montoIvaReducida4 = Double.valueOf(montoIvaReducida4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              montoIvaReducida4 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, montoIvaReducida4, tipoCambioMonedaFecha);
              cell = row.createCell(16);
              try {
                ivatransitorio0 = Double.valueOf(ivatransitorio0.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio0").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio0").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                ivatransitorio0 = Double.valueOf(ivatransitorio0.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              ivatransitorio0 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ivatransitorio0, tipoCambioMonedaFecha);
              cell = row.createCell(17);
              try {
                montoIvatransitorio0 = Double.valueOf(montoIvatransitorio0.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvatransitorio0").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvatransitorio0").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                montoIvatransitorio0 = Double.valueOf(montoIvatransitorio0.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              montoIvatransitorio0 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, montoIvatransitorio0, tipoCambioMonedaFecha);
              cell = row.createCell(18);
              try {
                ivatransitorio4 = Double.valueOf(ivatransitorio4.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio4").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio4").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                ivatransitorio4 = Double.valueOf(ivatransitorio4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              ivatransitorio4 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ivatransitorio4, tipoCambioMonedaFecha);
              cell = row.createCell(19);
              try {
                montoIvatransitorio4 = Double.valueOf(montoIvatransitorio4.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvatransitorio4").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvatransitorio4").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                montoIvatransitorio4 = Double.valueOf(montoIvatransitorio4.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              montoIvatransitorio4 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, montoIvatransitorio4, tipoCambioMonedaFecha);
              cell = row.createCell(20);
              try {
                ivatransitorio8 = Double.valueOf(ivatransitorio8.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio8").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivatransitorio8").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                ivatransitorio8 = Double.valueOf(ivatransitorio8.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              ivatransitorio8 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ivatransitorio8, tipoCambioMonedaFecha);
              cell = row.createCell(21);
              try {
                montoIvatransitorio8 = Double.valueOf(montoIvatransitorio8.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvatransitorio8").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvatransitorio8").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                montoIvatransitorio8 = Double.valueOf(montoIvatransitorio8.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              montoIvatransitorio8 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, montoIvatransitorio8, tipoCambioMonedaFecha);
              cell = row.createCell(22);
              try {
                ivageneral13 = Double.valueOf(ivageneral13.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivageneral13").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("ivageneral13").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                ivageneral13 = Double.valueOf(ivageneral13.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              ivageneral13 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ivageneral13, tipoCambioMonedaFecha);
              cell = row.createCell(23);
              try {
                montoIvageneral13 = Double.valueOf(montoIvageneral13.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvageneral13").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("montoIvageneral13").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                montoIvageneral13 = Double.valueOf(montoIvageneral13.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              montoIvageneral13 = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, montoIvageneral13, tipoCambioMonedaFecha);
              cell = row.createCell(24);
              try {
                totalServGravados = Double.valueOf(totalServGravados.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_gravados").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_gravados").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalServGravados = Double.valueOf(totalServGravados.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalServGravados = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalServGravados, tipoCambioMonedaFecha);
              cell = row.createCell(25);
              try {
                totalServExentos = Double.valueOf(totalServExentos.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exentos").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exentos").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalServExentos = Double.valueOf(totalServExentos.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalServExentos = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalServExentos, tipoCambioMonedaFecha);
              cell = row.createCell(26);
              try {
                totalSerExonerado = Double.valueOf(totalSerExonerado.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exonerado").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_serv_exonerado").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalSerExonerado = Double.valueOf(totalSerExonerado.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalSerExonerado = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalSerExonerado, tipoCambioMonedaFecha);
              cell = row.createCell(27);
              try {
                totalMerGravadas = Double.valueOf(totalMerGravadas.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_gravadas").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_gravadas").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalMerGravadas = Double.valueOf(totalMerGravadas.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalMerGravadas = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalMerGravadas, tipoCambioMonedaFecha);
              cell = row.createCell(28);
              try {
                totalMerExenta = Double.valueOf(totalMerExenta.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exentas").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exentas").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalMerExenta = Double.valueOf(totalMerExenta.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalMerExenta = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalMerExenta, tipoCambioMonedaFecha);
              cell = row.createCell(29);
              try {
                totalMerExonerada = Double.valueOf(totalMerExonerada.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exonerada").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_merc_exonerada").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalMerExonerada = Double.valueOf(totalMerExonerada.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalMerExonerada = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalMerExonerada, tipoCambioMonedaFecha);
              cell = row.createCell(30);
              try {
                totalGravados = Double.valueOf(totalGravados.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_gravados").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_gravados").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalGravados = Double.valueOf(totalGravados.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalGravados = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalGravados, tipoCambioMonedaFecha);
              cell = row.createCell(31);
              try {
                totalExentos = Double.valueOf(totalExentos.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_exentos").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_exentos").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalExentos = Double.valueOf(totalExentos.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalExentos = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalExentos, tipoCambioMonedaFecha);
              cell = row.createCell(32);
              try {
                totalExonerado = Double.valueOf(totalExonerado.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_exonerado").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_exonerado").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalExonerado = Double.valueOf(totalExonerado.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalExonerado = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalExonerado, tipoCambioMonedaFecha);
              cell = row.createCell(33);
              try {
                totalVenta = Double.valueOf(totalVenta.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_ventas").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_ventas").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalVenta = Double.valueOf(totalVenta.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalVenta = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalVenta, tipoCambioMonedaFecha);
              cell = row.createCell(34);
              try {
                descuentos = Double.valueOf(descuentos.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_descuentos").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_descuentos").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                descuentos = Double.valueOf(descuentos.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              descuentos = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, descuentos, tipoCambioMonedaFecha);
              cell = row.createCell(35);
              try {
                totalVentaNeta = Double.valueOf(totalVentaNeta.doubleValue() + ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_venta_neta").toString()))).doubleValue());
                cell.setCellValue(ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_venta_neta").toString()))).doubleValue());
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } catch (Exception e2) {
                totalVentaNeta = Double.valueOf(totalVentaNeta.doubleValue() + 0.0D);
                cell.setCellValue(Double.parseDouble("0.00"));
                if (clave.equalsIgnoreCase("3"))
                  cell.setCellStyle((CellStyle)styleBackground); 
              } 
              totalVentaNeta = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, totalVentaNeta, tipoCambioMonedaFecha);
              cell = row.createCell(36);
              impuestos = Double.valueOf((r.get("impuestos") != null && !r.get("impuestos").equals("") && !r.get("impuestos").equals("null")) ? ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("impuestos").toString()))).doubleValue() : 0.0D);
              impuestos = ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ReportesController.this.montoFinal(clave, impuestos), tipoCambioMonedaFecha);
              cell.setCellValue(ReportesController.this.montoFinal(clave, impuestos).doubleValue());
              TotalImpuestos = Double.valueOf(TotalImpuestos.doubleValue() + ReportesController.this.montoFinal(clave, impuestos).doubleValue());
              row.getCell(36).setCellStyle((CellStyle)styleNumbersG);
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(37);
              cell.setCellValue(ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_factura").toString()))), tipoCambioMonedaFecha).doubleValue());
              TotalFactura = Double.valueOf(TotalFactura.doubleValue() + ReportesController.this.tipoCambioReporteCompras(r.get("moneda").toString(), sr, ReportesController.this.montoFinal(clave, Double.valueOf(Double.parseDouble(r.get("total_factura").toString()))), tipoCambioMonedaFecha).doubleValue());
              row.getCell(37).setCellStyle((CellStyle)styleNumbersG);
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
              cell = row.createCell(38);
              cell.setCellValue(r.get("detalle_mensaje").toString());
              if (clave.equalsIgnoreCase("3"))
                cell.setCellStyle((CellStyle)styleBackground); 
            } 
            Row fila = sheet.createRow(rownum);
            cell = fila.createCell(9);
            cell.setCellValue("Totales");
            fila.getCell(9).setCellStyle((CellStyle)style);
            cell = fila.createCell(10);
            cell.setCellValue(ivaReducida1.doubleValue());
            fila.getCell(10).setCellStyle((CellStyle)style);
            cell = fila.createCell(11);
            cell.setCellValue(montoIvaReducida1.doubleValue());
            fila.getCell(11).setCellStyle((CellStyle)style);
            cell = fila.createCell(12);
            cell.setCellValue(ivaReducida2.doubleValue());
            fila.getCell(12).setCellStyle((CellStyle)style);
            cell = fila.createCell(13);
            cell.setCellValue(montoIvaReducida2.doubleValue());
            fila.getCell(13).setCellStyle((CellStyle)style);
            cell = fila.createCell(14);
            cell.setCellValue(ivaReducida4.doubleValue());
            fila.getCell(14).setCellStyle((CellStyle)style);
            cell = fila.createCell(15);
            cell.setCellValue(montoIvaReducida4.doubleValue());
            fila.getCell(15).setCellStyle((CellStyle)style);
            cell = fila.createCell(16);
            cell.setCellValue(ivatransitorio0.doubleValue());
            fila.getCell(16).setCellStyle((CellStyle)style);
            cell = fila.createCell(17);
            cell.setCellValue(montoIvatransitorio0.doubleValue());
            fila.getCell(17).setCellStyle((CellStyle)style);
            cell = fila.createCell(18);
            cell.setCellValue(ivatransitorio4.doubleValue());
            fila.getCell(18).setCellStyle((CellStyle)style);
            cell = fila.createCell(19);
            cell.setCellValue(montoIvatransitorio4.doubleValue());
            fila.getCell(19).setCellStyle((CellStyle)style);
            cell = fila.createCell(20);
            cell.setCellValue(ivatransitorio8.doubleValue());
            fila.getCell(20).setCellStyle((CellStyle)style);
            cell = fila.createCell(21);
            cell.setCellValue(montoIvatransitorio8.doubleValue());
            fila.getCell(21).setCellStyle((CellStyle)style);
            cell = fila.createCell(22);
            cell.setCellValue(ivageneral13.doubleValue());
            fila.getCell(22).setCellStyle((CellStyle)style);
            cell = fila.createCell(23);
            cell.setCellValue(montoIvageneral13.doubleValue());
            fila.getCell(23).setCellStyle((CellStyle)style);
            cell = fila.createCell(24);
            cell.setCellValue(totalServGravados.doubleValue());
            fila.getCell(24).setCellStyle((CellStyle)style);
            cell = fila.createCell(25);
            cell.setCellValue(totalServExentos.doubleValue());
            fila.getCell(25).setCellStyle((CellStyle)style);
            cell = fila.createCell(26);
            cell.setCellValue(totalSerExonerado.doubleValue());
            fila.getCell(26).setCellStyle((CellStyle)style);
            cell = fila.createCell(27);
            cell.setCellValue(totalMerGravadas.doubleValue());
            fila.getCell(27).setCellStyle((CellStyle)style);
            cell = fila.createCell(28);
            cell.setCellValue(totalMerExenta.doubleValue());
            fila.getCell(28).setCellStyle((CellStyle)style);
            cell = fila.createCell(29);
            cell.setCellValue(totalMerExonerada.doubleValue());
            fila.getCell(29).setCellStyle((CellStyle)style);
            cell = fila.createCell(30);
            cell.setCellValue(totalGravados.doubleValue());
            fila.getCell(30).setCellStyle((CellStyle)style);
            cell = fila.createCell(31);
            cell.setCellValue(totalExentos.doubleValue());
            fila.getCell(31).setCellStyle((CellStyle)style);
            cell = fila.createCell(32);
            cell.setCellValue(totalExonerado.doubleValue());
            fila.getCell(32).setCellStyle((CellStyle)style);
            cell = fila.createCell(33);
            cell.setCellValue(totalVenta.doubleValue());
            fila.getCell(33).setCellStyle((CellStyle)style);
            cell = fila.createCell(34);
            cell.setCellValue(descuentos.doubleValue());
            fila.getCell(34).setCellStyle((CellStyle)style);
            cell = fila.createCell(35);
            cell.setCellValue(totalVentaNeta.doubleValue());
            fila.getCell(35).setCellStyle((CellStyle)style);
            cell = fila.createCell(36);
            cell.setCellValue(TotalImpuestos.doubleValue());
            fila.getCell(36).setCellStyle((CellStyle)styleNumbers);
            cell = fila.createCell(37);
            cell.setCellValue(TotalFactura.doubleValue());
            fila.getCell(37).setCellStyle((CellStyle)styleNumbers);
          } 
        }
      };
  }
  
  public String formatDate(String fecha) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.println(LocalDate.parse(fecha, formatter).format(formatter2));
    return LocalDate.parse(fecha, formatter).format(formatter2);
  }
  
  public String CondicionVenta(String c) {
    String response = "";
    switch (c) {
      case "1":
        response = "Contado";
        break;
      case "2":
        response = "Crédito";
        break;
      case "3":
        response = "Consignación";
        break;
      case "4":
        response = "Apartado";
        break;
      case "5":
        response = "Arrendamiento con opción de compra";
        break;
      case "6":
        response = "Arrendamiento en función financiera";
        break;
      case "7":
        response = "Cobro a favor de un tercero";
        break;
      case "8":
        response = "Servicios prestados al Estado a crédito";
        break;
      case "9":
        response = "Pago del servicios prestado al Estado ";
        break;
      case "99":
        response = "Otros (se debe indicar la condición de la venta)";
        break;
    } 
    return response;
  }
  
  public String MedioDePago(String m) {
    String response = "";
    switch (m) {
      case "1":
        response = "Efectivo";
        break;
      case "2":
        response = "Tarjeta";
        break;
      case "3":
        response = "Cheque";
        break;
      case "4":
        response = "Transferencia - depósito bancario";
        break;
      case "5":
        response = "Recaudado por terceros";
        break;
      case "99":
        response = "Otros";
        break;
    } 
    return response;
  }
  
  public String TipoDoc(String td) {
    String response = "";
    switch (td) {
      case "FE":
        response = "Factura Electrónica";
        break;
      case "ND":
        response = "Nota de Débito";
        break;
      case "NC":
        response = "Nota de Crédito";
        break;
      case "TE":
        response = "Tiquete Electrónico";
        break;
      case "FEC":
        response = "Factura Electrónica Compra";
        break;
      case "FEE":
        response = "Factura Electrónica Exportación";
        break;
    } 
    return response;
  }
  
  public Double tipoCambioReporte(String moneda, String salidaReporte, Double montoAConvertir, Double tipoCambioDolar, Double tipoCambioEuro) {
    Double montoFinal = Double.valueOf(0.0D);
    if (moneda.equals("1")) {
      if (salidaReporte.equalsIgnoreCase("C")) {
        montoFinal = montoAConvertir;
      } else if (salidaReporte.equalsIgnoreCase("D")) {
        montoFinal = Double.valueOf(montoAConvertir.doubleValue() / tipoCambioDolar.doubleValue());
      } else {
        montoFinal = Double.valueOf(montoAConvertir.doubleValue() / tipoCambioEuro.doubleValue());
      } 
    } else if (moneda.equals("2")) {
      if (salidaReporte.equalsIgnoreCase("C")) {
        montoFinal = Double.valueOf(montoAConvertir.doubleValue() * tipoCambioDolar.doubleValue());
      } else if (salidaReporte.equalsIgnoreCase("D")) {
        montoFinal = montoAConvertir;
      } else {
        montoFinal = Double.valueOf(montoAConvertir.doubleValue() * tipoCambioDolar.doubleValue() / tipoCambioEuro.doubleValue());
      } 
    } else if (salidaReporte.equalsIgnoreCase("C")) {
      montoFinal = Double.valueOf(montoAConvertir.doubleValue() * tipoCambioEuro.doubleValue());
    } else if (salidaReporte.equalsIgnoreCase("D")) {
      montoFinal = Double.valueOf(montoAConvertir.doubleValue() * tipoCambioEuro.doubleValue() / tipoCambioDolar.doubleValue());
    } else {
      montoFinal = montoAConvertir;
    } 
    return montoFinal;
  }
  
  public Double tipoCambioReporteCompras(String moneda, String salidaReporte, Double montoAConvertir, Double tipoCambioDolar) {
    Double montoFinal = Double.valueOf(0.0D);
    if (moneda.equals("CRC")) {
      if (salidaReporte.equalsIgnoreCase("C")) {
        montoFinal = montoAConvertir;
      } else if (salidaReporte.equalsIgnoreCase("D")) {
        montoFinal = Double.valueOf(montoAConvertir.doubleValue() / tipoCambioDolar.doubleValue());
      } 
    } else if (moneda.equals("USD")) {
      if (salidaReporte.equalsIgnoreCase("C")) {
        montoFinal = Double.valueOf(montoAConvertir.doubleValue() * tipoCambioDolar.doubleValue());
      } else if (salidaReporte.equalsIgnoreCase("D")) {
        montoFinal = montoAConvertir;
      } 
    } 
    return montoFinal;
  }
  
  public String convertidoA(String sr) {
    String response = "";
    switch (sr) {
      case "C":
        response = "Colones";
        break;
      case "D":
        response = "Dolares";
        break;
      case "E":
        response = "Euros";
        break;
    } 
    return response;
  }
  
  public Double montoFinal(String tipoDoc, Double monto) {
    Double m = Double.valueOf(0.0D);
    try {
      if (tipoDoc.equalsIgnoreCase("3")) {
        m = Double.valueOf(monto.doubleValue() * -1.0D);
      } else {
        m = monto;
      } 
    } catch (Exception e) {
      m = monto;
    } 
    return m;
  }
}

