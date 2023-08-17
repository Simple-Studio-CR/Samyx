package com.samyx.models.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.FEFacturaRegistroPagosCXC;

public interface IFEFacturaRegistroPagosCXCDao extends CrudRepository<FEFacturaRegistroPagosCXC, Long> {
	@Query("SELECT c FROM FEFacturaRegistroPagosCXC c WHERE c.emisor.id=?1 AND c.cliente.id=?2 and c.facturaCXC.id=?3 ORDER BY id DESC")
	List<FEFacturaRegistroPagosCXC> findAllPagosCXC(Long paramLong1, Long paramLong2, Long paramLong3);

	@Query("SELECT MAX(c) FROM FEFacturaRegistroPagosCXC c WHERE c.emisor.id = ?1")
	FEFacturaRegistroPagosCXC findMaxFacturaByEmisor(Long paramLong);

	@Query("SELECT c FROM FEFacturaRegistroPagosCXC c JOIN FETCH c.facturaCXC f JOIN FETCH c.cliente cl JOIN FETCH c.medioDePago mp WHERE c.emisor.id = :emisorId AND (STR_TO_DATE(c.fechaRegistroAbondo, '%Y-%m-%d') BETWEEN :f1 AND :f2 AND mp.id IN(:mp))")
	List<FEFacturaRegistroPagosCXC> reporteFlujoCajaCXC(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("mp") List<Long> paramList);
}
