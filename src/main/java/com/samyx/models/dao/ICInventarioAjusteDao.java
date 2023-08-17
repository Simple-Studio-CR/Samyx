package com.samyx.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CInventarioAjuste;

public interface ICInventarioAjusteDao extends CrudRepository<CInventarioAjuste, Long> {
	@Query("SELECT c FROM CInventarioAjuste c INNER JOIN c.emisor e WHERE e.id = ?1 AND (CAST(c.numeroAjuste as string) LIKE %?2%  OR UPPER(c.observaciones) LIKE %?2%) ORDER BY c.id DESC")
	Page<CInventarioAjuste> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT c FROM CInventarioAjuste c INNER JOIN c.emisor e WHERE e.id = ?1 AND c.id=?2")
	CInventarioAjuste findByEmisorIdAndIdFactura(Long paramLong1, Long paramLong2);

	@Query("SELECT MAX(c) FROM CInventarioAjuste c INNER JOIN c.emisor e WHERE e.id = ?1")
	CInventarioAjuste numeroAjuste(Long paramLong);
}