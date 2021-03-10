package com.samyx.models.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.samyx.models.entity.Producto;

public interface ICProductoDao extends JpaRepository<Producto, Long> {
	@Query("SELECT c FROM Producto c INNER JOIN c.usuario user LEFT JOIN c.proveedor p INNER JOIN c.moneda m WHERE c.emisor.id = ?1 AND ( UPPER(c.codigo)=?2 OR UPPER(c.nombreProducto) LIKE %?3% )")
	Page<Producto> findAllByIdEmpresaId(Long paramLong, String paramString1, String paramString2,
			Pageable paramPageable);

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Producto c WHERE c.id = ?1 AND c.emisor.id =?2")
	void deleteByIdByEmpresaId(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM Producto c JOIN FETCH c.usuario user JOIN FETCH c.moneda m JOIN FETCH c.tipoProducto tp INNER JOIN c.emisor e  WHERE c.id = ?1 AND c.emisor.id = ?2")
	Producto findByIdByEmpresaId(Long paramLong1, Long paramLong2);

	@Modifying
	@Query(value = "UPDATE productos SET precio_compra=?1, precio_promediado=?2, precio=?3, entradas=entradas+?4, utilidad=?5, utilidad_fraccion=?6, precio_fraccion=?7  WHERE id=?8", nativeQuery = true)
	void registroEntradasInventUpdatePrecios(Double paramDouble1, Double paramDouble2, Double paramDouble3,
			Double paramDouble4, Double paramDouble5, Double paramDouble6, Double paramDouble7, Long paramLong);

	@Modifying
	@Query(value = "UPDATE productos SET precio_compra=?1, precio_promediado=?2, precio=?3, entradas=entradas+?4, utilidad=?5, utilidad_fraccion=?6, precio_fraccion=?7 WHERE id=?8 AND emisor_id=?9", nativeQuery = true)
	void aplicarInventarioMr(Double paramDouble1, Double paramDouble2, Double paramDouble3, Double paramDouble4,
			Double paramDouble5, Double paramDouble6, Double paramDouble7, Long paramLong1, Long paramLong2);

	@Modifying
	@Query(value = "UPDATE fe_mensaje_receptor_items SET estado_inventario_linea=?1, precio_venta_sin_iva=?2, utilidad=?3, utilidad_fraccion=?4, precio_venta_fraccion_sin_iva=?5, producto_id=?6, usuario_aplico_id=?7, fecha_aplicacion_inventario=?8, precio_actual_catalogo=?9 WHERE id=?10", nativeQuery = true)
	void aplicarInventarioMrLinea(String paramString, Double paramDouble1, Double paramDouble2, Double paramDouble3,
			Double paramDouble4, Long paramLong1, Long paramLong2, Date paramDate, Double paramDouble5,
			Long paramLong3);

	@Modifying
	@Query(value = "UPDATE productos SET entradas=entradas+?1 WHERE id=?2", nativeQuery = true)
	void registroEntradasInvent(Double paramDouble, Long paramLong);

	@Modifying
	@Query(value = "UPDATE productos SET salidas=salidas+?1 WHERE id=?2", nativeQuery = true)
	void registroSalidasInvent(Double paramDouble, Long paramLong);

	@Query(value = "SELECT * FROM productos WHERE emisor_id=?1", nativeQuery = true)
	List<Producto> reporteProductosTodo(Long paramLong);
}