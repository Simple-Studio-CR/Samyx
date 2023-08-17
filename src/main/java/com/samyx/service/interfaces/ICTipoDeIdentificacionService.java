package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CTipoDeIdentificacion;

public interface ICTipoDeIdentificacionService {
	List<CTipoDeIdentificacion> findAll();

	CTipoDeIdentificacion findById(Long paramLong);
}
