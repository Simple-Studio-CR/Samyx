package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CCodigosTarifasIva;

public interface ICCodigosTarifasIvaService {
	List<CCodigosTarifasIva> findAll();

	CCodigosTarifasIva findById(Long paramLong);
}
