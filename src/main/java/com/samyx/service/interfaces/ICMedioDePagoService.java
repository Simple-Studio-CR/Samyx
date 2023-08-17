package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CMedioDePago;

public interface ICMedioDePagoService {
	List<CMedioDePago> findAll();

	List<CMedioDePago> findAllIn(List<Long> paramList);

	CMedioDePago findById(Long paramLong);
}
