package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.EmpresaUsuarioAcceso;

public interface IEmpresaUsuarioAccesoDao extends CrudRepository<EmpresaUsuarioAcceso, Long> {
	@Query("SELECT eu FROM EmpresaUsuarioAcceso eu JOIN FETCH eu.emisor e JOIN FETCH eu.usuario user WHERE user.id = ?1 ")
	List<EmpresaUsuarioAcceso> findEmpresaUsuarioById(Long paramLong);

	@Query("SELECT eu FROM EmpresaUsuarioAcceso eu INNER JOIN eu.emisor e INNER JOIN eu.usuario user WHERE user.id = ?1 AND  (UPPER(e.identificacion) LIKE %?2%  OR UPPER(e.nombreRazonSocial) LIKE %?2% OR UPPER(e.nombreComercial) LIKE %?2%)")
	Page<EmpresaUsuarioAcceso> findByAllEmisoresByUserId(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT eu FROM EmpresaUsuarioAcceso eu WHERE emisor.id = ?1 AND eu.usuario.id = ?2")
	EmpresaUsuarioAcceso findEmpresaByEmisorAndUsuarioById(Long paramLong1, Long paramLong2);

	@Query("SELECT eu FROM EmpresaUsuarioAcceso eu JOIN FETCH eu.usuario user WHERE eu.emisor.id = ?1 AND user.id > 1")
	List<EmpresaUsuarioAcceso> findUsuariosByEmpresa(Long paramLong);

	@Query("SELECT eu FROM EmpresaUsuarioAcceso eu JOIN FETCH eu.usuario user WHERE eu.emisor.id = ?1")
	List<EmpresaUsuarioAcceso> findUsuariosByEmpresaAdmin(Long paramLong);
}
