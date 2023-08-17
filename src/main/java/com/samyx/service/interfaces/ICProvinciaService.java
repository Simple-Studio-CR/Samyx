package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CProvincia;

public interface ICProvinciaService {
	List<CProvincia> findAll();

	CProvincia findById(Long paramLong);
}
