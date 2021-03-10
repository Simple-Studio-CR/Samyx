package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.ProductoBodega;

public interface IProductoBodegaDao extends CrudRepository<ProductoBodega, Long> {
	@Query("SELECT c FROM ProductoBodega c WHERE c.emisor.id = ?1")
	List<ProductoBodega> findAllByEmisorId(Long paramLong);

	@Query("SELECT c FROM ProductoBodega c WHERE c.emisor.id = ?1 AND c.id=?2")
	ProductoBodega findByIdAndEmisorId(Long paramLong1, Long paramLong2);
}