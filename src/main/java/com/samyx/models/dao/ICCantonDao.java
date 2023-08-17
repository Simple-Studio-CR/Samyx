package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CCanton;

public interface ICCantonDao extends CrudRepository<CCanton, Long> {
	@Query("SELECT c FROM CCanton c WHERE c.provincia.id = ?1")
	List<CCanton> findAllByIdProvincia(Long paramLong);
}