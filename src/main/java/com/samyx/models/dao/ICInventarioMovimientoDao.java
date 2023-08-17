package com.samyx.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CInventarioMovimiento;

public interface ICInventarioMovimientoDao extends CrudRepository<CInventarioMovimiento, Long> {
	@Query("SELECT c FROM CInventarioMovimiento c INNER JOIN c.emisor e INNER JOIN c.proveedor p WHERE e.id = ?1 AND (c.numeroFactura LIKE %?2%  OR UPPER(p.nombreCompleto) LIKE %?2%) ORDER BY c.id DESC")
	Page<CInventarioMovimiento> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT c FROM CInventarioMovimiento c INNER JOIN c.emisor e INNER JOIN c.proveedor p WHERE e.id = ?1 AND c.id=?2")
	CInventarioMovimiento findByEmisorIdAndIdFactura(Long paramLong1, Long paramLong2);

	@Query("SELECT MAX(c) FROM CInventarioMovimiento c INNER JOIN c.emisor e WHERE e.id = ?1")
	CInventarioMovimiento secuenciaFacturaCompra(Long paramLong);
}