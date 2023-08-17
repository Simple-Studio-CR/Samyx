package com.samyx.controllers;

import com.samyx.models.entity.Emisor;
import com.samyx.models.entity.FEFactura;
import com.samyx.service.interfaces.IEmisorService;
import com.samyx.service.interfaces.IFEFacturaService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImpresionController {
	@Autowired
	public DataSource dataSource;

	@Value("${url.qr}")
	private String urlQr;

	@Value("${path.upload.files.api}")
	private String pathUploadFilesApi;

	@Autowired
	private IFEFacturaService _facturaService;

	@Autowired
	private IEmisorService _emisorService;

	@GetMapping({ "/imprimir-factura-imagen/{clave}" })
	@ResponseBody
	public void imprimirFacturaImagen(HttpServletResponse response, HttpSession session,
			@PathVariable("clave") String clave, @RequestParam(name = "print", required = false) String print)
			throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			FEFactura factura = this._facturaService.findByEmisorByIdAndClave(emisorId, clave);
			if (factura != null) {
				Emisor emisor = this._emisorService.findById(emisorId);
				String logo = "";
				if (emisor != null) {
					logo = emisor.getLogoEmpresa();
					if (logo != null && !logo.equals("") && logo.length() > 0) {
						logo = this.pathUploadFilesApi + "logo/" + logo;
						File f = new File(logo);
						if (!f.exists())
							logo = this.pathUploadFilesApi + "logo/default.png";
					} else {
						logo = this.pathUploadFilesApi + "logo/default.png";
					}
				}
				Connection db = this.dataSource.getConnection();
				InputStream reportfile = null;
				if (emisor.getImpresion().equals("1")) {
					reportfile = getClass().getResourceAsStream("/facturas.jasper");
				} else {
					reportfile = getClass().getResourceAsStream("/tiquete.jasper");
				}
				if (print != null && print.equalsIgnoreCase("C")) {
					reportfile = getClass().getResourceAsStream("/facturas.jasper");
				} else if (print != null && print.equalsIgnoreCase("T")) {
					reportfile = getClass().getResourceAsStream("/tiquete.jasper");
				}
				URL base = getClass().getResource("/");
				String baseUrl = base.toString();
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("BASE_URL", baseUrl);
				parameter.put("BASE_URL_LOGO", logo);
				parameter.put("CLAVE_FACTURA", factura.getClave());
				parameter.put("TIPO_DOCUMENTO", tipoDocumento(factura.getTipoDocumento()));
				parameter.put("RESOLUCION", "“Autorizada mediante resolución Nº DGT-R-033-2019 del 20/06/2019”");
				parameter.put("NOTA_FACTURA", factura.getOtros());
				parameter.put("URL_QR", this.urlQr + factura.getClave());
				try {
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportfile, parameter, db);
					response.setContentType("image/png");
					response.setHeader("Content-Disposition",
							"filename=" + factura.getTipoDocumento() + "-" + factura.getClave() + ".png");
					DefaultJasperReportsContext.getInstance();
					BufferedImage rendered_image = null;
					rendered_image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0, 2.0F);
					ImageIO.write(rendered_image, "png", (OutputStream) response.getOutputStream());
					response.flushBuffer();
				} catch (Exception ex) {
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
	}

	@GetMapping({ "/imprimir-factura/{clave}" })
	@ResponseBody
	public void imprimirFactura(HttpServletResponse response, HttpSession session, @PathVariable("clave") String clave,
			@RequestParam(name = "print", required = false) String print,
			@RequestParam(name = "t", required = false) String tipoImpresion)
			throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			FEFactura factura = this._facturaService.findByEmisorByIdAndClave(emisorId, clave);
			if (factura != null) {
				Emisor emisor = this._emisorService.findById(emisorId);
				String logo = "";
				if (emisor != null) {
					logo = emisor.getLogoEmpresa();
					if (logo != null && !logo.equals("") && logo.length() > 0) {
						logo = this.pathUploadFilesApi + "logo/" + logo;
						File f = new File(logo);
						if (!f.exists())
							logo = this.pathUploadFilesApi + "logo/default.png";
					} else {
						logo = this.pathUploadFilesApi + "logo/default.png";
					}
				}
				Connection db = this.dataSource.getConnection();
				InputStream reportfile = null;
				if (emisor.getImpresion().equals("1")) {
					reportfile = getClass().getResourceAsStream("/facturas.jasper");
				} else {
					reportfile = getClass().getResourceAsStream("/tiquete.jasper");
				}
				if (print != null && print.equalsIgnoreCase("C")) {
					reportfile = getClass().getResourceAsStream("/facturas.jasper");
				} else if (print != null && print.equalsIgnoreCase("T")) {
					reportfile = getClass().getResourceAsStream("/tiquete.jasper");
				}
				URL base = getClass().getResource("/");
				String baseUrl = base.toString();
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("IS_IGNORE_PAGINATION", Boolean.valueOf(false));
				parameter.put("BASE_URL", baseUrl);
				parameter.put("BASE_URL_LOGO", logo);
				parameter.put("CLAVE_FACTURA", factura.getClave());
				parameter.put("TIPO_DOCUMENTO", tipoDocumento(factura.getTipoDocumento()));
				parameter.put("RESOLUCION", "“Autorizada mediante resolución Nº DGT-R-033-2019 del 20/06/2019”");
				parameter.put("NOTA_FACTURA", factura.getOtros());
				parameter.put("URL_QR", this.urlQr + factura.getClave());
				try {
					byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
					if (bytes != null && bytes.length > 0) {
						response.setContentType("application/pdf");
						response.setHeader("Content-Disposition",
								"filename=" + factura.getTipoDocumento() + "-" + factura.getClave() + ".pdf");
						ServletOutputStream outputstream = response.getOutputStream();
						outputstream.write(bytes, 0, bytes.length);
						outputstream.flush();
						outputstream.close();
					}
				} catch (Exception ex) {
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
	}

	private void PrintReportToPrinter(JasperPrint jp, String impresora) throws JRException {
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(new Copies(1));
		PrinterName printerName = new PrinterName(impresora, null);
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(printerName);
		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter((JRExporterParameter) JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
				printRequestAttributeSet);
		exporter.setParameter((JRExporterParameter) JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
				printServiceAttributeSet);
		exporter.setParameter((JRExporterParameter) JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
		exporter.setParameter((JRExporterParameter) JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
				Boolean.FALSE);
		exporter.exportReport();
	}

	@GetMapping({ "/estado-de-cuenta-cxc/{clienteId}" })
	@ResponseBody
	public void imprimirEstadoDeCuentaCXC(HttpServletResponse response, HttpSession session,
			@PathVariable("clienteId") String clienteId) throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Connection db = this.dataSource.getConnection();
			InputStream reportfile = getClass()
					.getResourceAsStream("/estado-de-cuenta-cxc/estado-de-cuenta-cxc.jasper");
			URL base = getClass().getResource("/estado-de-cuenta-cxc/");
			String baseUrl = base.toString();
			if (clienteId != null) {
				Emisor e = this._emisorService.findById(emisorId);
				String td = tipoDocumento("ECCXC");
				String logo = e.getLogoEmpresa();
				if (logo != null && !logo.equals("") && logo.length() > 0) {
					logo = this.pathUploadFilesApi + "logo/" + logo;
				} else {
					logo = this.pathUploadFilesApi + "logo/default.png";
				}
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("BASE_URL", baseUrl);
				parameter.put("BASE_URL_LOGO", logo);
				parameter.put("CLIENTE_ID", clienteId);
				parameter.put("TIPO_DOCUMENTO", td);
				parameter.put("RESOLUCION", "");
				parameter.put("NOTA_FACTURA", e.getNataFactura());
				parameter.put("URL_QR", this.urlQr + clienteId);
				try {
					byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
					if (bytes != null && bytes.length > 0) {
						response.setContentType("application/pdf");
						ServletOutputStream outputstream = response.getOutputStream();
						outputstream.write(bytes, 0, bytes.length);
						outputstream.flush();
						outputstream.close();
					} else {
						System.out.println("NO trae nada");
					}
				} catch (JRException ex) {
					System.out.println("Error del reporte: " + ex.getMessage());
				} finally {
					reportfile.close();
					try {
						if (db != null)
							db.close();
					} catch (SQLException ex) {
						System.out.println("Error: desconectando la base de datos.");
					}
				}
			}
		}
	}

	@GetMapping({ "/estado-de-cuenta-cxc-canceladas/{clienteId}" })
	@ResponseBody
	public void imprimirEstadoDeCuentaCXCCanceladas(HttpServletResponse response, HttpSession session,
			@PathVariable("clienteId") String clienteId) throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			Connection db = this.dataSource.getConnection();
			InputStream reportfile = getClass()
					.getResourceAsStream("/impresiones/cxc-canceladas/estado-de-cuenta-cxc.jasper");
			URL base = getClass().getResource("/impresiones/cxc-canceladas/");
			String baseUrl = base.toString();
			if (clienteId != null) {
				Emisor e = this._emisorService.findById(emisorId);
				String td = tipoDocumento("ECCXC");
				String logo = e.getLogoEmpresa();
				if (logo != null && !logo.equals("") && logo.length() > 0) {
					logo = this.pathUploadFilesApi + "logo/" + logo;
				} else {
					logo = this.pathUploadFilesApi + "logo/default.png";
				}
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("BASE_URL", baseUrl);
				parameter.put("BASE_URL_LOGO", logo);
				parameter.put("CLIENTE_ID", clienteId);
				parameter.put("TIPO_DOCUMENTO", td);
				parameter.put("RESOLUCION", "");
				parameter.put("NOTA_FACTURA", e.getNataFactura());
				parameter.put("URL_QR", this.urlQr + clienteId);
				try {
					byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
					if (bytes != null && bytes.length > 0) {
						response.setContentType("application/pdf");
						ServletOutputStream outputstream = response.getOutputStream();
						outputstream.write(bytes, 0, bytes.length);
						outputstream.flush();
						outputstream.close();
					} else {
						System.out.println("NO trae nada");
					}
				} catch (JRException ex) {
					System.out.println("Error del reporte: " + ex.getMessage());
				} finally {
					reportfile.close();
					try {
						if (db != null)
							db.close();
					} catch (SQLException ex) {
						System.out.println("Error: desconectando la base de datos.");
					}
				}
			}
		}
	}

	@GetMapping({ "/imprimir-proforma/{factura_id}" })
	@ResponseBody
	public void imprimirProforma(HttpServletResponse response, HttpSession session, @PathVariable("factura_id") Long id,
			@RequestParam(name = "print", required = false) String print)
			throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Long emisorId = Long.valueOf(Long.parseLong(session.getAttribute("SESSION_EMPRESA_ID").toString()));
			FEFactura factura = this._facturaService.findByEmisorById(emisorId, id);
			if (factura != null) {
				Emisor emisor = this._emisorService.findById(emisorId);
				String logo = "";
				if (emisor != null) {
					logo = emisor.getLogoEmpresa();
					if (logo != null && !logo.equals("") && logo.length() > 0) {
						logo = this.pathUploadFilesApi + "logo/" + logo;
					} else {
						logo = this.pathUploadFilesApi + "logo/default.png";
					}
				}
				Connection db = this.dataSource.getConnection();
				InputStream reportfile = null;
				reportfile = getClass().getResourceAsStream("/proforma/facturas.jasper");
				URL base = getClass().getResource("/proforma/");
				String baseUrl = base.toString();
				Map<String, Object> parameter = new HashMap<>();
				parameter.put("BASE_URL", baseUrl);
				parameter.put("BASE_URL_LOGO", logo);
				parameter.put("CLAVE_FACTURA", factura.getId());
				parameter.put("TIPO_DOCUMENTO", tipoDocumento(factura.getTipoDocumento()));
				parameter.put("RESOLUCION", factura.getEmisor().getNotaValidezProforma());
				parameter.put("NOTA_FACTURA", factura.getOtros());
				parameter.put("URL_QR", this.urlQr + factura.getId());
				try {
					byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
					if (bytes != null && bytes.length > 0) {
						response.setContentType("application/pdf");
						ServletOutputStream outputstream = response.getOutputStream();
						outputstream.write(bytes, 0, bytes.length);
						outputstream.flush();
						outputstream.close();
					} else {
						System.out.println("_____________________________________________");
					}
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
	}

	@GetMapping({ "/imprimir-recibo-cxc/{id}" })
	@ResponseBody
	public void imprimirReciboCXC(HttpServletResponse response, HttpSession session, @PathVariable("id") String id,
			@RequestParam(name = "print", required = false) String print)
			throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Connection db = this.dataSource.getConnection();
			InputStream reportfile = null;
			reportfile = getClass().getResourceAsStream("/reciboCXC.jasper");
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("CXC_ID", id);
			try {
				byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
				if (bytes != null && bytes.length > 0) {
					response.setContentType("application/pdf");
					ServletOutputStream outputstream = response.getOutputStream();
					outputstream.write(bytes, 0, bytes.length);
					outputstream.flush();
					outputstream.close();
				}
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

	@GetMapping({ "/imprimir-recibo-cxc-masivo/{id}" })
	@ResponseBody
	public void imprimirReciboCXCMasivo(HttpServletResponse response, HttpSession session,
			@PathVariable("id") String id, @RequestParam(name = "print", required = false) String print)
			throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Connection db = this.dataSource.getConnection();
			InputStream reportfile = null;
			reportfile = getClass().getResourceAsStream("/impresiones/cxc/recibo/reciboCXCMasivo.jasper");
			URL base = getClass().getResource("/impresiones/cxc/recibo/");
			String baseUrl = base.toString();
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("BASE_URL", baseUrl);
			parameter.put("CXC_NUMERO", id);
			parameter.put("EMISOR_ID", session.getAttribute("SESSION_EMPRESA_ID").toString());
			try {
				byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
				if (bytes != null && bytes.length > 0) {
					response.setContentType("application/pdf");
					ServletOutputStream outputstream = response.getOutputStream();
					outputstream.write(bytes, 0, bytes.length);
					outputstream.flush();
					outputstream.close();
				}
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

	@GetMapping({ "/imprimir-cierre-caja/{id}" })
	@ResponseBody
	public void imprimirCierreCaja(HttpServletResponse response, HttpSession session, @PathVariable("id") String id,
			@RequestParam(name = "resumen", required = false) String resumen)
			throws JRException, IOException, SQLException {
		if (session.getAttribute("SESSION_EMPRESA_ID") != null) {
			Connection db = this.dataSource.getConnection();
			InputStream reportfile = null;
			reportfile = getClass().getResourceAsStream("/cierre-caja/cierre_caja.jasper");
			if (resumen != null)
				reportfile = getClass().getResourceAsStream("/cierre-caja/cierre_caja_resumen.jasper");
			Map<String, Object> parameter = new HashMap<>();
			parameter.put("ID_CAJA", id);
			parameter.put("EMISOR_ID", session.getAttribute("SESSION_EMPRESA_ID").toString());
			try {
				byte[] bytes = JasperRunManager.runReportToPdf(reportfile, parameter, db);
				if (bytes != null && bytes.length > 0) {
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "filename=reporte-cierre-de-caja.pdf");
					ServletOutputStream outputstream = response.getOutputStream();
					outputstream.write(bytes, 0, bytes.length);
					outputstream.flush();
					outputstream.close();
				}
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
			resp = "Factura Electrónica Compra";
			break;
		case "FEE":
			resp = "Factura Electrónica de Exportación";
			break;
		case "PR":
			resp = "Factura Proforma";
			break;
		case "ECCXC":
			resp = "Estado de Cuenta";
			break;
		}
		return resp;
	}
}
