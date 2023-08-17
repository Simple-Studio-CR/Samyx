package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.FEBitacora;

public interface IFEBitacoraService {
	List<FEBitacora> findByIdFactura(Long paramLong);

	FEBitacora findById(Long paramLong);

	void save(FEBitacora paramFEBitacora);

	List<FEBitacora> findAllByEmisorId(Long paramLong);

	void updateEstadoDocByClave(String paramString1, String paramString2, String paramString3, String paramString4);

	int countFe(String paramString);

	int countNd(String paramString);

	int countNc(String paramString);

	int countTe(String paramString);

	int countFec(String paramString);

	int countFee(String paramString);
}
