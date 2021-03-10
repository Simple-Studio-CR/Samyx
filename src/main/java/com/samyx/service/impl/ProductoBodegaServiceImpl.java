package com.samyx.service.impl;

import com.samyx.models.dao.IProductoBodegaDao;
import com.samyx.models.entity.ProductoBodega;
import com.samyx.service.interfaces.IProductoBodegaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoBodegaServiceImpl implements IProductoBodegaService {
	@Autowired
	private IProductoBodegaDao _dao;

	public ProductoBodega save(ProductoBodega entity) {
		return (ProductoBodega) this._dao.save(entity);
	}

	public List<ProductoBodega> findAllByEmisorId(Long emisorId) {
		return this._dao.findAllByEmisorId(emisorId);
	}

	public ProductoBodega findByIdAndEmisorId(Long emisorId, Long id) {
		return this._dao.findByIdAndEmisorId(emisorId, id);
	}
}
