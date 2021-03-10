package com.samyx.models.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CTipoDeCambio;

public interface ICTipoDeCambioDao extends CrudRepository<CTipoDeCambio, Long> {
	@Query("SELECT c FROM CTipoDeCambio c ORDER BY c.fecha DESC")
	Page<CTipoDeCambio> findAllByEmisorId(Pageable paramPageable);

	@Query("SELECT MAX(c) FROM CTipoDeCambio c GROUP BY c.moneda.id ORDER BY c.fecha DESC")
	List<CTipoDeCambio> findAllDivisas();

	@Query("SELECT c FROM CTipoDeCambio c WHERE c.moneda.simboloMoneda=?1 AND c.fecha=?2 ORDER BY c.fecha DESC")
	CTipoDeCambio tipoDeCambioPorMonedaAndFecha(String paramString, Date paramDate);
}
