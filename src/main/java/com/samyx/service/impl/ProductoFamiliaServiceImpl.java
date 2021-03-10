package com.samyx.service.impl;

import com.samyx.models.dao.IProductoFamiliaDao;
import com.samyx.models.entity.ProductoFamilia;
import com.samyx.service.interfaces.IProductoFamiliaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoFamiliaServiceImpl implements IProductoFamiliaService {
	@Autowired
	private IProductoFamiliaDao _dao;

	public ProductoFamilia save(ProductoFamilia entity) {
		return (ProductoFamilia) this._dao.save(entity);
	}

	public List<ProductoFamilia> findAllByEmisorId(Long emisorId) {
		return this._dao.findAllByEmisorId(emisorId);
	}

	public ProductoFamilia findByIdAndEmisorId(Long emisorId, Long id) {
		return this._dao.findByIdAndEmisorId(emisorId, id);
	}
}
