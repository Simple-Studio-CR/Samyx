package com.samyx.models.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	Usuario findByUsername(String paramString);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario c SET c.password=:pw WHERE c.id =:id")
	void updatePassword(@Param("pw") String paramString, @Param("id") Long paramLong);

	@Query("SELECT c FROM Usuario c WHERE UPPER(c.username)=?1 OR UPPER(c.email)=?1")
	Usuario findByUsuarioOrEmail(String paramString);

	@Query("SELECT c FROM Usuario c WHERE UPPER(c.username)=?1 OR UPPER(c.email)=?2")
	Usuario verificarSiExisteByUsuarioOrEmail(String paramString1, String paramString2);
}