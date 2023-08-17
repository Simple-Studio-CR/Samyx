package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CBarrio;

public interface ICBarrioService {
	List<CBarrio> findAllByIdDistrito(Long paramLong);

	CBarrio findById(Long paramLong);
}
