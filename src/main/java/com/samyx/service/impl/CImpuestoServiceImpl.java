package com.samyx.service.impl;

import com.samyx.models.dao.ICImpuestoDao;
import com.samyx.models.entity.CImpuesto;
import com.samyx.service.interfaces.ICImpuestoService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CImpuestoServiceImpl implements ICImpuestoService {
	@Autowired
	private ICImpuestoDao _impuestoDao;

	public List<CImpuesto> findAllImpuestos() {
		return this._impuestoDao.findAllImpuestos();
	}

	public CImpuesto findById(Long id) {
		return this._impuestoDao.findById(id).orElse(null);
	}

	public void save(CImpuesto entity) {
		this._impuestoDao.save(entity);
	}

	public void deleteById(Long id) {
		this._impuestoDao.deleteById(id);
	}
}