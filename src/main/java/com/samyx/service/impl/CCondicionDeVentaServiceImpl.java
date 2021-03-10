package com.samyx.service.impl;

import com.samyx.models.dao.ICCondicionDeVentaDao;
import com.samyx.models.entity.CCondicionDeVenta;
import com.samyx.service.interfaces.ICCondicionDeVentaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CCondicionDeVentaServiceImpl implements ICCondicionDeVentaService {
	@Autowired
	private ICCondicionDeVentaDao _condicionDeVentaDao;

	public List<CCondicionDeVenta> findAll() {
		return (List<CCondicionDeVenta>) this._condicionDeVentaDao.findAll();
	}
}
