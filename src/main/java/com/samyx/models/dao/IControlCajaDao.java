package com.samyx.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.ControlCaja;

public interface IControlCajaDao extends CrudRepository<ControlCaja, Long> {
	@Query("SELECT c FROM ControlCaja c INNER JOIN c.emisor e INNER JOIN c.usuario u WHERE c.emisor.id = ?1 ORDER BY c.id DESC")
	Page<ControlCaja> findByAllByEmisor(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT c FROM ControlCaja c INNER JOIN c.emisor e INNER JOIN c.usuario u WHERE c.emisor.id = ?1 AND c.usuario.id=?2 ORDER BY c.id DESC")
	Page<ControlCaja> findByAllByEmisorByUserId(Long paramLong1, Long paramLong2, String paramString,
			Pageable paramPageable);

	@Query("SELECT c FROM ControlCaja c WHERE c.id = ?1 AND c.emisor.id = ?2")
	ControlCaja findByIdAndEmisorId(Long paramLong1, Long paramLong2);

	@Query("SELECT COUNT(c.id) FROM ControlCaja c INNER JOIN c.emisor e INNER JOIN c.usuario u WHERE c.emisor.id = ?1 AND c.usuario.id=?2 AND c.estadoCaja='A'")
	Integer countByUsuarioAndEmpresa(Long paramLong1, Long paramLong2);
}
