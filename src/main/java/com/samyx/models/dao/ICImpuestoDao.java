package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CImpuesto;

public interface ICImpuestoDao extends CrudRepository<CImpuesto, Long> {
	@Query("SELECT c FROM CImpuesto c ORDER BY c.id")
	List<CImpuesto> findAllImpuestos();
}