package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CProductoImpuesto;

public interface ICProductoImpuestoDao extends CrudRepository<CProductoImpuesto, Long> {
	@Query("SELECT c FROM CProductoImpuesto c JOIN FETCH c.impuesto imp WHERE c.producto.id = ?1")
	List<CProductoImpuesto> findAllByIdProducto(Long paramLong);
}