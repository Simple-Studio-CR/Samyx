package com.samyx.service.impl;

import com.samyx.models.dao.ICProductoImpuestoDao;
import com.samyx.models.entity.CProductoImpuesto;
import com.samyx.service.interfaces.ICProductoImpuestoService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CProductoImpuestoServiceImpl implements ICProductoImpuestoService {
	@Autowired
	private ICProductoImpuestoDao _productoImpuestoDao;

	public void save(CProductoImpuesto entity) {
		this._productoImpuestoDao.save(entity);
	}

	public List<CProductoImpuesto> findAllByIdProducto(Long id) {
		return this._productoImpuestoDao.findAllByIdProducto(id);
	}

	public void deleteById(Long id) {
		this._productoImpuestoDao.deleteById(id);
	}
}
