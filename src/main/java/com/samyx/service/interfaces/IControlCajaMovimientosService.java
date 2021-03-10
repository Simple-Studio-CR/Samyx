package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.ControlCajaMovimientos;

public interface IControlCajaMovimientosService {
	void save(ControlCajaMovimientos paramControlCajaMovimientos);

	List<ControlCajaMovimientos> findAllByEmisorAndIdCaja(Long paramLong1, Long paramLong2);

	Double totalEntradasSalidasEfectivo(Long paramLong1, String paramString1, Long paramLong2, String paramString2);
}
