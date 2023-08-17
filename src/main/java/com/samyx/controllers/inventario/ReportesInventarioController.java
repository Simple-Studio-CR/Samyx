package com.samyx.controllers.inventario;

import com.samyx.models.entity.Producto;
import com.samyx.service.interfaces.ICProductoService;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@Controller
@RequestMapping({"/inventario/reportes"})
public class ReportesInventarioController {
  @Autowired
  private ICProductoService _productoService;
  
  @GetMapping({"/"})
  public String home(Model model, HttpSession session) {
    if (session.getAttribute("SESSION_EMPRESA_ID") != null)
      return "/catalogos/productos/inventario/reportes/index"; 
    return "redirect:/";
  }
  
  @PostMapping({"/"})
  public View comprasExcel(Model model, HttpServletRequest request, final HttpSession session, @RequestParam(name = "cr", required = false) final String cr, @RequestParam(name = "cantidad", required = false) final Double cantidad) {
    return (View)new AbstractXlsxView() {
        protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
          if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
            response.setHeader("Content-Disposition", "attachment; filename=\"inventario-actual.xlsx\"");
            String nombreHoja = "Inventario actual";
            Sheet sheet = workbook.createSheet(nombreHoja);
            Cell cell = null;
            Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
            XSSFFont font = (XSSFFont)workbook.createFont();
            XSSFCellStyle style = (XSSFCellStyle)workbook.createCellStyle();
            font.setBold(true);
            style.setFont((Font)font);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Código de producto");
            header.createCell(1).setCellValue("Detalle");
            header.createCell(2).setCellValue("Unidad de medida");
            header.createCell(3).setCellValue("Moneda");
            header.createCell(4).setCellValue("Precio compra");
            header.createCell(5).setCellValue("Precio medio ponderado");
            header.createCell(6).setCellValue("Precio venta sin IVA");
            header.createCell(7).setCellValue("Precio venta con IVA");
            header.createCell(8).setCellValue("Inventario actual (Cantidad/Unidad)");
            header.createCell(9).setCellValue("Fracciones por unidad");
            header.createCell(10).setCellValue("Inventario actual (Fracciones)");
            header.createCell(11).setCellValue("Valor inventario ponderado");
            header.createCell(12).setCellValue("Valor inventario actual");
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
            List<Producto> reporteInventario = null;
            if (cr.equalsIgnoreCase("*")) {
              reporteInventario = ReportesInventarioController.this._productoService.reporteProductosTodo(emisorId);
            } else {
              reporteInventario = ReportesInventarioController.this._productoService.reportePorExistencia(emisorId, cr, cantidad);
            } 
            Double precioPromediado = Double.valueOf(0.0D);
            Double precioCompra = Double.valueOf(0.0D);
            Double precioVentaSinIva = Double.valueOf(0.0D);
            Double precioVentaConIva = Double.valueOf(0.0D);
            Double inventPonderado = Double.valueOf(0.0D);
            Double valorInventActual = Double.valueOf(0.0D);
            int rownum = 1;
            for (Producto item : reporteInventario) {
              Row row = sheet.createRow(rownum++);
              cell = row.createCell(0);
              cell.setCellValue(item.getCodigo());
              cell = row.createCell(1);
              cell.setCellValue(item.getNombreProducto());
              cell = row.createCell(2);
              cell.setCellValue(item.getUnidadDeMedida().getSimbolo());
              cell = row.createCell(3);
              cell.setCellValue(item.getMoneda().getAbreviatura());
              cell = row.createCell(4);
              try {
                cell.setCellValue(item.getPrecioCompra().doubleValue());
                precioCompra = Double.valueOf(precioCompra.doubleValue() + item.getPrecioCompra().doubleValue());
              } catch (Exception e) {
                cell.setCellValue(0.0D);
                precioCompra = Double.valueOf(precioCompra.doubleValue() + 0.0D);
              } 
              cell = row.createCell(5);
              if (item.getPrecioPromediado() != null && item.getPrecioPromediado().doubleValue() > 0.0D)
                precioPromediado = item.getPrecioPromediado(); 
              cell.setCellValue(precioPromediado.doubleValue());
              cell = row.createCell(6);
              cell.setCellValue(item.getPrecio().doubleValue());
              precioVentaSinIva = Double.valueOf(precioVentaSinIva.doubleValue() + item.getPrecio().doubleValue());
              cell = row.createCell(7);
              cell.setCellValue(item.getPrecioFinal().doubleValue());
              precioVentaConIva = Double.valueOf(precioVentaConIva.doubleValue() + item.getPrecioFinal().doubleValue());
              cell = row.createCell(8);
              if (item.getFraccionesPorUnidad() != null && item.getFraccionesPorUnidad().doubleValue() > 0.0D) {
                cell.setCellValue(item.getInventarioActual().doubleValue() / item.getFraccionesPorUnidad().doubleValue());
              } else {
                cell.setCellValue(item.getInventarioActual().doubleValue());
              } 
              cell = row.createCell(9);
              cell.setCellValue(item.getFraccionesPorUnidad().doubleValue());
              if (item.getFraccionesPorUnidad() != null && item.getFraccionesPorUnidad().doubleValue() > 0.0D) {
                cell = row.createCell(10);
                cell.setCellValue(item.getInventarioActual().doubleValue());
              } else {
                cell = row.createCell(10);
                cell.setCellValue("N/A");
              } 
              if (item.getFraccionesPorUnidad() != null && item.getFraccionesPorUnidad().doubleValue() > 0.0D) {
                cell = row.createCell(11);
                cell.setCellValue(item.getInventarioActual().doubleValue() / item.getFraccionesPorUnidad().doubleValue() * precioPromediado.doubleValue());
                inventPonderado = Double.valueOf(inventPonderado.doubleValue() + item.getInventarioActual().doubleValue() / item.getFraccionesPorUnidad().doubleValue() * precioPromediado.doubleValue());
                cell = row.createCell(12);
                cell.setCellValue(item.getInventarioActual().doubleValue() / item.getFraccionesPorUnidad().doubleValue() * item.getPrecioCompra().doubleValue());
                valorInventActual = Double.valueOf(valorInventActual.doubleValue() + item.getInventarioActual().doubleValue() / item.getFraccionesPorUnidad().doubleValue() * item.getPrecioCompra().doubleValue());
                continue;
              } 
              cell = row.createCell(11);
              cell.setCellValue(item.getInventarioActual().doubleValue() * precioPromediado.doubleValue());
              inventPonderado = Double.valueOf(inventPonderado.doubleValue() + item.getInventarioActual().doubleValue() * precioPromediado.doubleValue());
              cell = row.createCell(12);
              cell.setCellValue(item.getInventarioActual().doubleValue() * item.getPrecioCompra().doubleValue());
              valorInventActual = Double.valueOf(valorInventActual.doubleValue() + item.getInventarioActual().doubleValue() * item.getPrecioCompra().doubleValue());
            } 
            Row fila = sheet.createRow(rownum);
            cell = fila.createCell(4);
            cell.setCellValue(precioCompra.doubleValue());
            fila.getCell(4).setCellStyle((CellStyle)style);
            cell = fila.createCell(5);
            cell.setCellValue("");
            fila.getCell(5).setCellStyle((CellStyle)style);
            cell = fila.createCell(6);
            cell.setCellValue(precioVentaSinIva.doubleValue());
            fila.getCell(6).setCellStyle((CellStyle)style);
            cell = fila.createCell(7);
            cell.setCellValue(precioVentaConIva.doubleValue());
            fila.getCell(7).setCellStyle((CellStyle)style);
            cell = fila.createCell(8);
            cell.setCellValue("");
            fila.getCell(8).setCellStyle((CellStyle)style);
            cell = fila.createCell(9);
            cell.setCellValue("");
            fila.getCell(9).setCellStyle((CellStyle)style);
            cell = fila.createCell(10);
            cell.setCellValue("");
            fila.getCell(10).setCellStyle((CellStyle)style);
            cell = fila.createCell(11);
            cell.setCellValue(inventPonderado.doubleValue());
            fila.getCell(11).setCellStyle((CellStyle)style);
            cell = fila.createCell(12);
            cell.setCellValue(valorInventActual.doubleValue());
            fila.getCell(12).setCellStyle((CellStyle)style);
          } 
        }
      };
  }
}

