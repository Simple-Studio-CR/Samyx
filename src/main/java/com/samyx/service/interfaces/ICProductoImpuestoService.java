package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CProductoImpuesto;

public interface ICProductoImpuestoService {
	void save(CProductoImpuesto paramCProductoImpuesto);

	void deleteById(Long paramLong);

	List<CProductoImpuesto> findAllByIdProducto(Long paramLong);
}
