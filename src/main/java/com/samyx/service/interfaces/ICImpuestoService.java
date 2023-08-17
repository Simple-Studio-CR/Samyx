package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CImpuesto;

public interface ICImpuestoService {
	List<CImpuesto> findAllImpuestos();

	CImpuesto findById(Long paramLong);

	void save(CImpuesto paramCImpuesto);

	void deleteById(Long paramLong);
}
