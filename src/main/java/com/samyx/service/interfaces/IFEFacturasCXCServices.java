package com.samyx.service.interfaces;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.FEFacturasCXC;

public interface IFEFacturasCXCServices {
	void save(FEFacturasCXC paramFEFacturasCXC);

	void deleteById(Long paramLong);

	Page<FEFacturasCXC> findAllCXC(Long paramLong, String paramString1, String paramString2, Pageable paramPageable);

	Page<FEFacturasCXC> findAllCXCCanceladas(Long paramLong, String paramString1, String paramString2,
			Pageable paramPageable);

	List<FEFacturasCXC> findAllCXCDetalle(Long paramLong1, Long paramLong2, String paramString);

	void modificaEstadoFacturaCXC(String paramString, Long paramLong1, Long paramLong2);

	void anularFacturaCXC(String paramString, Long paramLong1, Long paramLong2);

	Double findAllCXCPagadas(Long paramLong1, String paramString1, Long paramLong2, Date paramDate,
			String paramString2);

	FEFacturasCXC estadoClienteParaFacturar(Long paramLong1, Long paramLong2);

	FEFacturasCXC obtenerIdFECXCPorClave(String paramString);
}
