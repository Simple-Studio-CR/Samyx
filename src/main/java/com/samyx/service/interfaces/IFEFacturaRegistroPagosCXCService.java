package com.samyx.service.interfaces;

import java.util.Date;
import java.util.List;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.FEFacturaRegistroPagosCXC;

public interface IFEFacturaRegistroPagosCXCService {
	List<FEFacturaRegistroPagosCXC> findAllPagosCXC(Long paramLong1, Long paramLong2, Long paramLong3);

	void save(FEFacturaRegistroPagosCXC paramFEFacturaRegistroPagosCXC);

	FEFacturaRegistroPagosCXC findMaxFacturaByEmisor(Long paramLong);

	List<FEFacturaRegistroPagosCXC> reporteFlujoCajaCXC(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("mp") List<Long> paramList);
}
