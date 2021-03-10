package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CBarrio;

public interface ICBarrioDao extends CrudRepository<CBarrio, Long> {
	@Query("SELECT c FROM CBarrio c WHERE c.distrito.id = ?1")
	List<CBarrio> findAllByIdDistrito(Long paramLong);
}
