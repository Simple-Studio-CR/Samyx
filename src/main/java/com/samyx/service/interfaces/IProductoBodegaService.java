package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.ProductoBodega;

public interface IProductoBodegaService {
	List<ProductoBodega> findAllByEmisorId(Long paramLong);

	ProductoBodega save(ProductoBodega paramProductoBodega);

	ProductoBodega findByIdAndEmisorId(Long paramLong1, Long paramLong2);
}
