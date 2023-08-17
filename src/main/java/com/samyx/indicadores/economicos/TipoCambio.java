package com.samyx.indicadores.economicos;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TipoCambio {
  private int indicador = 0;
  
  private String tcFechaInicio;
  
  private String tcFechaFinal;
  
  private final String tcNombre = "TEC";
  
  private final String tnSubNiveles = "N";
  
  private final String CorreoElectronico = "jose.mata@sinmata.com";
  
  private final String Token = "A5ZT12SADO";
  
  private final String HOST = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos";
  
  private String url;
  
  private final String VALUE_TAG = "NUM_VALOR";
  
  public TipoCambio() {
    setFecha();
  }
  
  public double getCompra() {
    setCompra();
    double valor = Double.parseDouble(getValue());
    return valor;
  }
  
  public double getVenta() {
    setVenta();
    double valor = Double.parseDouble(getValue());
    return valor;
  }
  
  private String getValue() {
    try {
      setUrl();
      String data = GetMethod.getHTML(this.url);
      XmlParser xml = new XmlParser(data);
      return xml.getValue("NUM_VALOR");
    } catch (Exception e) {
      System.out.println("Error al obtener el valor del BCCR.");
      return "0";
    } 
  }
  
  private void setUrl() {
    String params = "Indicador=" + this.indicador + "&FechaInicio=" + this.tcFechaInicio + "&FechaFinal=" + this.tcFechaFinal + "&Nombre=" + "TEC" + "&SubNiveles=" + "N" + "&CorreoElectronico=" + "jose.mata@sinmata.com" + "&Token=" + "A5ZT12SADO" + "";
    this.url = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos?" + params;
  }
  
  private void setFecha() {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    this.tcFechaInicio = sdf.format(date);
    this.tcFechaFinal = this.tcFechaInicio;
  }
  
  private void setCompra() {
    this.indicador = 317;
  }
  
  private void setVenta() {
    this.indicador = 318;
  }
}

