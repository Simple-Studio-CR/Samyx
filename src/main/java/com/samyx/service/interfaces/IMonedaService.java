package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.Moneda;

public interface IMonedaService {
	List<Moneda> findAll();

	Moneda findById(Long paramLong);
}
