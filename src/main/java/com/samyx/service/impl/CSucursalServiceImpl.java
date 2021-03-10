package com.samyx.service.impl;

import com.samyx.models.dao.ICSucursalDao;
import com.samyx.models.entity.CSucursal;
import com.samyx.service.interfaces.ICSucursalService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CSucursalServiceImpl implements ICSucursalService {
	@Autowired
	private ICSucursalDao _sucursalDao;

	public List<CSucursal> findAllByEmisorId(Long id) {
		return this._sucursalDao.findAllByEmisorId(id);
	}

	public void save(CSucursal entity) {
		this._sucursalDao.save(entity);
	}

	public CSucursal findById(Long id) {
		return this._sucursalDao.findById(id).orElse(null);
	}
}
