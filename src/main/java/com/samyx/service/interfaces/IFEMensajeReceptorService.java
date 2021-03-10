package com.samyx.service.interfaces;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.FEMensajeReceptor;
import com.samyx.models.entity.Usuario;

public interface IFEMensajeReceptorService {
	void save(FEMensajeReceptor paramFEMensajeReceptor);

	FEMensajeReceptor findMrByClave(String paramString);

	Page<FEMensajeReceptor> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	FEMensajeReceptor findMrByEmisorIdAndId(Long paramLong1, Long paramLong2);

	List<FEMensajeReceptor> findAllByEmisorId(Long paramLong);

	void updateEstadoDocByClave(String paramString1, String paramString2, String paramString3, String paramString4);

	List<FEMensajeReceptor> findAllMR(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("estadoHacienda") List<String> paramList1,
			@Param("condicionImpuesto") List<String> paramList2, @Param("moneda") List<String> paramList3,
			String paramString);

	List<FEMensajeReceptor> findAllMRFechaEmision(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("estadoHacienda") List<String> paramList1,
			@Param("condicionImpuesto") List<String> paramList2, @Param("moneda") List<String> paramList3,
			String paramString);

	void aplicarFacturaCompra(Usuario paramUsuario, String paramString, Date paramDate, Long paramLong1,
			Long paramLong2);
}
