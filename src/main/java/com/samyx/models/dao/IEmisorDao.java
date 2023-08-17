package com.samyx.models.dao;

import java.util.Date;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.Emisor;

public interface IEmisorDao extends CrudRepository<Emisor, Long> {
	@Query("SELECT e FROM Emisor e WHERE e.identificacion = ?1 AND e.tokenAccess = ?2")
	Emisor findEmisorByIdentificacion(String paramString1, String paramString2);

	@Modifying
	@Query(value = "UPDATE emisores SET fecha_caducidad_cert=?1, fecha_emision_cert=?2 WHERE id=?3", nativeQuery = true)
	void updateFechaCriptografica(Date paramDate1, Date paramDate2, Long paramLong);
}