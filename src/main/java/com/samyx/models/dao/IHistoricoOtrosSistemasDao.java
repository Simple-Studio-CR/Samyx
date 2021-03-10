package com.samyx.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.HistoricoOtrosSistemas;

public interface IHistoricoOtrosSistemasDao extends CrudRepository<HistoricoOtrosSistemas, Long> {
	@Query("SELECT c FROM HistoricoOtrosSistemas c WHERE c.emisor.id = ?1 AND (c.cedulaReceptor LIKE %?2% OR UPPER(c.nombreReceptor) LIKE %?2%)")
	Page<HistoricoOtrosSistemas> findAllByIdEmisorAndId(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT c FROM HistoricoOtrosSistemas c WHERE c.emisor.id = ?1 AND c.id=?2")
	HistoricoOtrosSistemas findByIdEmisorAndId(Long paramLong1, Long paramLong2);
}
