package com.samyx.service.impl;

import com.samyx.models.dao.ICProductoDao;
import com.samyx.models.entity.Producto;
import com.samyx.service.interfaces.ICProductoService;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CProductoServiceImpl implements ICProductoService {
	@Autowired
	private ICProductoDao _productoDao;

	@PersistenceContext
	EntityManager em;

	public Page<Producto> findAllByIdEmpresaId(Long id, String codigo, String q, Pageable pageable) {
		return this._productoDao.findAllByIdEmpresaId(id, codigo.toUpperCase(), q.toUpperCase(), pageable);
	}

	@Transactional
	public void save(Producto entity) {
		this._productoDao.save(entity);
	}

	@Transactional
	public void deleteByIdByEmpresaId(Long id, Long emisorId) {
		this._productoDao.deleteByIdByEmpresaId(id, emisorId);
	}

	public Producto findByIdByEmpresaId(Long id, Long emisorId) {
		return this._productoDao.findByIdByEmpresaId(id, emisorId);
	}

	@Transactional
	public List<Producto> findByCodigoAndNombreProductoIgnoreCase(Long emisorId, String codigo, String term,
			int limiteRegistros) {
		return this.em.createQuery(
				"SELECT c FROM Producto c JOIN FETCH c.moneda m JOIN FETCH c.tipoProducto tp  WHERE c.emisor.id = :emisorId AND c.estadoProducto='A' AND ( UPPER(c.codigo)=:codigo OR UPPER(c.nombreProducto) LIKE :term)",
				Producto.class)

				.setParameter("emisorId", emisorId).setParameter("codigo", codigo.toUpperCase())
				.setParameter("term", "%" + term.toUpperCase() + "%").setMaxResults(limiteRegistros).getResultList();
	}

	public List<Producto> findByCodigoProductoIgnoreCase(Long emisorId, String codigo) {
		return this.em.createQuery(
				"SELECT c FROM Producto c JOIN FETCH c.moneda m JOIN FETCH c.tipoProducto tp  WHERE c.emisor.id = :emisorId AND c.estadoProducto='A' AND (UPPER(c.codigo)=:codigo or UPPER(c.codigoBarras)=:codigo)",
				Producto.class)

				.setParameter("emisorId", emisorId).setParameter("codigo", codigo.toUpperCase()).setMaxResults(1)
				.getResultList();
	}

	public Producto findById(Long id) {
		return this._productoDao.findById(id).orElse(null);
	}

	@Transactional
	public void registroEntradasInvent(Double entrada, Long productoId) {
		this._productoDao.registroEntradasInvent(entrada, productoId);
	}

	@Transactional(rollbackOn = { Exception.class })
	public void registroSalidasInvent(Double entrada, Long productoId) {
		this._productoDao.registroSalidasInvent(entrada, productoId);
	}

	@Transactional(rollbackOn = { Exception.class })
	public void registroEntradasInventUpdatePrecios(Double precio, Double precioCompra, Double precioPromediado,
			Double entrada, Double utilidad, Double utilidadFraccion, Double precioVentaFraccion, Long productoId) {
		this._productoDao.registroEntradasInventUpdatePrecios(precio, precioCompra, precioPromediado, entrada, utilidad,
				utilidadFraccion, precioVentaFraccion, productoId);
	}

	public List<Producto> reportePorExistencia(Long emisorId, String criterio, Double cantidad) {
		String sql = "SELECT * FROM productos WHERE emisor_id = :emisorId AND  (entradas - salidas " + criterio
				+ " :cantidad)";
		return this.em.createNativeQuery(sql, Producto.class).setParameter("emisorId", emisorId)
				.setParameter("cantidad", cantidad).getResultList();
	}

	public List<Producto> reporteProductosTodo(Long emisorId) {
		return this._productoDao.reporteProductosTodo(emisorId);
	}

	public List<Producto> reporteVentaProductos(Long emisorId, String codigo, String term) {
		String sql = "SELECT\r\nconsecutivo,\r\nfecha_emision,\r\nSUM(cantidad) cantidad,\r\nid,\r\nmonto_descuento,\r\nmonto_total,\r\nSUM(monto_total_linea) monto_total_linea,\r\nnaturaleza_descuento,\r\nnumero_linea,\r\nprecio_unitario,\r\nsub_total,\r\nproducto_id,\r\nfactura_id,\r\ndetalle_producto,\r\npartida_arancelaria,\r\nimpuesto_neto,\r\nmoneda_id,\r\nSUM(monto) monto_total_impuesto,\r\ntarifa,\r\ncodigo_tarifa_iva_id,\r\ncodigo,\r\nnombre_producto,\r\nsimbolo_moneda,\r\nabreviatura,\r\nnombre_familia,\r\nprecio_compra,\r\nprecio_promediado,\r\nsimbolo,\r\nproducto_familia_id,\r\nemisor_id,\r\nuser_id\r\nFROM\r\nWHERE\r\nemisor_id = ?1 AND \r\nfecha_emision BETWEEN ?2 AND ?3\r\nv_ventas_por_producto\r\nGROUP BY codigo, simbolo_moneda\r\nORDER BY producto_familia_id, codigo ASC";
		return this.em.createNativeQuery(sql).setParameter("emisorId", emisorId).getResultList();
	}

	@Transactional
	public void aplicarInventarioMrLinea(String estadoInventarioLinea, Double precio, Double utilidad,
			Double utilidadFraccion, Double precioVentaFraccion, Long productoId, Long usuarioAplicoId,
			Date fechaAplicacionInventario, Double precioActualCatalogo, Long id) {
		this._productoDao.aplicarInventarioMrLinea(estadoInventarioLinea, precio, utilidad, utilidadFraccion,
				precioVentaFraccion, productoId, usuarioAplicoId, fechaAplicacionInventario, precioActualCatalogo, id);
	}

	@Transactional
	public void aplicarInventarioMr(Double precioCompra, Double precioPromediado, Double precioVenta, Double entrada,
			Double utilidad, Double utilidadFraccion, Double precioVentaFraccion, Long productoId, Long emisorId) {
		this._productoDao.aplicarInventarioMr(precioCompra, precioPromediado, precioVenta, entrada, utilidad,
				utilidadFraccion, precioVentaFraccion, productoId, emisorId);
	}
}
