package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CTerminal;

public interface ICTerminalDao extends CrudRepository<CTerminal, Long> {
	@Query("SELECT c FROM CTerminal c JOIN FETCH c.sucursal s WHERE s.emisor.id = ?1 GROUP BY c.sucursal.id, c.terminal")
	List<CTerminal> findAllTerminalBySucursalByEmisor(Long paramLong);

	@Query("SELECT c FROM CTerminal c WHERE c.sucursal.id = ?1 AND c.status = 1 GROUP BY c.sucursal.id, c.terminal")
	List<CTerminal> findAllTerminalBySucursal(Long paramLong);
}
