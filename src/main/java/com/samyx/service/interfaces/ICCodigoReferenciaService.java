package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CCodigoReferencia;

public interface ICCodigoReferenciaService {
	List<CCodigoReferencia> findAll();

	CCodigoReferencia findById(Long paramLong);
}
