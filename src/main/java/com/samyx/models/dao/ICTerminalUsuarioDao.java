package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.CTerminalUsuario;

public interface ICTerminalUsuarioDao extends CrudRepository<CTerminalUsuario, Long> {
	@Query("SELECT c FROM CTerminalUsuario c JOIN FETCH c.terminal t WHERE t.terminal.sucursal.id = ?1")
	List<CTerminalUsuario> findAllBySucursal(Long paramLong);

	@Modifying
	@Query("DELETE FROM CTerminalUsuario c WHERE c.id = ?1")
	void deleteAccesoUsuarioById(Long paramLong);

	@Query("SELECT c FROM CTerminalUsuario c JOIN FETCH c.terminal t JOIN FETCH t.sucursal s WHERE s.emisor.id=?1 AND c.usuario.id=?2 GROUP BY s.id")
	List<CTerminalUsuario> findAllByEmisorAndSucursal(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM CTerminalUsuario c JOIN FETCH c.terminal t JOIN FETCH t.sucursal s WHERE s.id=?1 AND c.usuario.id=?2")
	List<CTerminalUsuario> findAllBySucursalByUsuario(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM CTerminalUsuario c JOIN FETCH c.terminal t JOIN FETCH t.sucursal s WHERE s.id=?1 AND t.terminal.id = ?2 AND c.usuario.id = ?3")
	CTerminalUsuario findSucursalTerminalBySucursalByTerminal(Long paramLong1, Long paramLong2, Long paramLong3);
}