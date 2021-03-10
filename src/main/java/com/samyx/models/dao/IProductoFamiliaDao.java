package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.ProductoFamilia;

public interface IProductoFamiliaDao extends CrudRepository<ProductoFamilia, Long> {
	@Query("SELECT c FROM ProductoFamilia c WHERE c.emisor.id = ?1")
	List<ProductoFamilia> findAllByEmisorId(Long paramLong);

	@Query("SELECT c FROM ProductoFamilia c WHERE c.emisor.id = ?1 AND c.id=?2")
	ProductoFamilia findByIdAndEmisorId(Long paramLong1, Long paramLong2);
}