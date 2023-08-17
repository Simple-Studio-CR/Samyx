package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CDistrito;

public interface ICDistritoService {
	List<CDistrito> findAllByIdCanton(Long paramLong);

	CDistrito findById(Long paramLong);
}
