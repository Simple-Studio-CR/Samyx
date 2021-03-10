package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CSucursal;

public interface ICSucursalService {
	List<CSucursal> findAllByEmisorId(Long paramLong);

	CSucursal findById(Long paramLong);

	void save(CSucursal paramCSucursal);
}
