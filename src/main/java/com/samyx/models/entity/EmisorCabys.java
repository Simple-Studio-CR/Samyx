package com.samyx.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "emisores_cabys", uniqueConstraints = {@UniqueConstraint(columnNames = {"emisor_id", "codigo_hacienda"})})
public class EmisorCabys {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  private Emisor emisor;
  
  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  private CCabys cabys;
  
  @Column(name = "codigo_hacienda", length = 13)
  private String codigoHacienda;
  
  @Column(name = "producto_servicio")
  private String productoServicio;
  
  @Column(length = 100)
  private String impuesto;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Emisor getEmisor() {
    return this.emisor;
  }
  
  public void setEmisor(Emisor emisor) {
    this.emisor = emisor;
  }
  
  public CCabys getCabys() {
    return this.cabys;
  }
  
  public void setCabys(CCabys cabys) {
    this.cabys = cabys;
  }
  
  public String getCodigoHacienda() {
    return this.codigoHacienda;
  }
  
  public void setCodigoHacienda(String codigoHacienda) {
    this.codigoHacienda = codigoHacienda;
  }
  
  public String getProductoServicio() {
    return this.productoServicio;
  }
  
  public void setProductoServicio(String productoServicio) {
    this.productoServicio = productoServicio;
  }
  
  public String getImpuesto() {
    return this.impuesto;
  }
  
  public void setImpuesto(String impuesto) {
    this.impuesto = impuesto;
  }
}

