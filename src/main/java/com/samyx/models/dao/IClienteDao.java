package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
	@Modifying
	@Query("UPDATE Cliente c SET c.correo1=:correo WHERE c.id =:id")
	void updateCorreo(@Param("correo") String paramString, @Param("id") Long paramLong);

	@Query("SELECT c FROM Cliente c INNER JOIN c.usuario user INNER JOIN c.tipoDeIdentificacion tdi INNER JOIN c.emisor e WHERE c.emisor.id = ?1 AND c.tipoCatalogo=?2 AND (UPPER(c.nombreCompleto) LIKE %?3% OR c.identificacion LIKE %?3%)")
	Page<Cliente> findAllByIdUserId(Long paramLong, String paramString1, String paramString2, Pageable paramPageable);

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Cliente c WHERE c.id = ?1 AND c.emisor.id = ?2")
	void deleteByIdByUserIdAndIdEmisor(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM Cliente c JOIN FETCH c.usuario user JOIN FETCH c.tipoDeIdentificacion tdi LEFT JOIN FETCH c.provincia p LEFT JOIN FETCH c.canton ca LEFT JOIN FETCH c.distrito d LEFT JOIN FETCH c.barrio b WHERE c.id = ?1 AND c.emisor.id = ?2 AND c.tipoCatalogo=?3")
	Cliente findByIdByUserId(Long paramLong1, Long paramLong2, String paramString);

	@Query("SELECT c FROM Cliente c JOIN FETCH c.emisor e WHERE c.emisor.id = ?1 AND c.tipoCatalogo=?2 AND ( UPPER(c.identificacion) LIKE %?3% OR UPPER(c.nombreCompleto) LIKE %?3% )")
	List<Cliente> findByIdentificacionOrNombreCompleto(Long paramLong, String paramString1, String paramString2);

	@Query("SELECT c FROM Cliente c JOIN FETCH c.emisor e WHERE c.emisor.id = ?1 AND c.tipoCatalogo=?2")
	List<Cliente> findAllClienteProveedor(Long paramLong, String paramString);
}