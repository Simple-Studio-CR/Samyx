package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CMedioDePago;

public interface ICMedioDePagoDao extends CrudRepository<CMedioDePago, Long> {
	@Query("SELECT c FROM CMedioDePago c WHERE c.id IN (?1)")
	List<CMedioDePago> findAllIn(List<Long> paramList);
}
