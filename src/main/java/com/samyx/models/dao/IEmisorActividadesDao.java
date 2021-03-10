package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.EmisorActividades;

public interface IEmisorActividadesDao extends CrudRepository<EmisorActividades, Long> {
	@Query("SELECT c FROM EmisorActividades c WHERE c.emisor.id = ?1")
	List<EmisorActividades> findAllByEmisorId(Long paramLong);

	@Modifying
	@Query(value = "DELETE FROM emisores_actividades WHERE id=?1 AND emisor_id = id=?2", nativeQuery = true)
	void deleteByIdAndEmisorId(Long paramLong1, Long paramLong2);
}
