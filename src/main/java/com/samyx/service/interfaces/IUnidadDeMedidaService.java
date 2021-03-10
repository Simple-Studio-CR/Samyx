package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.UnidadDeMedida;

public interface IUnidadDeMedidaService {
	List<UnidadDeMedida> findAll();

	UnidadDeMedida findById(Long paramLong);

	void save(UnidadDeMedida paramUnidadDeMedida);
}
