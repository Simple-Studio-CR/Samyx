package com.samyx.service.impl;

import com.samyx.models.dao.ICProvinciaDao;
import com.samyx.models.entity.CProvincia;
import com.samyx.service.interfaces.ICProvinciaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CProvinciaServiceImpl implements ICProvinciaService {
	@Autowired
	private ICProvinciaDao _provinciaDao;

	public List<CProvincia> findAll() {
		return (List<CProvincia>) this._provinciaDao.findAll();
	}

	public CProvincia findById(Long id) {
		return this._provinciaDao.findById(id).orElse(null);
	}
}
