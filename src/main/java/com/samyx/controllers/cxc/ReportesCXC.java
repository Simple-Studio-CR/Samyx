package com.samyx.controllers.cxc;

import com.samyx.models.entity.CMedioDePago;
import com.samyx.models.entity.FEFacturaRegistroPagosCXC;
import com.samyx.service.interfaces.ICMedioDePagoService;
import com.samyx.service.interfaces.IFEFacturaRegistroPagosCXCService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
@Secured({"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_CXC", "ROLE_CXC_READ", "ROLE_CXC_READ_WRITER"})
@RequestMapping({"/cxc/reportes"})
public class ReportesCXC {
  @Autowired
  private ICMedioDePagoService _medioDePagoService;
  
  @Autowired
  private IFEFacturaRegistroPagosCXCService _cxcRegistroService;
  
  @Autowired
  public DataSource dataSource;
  
  @GetMapping({"/flujo-de-caja"})
  public String ventas(Model model, HttpSession session) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      List<Long> mediosDePago = new ArrayList<>();
      mediosDePago.add(Long.valueOf(Long.parseLong("1")));
      mediosDePago.add(Long.valueOf(Long.parseLong("2")));
      mediosDePago.add(Long.valueOf(Long.parseLong("3")));
      mediosDePago.add(Long.valueOf(Long.parseLong("4")));
      mediosDePago.add(Long.valueOf(Long.parseLong("100")));
      mediosDePago.add(Long.valueOf(Long.parseLong("101")));
      List<CMedioDePago> listaMedioDePago = this._medioDePagoService.findAllIn(mediosDePago);
      model.addAttribute("listaMedioDePago", listaMedioDePago);
      return "/cuentas-por-cobrar/reportes/index";
    } 
    return "redirect:/";
  }
  
  @PostMapping({"/flujo-de-caja"})
  public View comprasExcel(Model model, HttpServletRequest request, final HttpSession session, @RequestParam String cr, @RequestParam final String f1, @RequestParam final String f2, @RequestParam final String mp) {
    return (View)new AbstractXlsxView() {
        protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
          if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
            response.setHeader("Content-Disposition", "attachment; filename=\"cxc-flujo-de-caja.xlsx\"");
            String nombreHoja = "Estado de cuenta, CXC";
            Sheet sheet = workbook.createSheet(nombreHoja);
            Cell cell = null;
            Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
            XSSFFont font = (XSSFFont)workbook.createFont();
            XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
            font.setBold(true);
            style.setFont((Font)font);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Cliente");
            header.createCell(1).setCellValue("Identificación");
            header.createCell(2).setCellValue("Clave");
            header.createCell(3).setCellValue("Número factura");
            header.createCell(4).setCellValue("Fecha registro sistema");
            header.createCell(5).setCellValue("Fecha abondo");
            header.createCell(6).setCellValue("Moneda");
            header.createCell(7).setCellValue("Monto");
            header.createCell(8).setCellValue("Medio pago");
            header.getCell(0).setCellStyle((CellStyle)style);
            header.getCell(1).setCellStyle((CellStyle)style);
            header.getCell(2).setCellStyle((CellStyle)style);
            header.getCell(3).setCellStyle((CellStyle)style);
            header.getCell(4).setCellStyle((CellStyle)style);
            header.getCell(5).setCellStyle((CellStyle)style);
            header.getCell(6).setCellStyle((CellStyle)style);
            header.getCell(7).setCellStyle((CellStyle)style);
            header.getCell(8).setCellStyle((CellStyle)style);
            Double montoAbono = Double.valueOf(0.0D);
            List<Long> medioPago = new ArrayList<>();
            switch (mp) {
              case "1":
                medioPago.add(Long.valueOf(Long.parseLong("1")));
                break;
              case "2":
                medioPago.add(Long.valueOf(Long.parseLong("2")));
                break;
              case "3":
                medioPago.add(Long.valueOf(Long.parseLong("3")));
                break;
              case "4":
                medioPago.add(Long.valueOf(Long.parseLong("4")));
                break;
              case "100":
                medioPago.add(Long.valueOf(Long.parseLong("100")));
                break;
              case "101":
                medioPago.add(Long.valueOf(Long.parseLong("101")));
                break;
              case "200":
                medioPago.add(Long.valueOf(Long.parseLong("01")));
                medioPago.add(Long.valueOf(Long.parseLong("02")));
                medioPago.add(Long.valueOf(Long.parseLong("03")));
                medioPago.add(Long.valueOf(Long.parseLong("04")));
                medioPago.add(Long.valueOf(Long.parseLong("100")));
                medioPago.add(Long.valueOf(Long.parseLong("101")));
                break;
            } 
            List<FEFacturaRegistroPagosCXC> flujoCXC = ReportesCXC.this._cxcRegistroService.reporteFlujoCajaCXC(emisorId, (new SimpleDateFormat("dd/MM/yyyy")).parse(f1), (new SimpleDateFormat("dd/MM/yyyy")).parse(f2), medioPago);
            int rownum = 1;
            for (FEFacturaRegistroPagosCXC item : flujoCXC) {
              Row row = sheet.createRow(rownum++);
              cell = row.createCell(0);
              cell.setCellValue(item.getCliente().getNombreCompleto());
              cell = row.createCell(1);
              cell.setCellValue(item.getCliente().getIdentificacion());
              cell = row.createCell(2);
              cell.setCellValue(item.getFacturaCXC().getClave());
              cell = row.createCell(3);
              cell.setCellValue(item.getFacturaCXC().getNumeroFactura().longValue());
              cell = row.createCell(4);
              cell.setCellValue(item.getFechaAbono());
              cell = row.createCell(5);
              if (item.getFechaPagoAbondo() != null) {
                cell.setCellValue(format.format(item.getFechaPagoAbondo()));
              } else {
                cell.setCellValue("No registrada");
              } 
              cell = row.createCell(6);
              cell.setCellValue(item.getFacturaCXC().getMoneda());
              cell = row.createCell(7);
              cell.setCellValue(item.getMontoAbono());
              montoAbono = Double.valueOf(montoAbono.doubleValue() + item.getMontoAbono());
              cell = row.createCell(8);
              cell.setCellValue(item.getMedioDePago().getMedioDePago());
            } 
            Row fila = sheet.createRow(rownum);
            cell = fila.createCell(6);
            cell.setCellValue("Total");
            fila.getCell(6).setCellStyle((CellStyle)style);
            cell = fila.createCell(7);
            cell.setCellValue(montoAbono.doubleValue());
            fila.getCell(7).setCellStyle((CellStyle)style);
          } 
        }
      };
  }
  
  @GetMapping({"/reportes"})
  public String reportesCXC(Model model, HttpSession session) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null)
      return "/cuentas-por-cobrar/reportes/reporteCXC"; 
    return "redirect:/";
  }
  
  @PostMapping({"/reportes"})
  @ResponseBody
  public void reporteProductos(HttpServletResponse response, HttpSession session) throws JRException, IOException, SQLException, ParseException {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
      Connection db = this.dataSource.getConnection();
      InputStream reportfile = null;
      reportfile = getClass().getResourceAsStream("/impresiones/cxc/todo-pendiente/reporteCXCPendientes.jasper");
      Map<String, Object> parameter = new HashMap<>();
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
  
  @GetMapping({"/estado-de-cuenta"})
  public View estadoDeCuenta(Model model, HttpServletRequest request, final HttpSession session, @RequestParam String cr, @RequestParam final String f1, @RequestParam final String f2, @RequestParam final String mp) {
    return (View)new AbstractXlsxView() {
        protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
          if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
            response.setHeader("Content-Disposition", "attachment; filename=\"flujo-de-caja-cxc.xlsx\"");
            String nombreHoja = "Flujo de caja, CXC";
            Sheet sheet = workbook.createSheet(nombreHoja);
            Cell cell = null;
            Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
            XSSFFont font = (XSSFFont)workbook.createFont();
            XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
            font.setBold(true);
            style.setFont((Font)font);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Cliente");
            header.createCell(1).setCellValue("Identificación");
            header.createCell(2).setCellValue("Clave");
            header.createCell(3).setCellValue("Número factura");
            header.createCell(4).setCellValue("Fecha registro sistema");
            header.createCell(5).setCellValue("Fecha abondo");
            header.createCell(6).setCellValue("Monto");
            header.createCell(7).setCellValue("Medio pago");
            header.getCell(0).setCellStyle((CellStyle)style);
            header.getCell(1).setCellStyle((CellStyle)style);
            header.getCell(2).setCellStyle((CellStyle)style);
            header.getCell(3).setCellStyle((CellStyle)style);
            header.getCell(4).setCellStyle((CellStyle)style);
            header.getCell(5).setCellStyle((CellStyle)style);
            header.getCell(6).setCellStyle((CellStyle)style);
            header.getCell(7).setCellStyle((CellStyle)style);
            Double montoAbono = Double.valueOf(0.0D);
            List<Long> medioPago = new ArrayList<>();
            switch (mp) {
              case "1":
                medioPago.add(Long.valueOf(Long.parseLong("1")));
                break;
              case "2":
                medioPago.add(Long.valueOf(Long.parseLong("2")));
                break;
              case "3":
                medioPago.add(Long.valueOf(Long.parseLong("3")));
                break;
              case "4":
                medioPago.add(Long.valueOf(Long.parseLong("4")));
                break;
              case "100":
                medioPago.add(Long.valueOf(Long.parseLong("100")));
                break;
              case "101":
                medioPago.add(Long.valueOf(Long.parseLong("101")));
                break;
              case "200":
                medioPago.add(Long.valueOf(Long.parseLong("01")));
                medioPago.add(Long.valueOf(Long.parseLong("02")));
                medioPago.add(Long.valueOf(Long.parseLong("03")));
                medioPago.add(Long.valueOf(Long.parseLong("04")));
                medioPago.add(Long.valueOf(Long.parseLong("100")));
                medioPago.add(Long.valueOf(Long.parseLong("101")));
                break;
            } 
            List<FEFacturaRegistroPagosCXC> flujoCXC = ReportesCXC.this._cxcRegistroService.reporteFlujoCajaCXC(emisorId, (new SimpleDateFormat("dd/MM/yyyy")).parse(f1), (new SimpleDateFormat("dd/MM/yyyy")).parse(f2), medioPago);
            int rownum = 1;
            for (FEFacturaRegistroPagosCXC item : flujoCXC) {
              Row row = sheet.createRow(rownum++);
              cell = row.createCell(0);
              cell.setCellValue(item.getCliente().getNombreCompleto());
              cell = row.createCell(1);
              cell.setCellValue(item.getCliente().getIdentificacion());
              cell = row.createCell(2);
              cell.setCellValue(item.getFacturaCXC().getClave());
              cell = row.createCell(3);
              cell.setCellValue(item.getFacturaCXC().getNumeroFactura().longValue());
              cell = row.createCell(4);
              cell.setCellValue(item.getFechaAbono());
              cell = row.createCell(5);
              if (item.getFechaPagoAbondo() != null) {
                cell.setCellValue(format.format(item.getFechaPagoAbondo()));
              } else {
                cell.setCellValue("No registrada");
              } 
              cell = row.createCell(6);
              cell.setCellValue(item.getMontoAbono());
              montoAbono = Double.valueOf(montoAbono.doubleValue() + item.getMontoAbono());
              cell = row.createCell(7);
              cell.setCellValue(item.getMedioDePago().getMedioDePago());
            } 
            Row fila = sheet.createRow(rownum);
            cell = fila.createCell(5);
            cell.setCellValue("Total");
            fila.getCell(5).setCellStyle((CellStyle)style);
            cell = fila.createCell(6);
            cell.setCellValue(montoAbono.doubleValue());
            fila.getCell(6).setCellStyle((CellStyle)style);
          } 
        }
      };
  }
}

