package com.samyx.service.impl;

import com.samyx.models.dao.ICDistritoDao;
import com.samyx.models.entity.CDistrito;
import com.samyx.service.interfaces.ICDistritoService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CDistritoServiceImpl implements ICDistritoService {
	@Autowired
	private ICDistritoDao _distritoDao;

	public List<CDistrito> findAllByIdCanton(Long id) {
		return this._distritoDao.findAllByIdCanton(id);
	}

	public CDistrito findById(Long id) {
		return this._distritoDao.findById(id).orElse(null);
	}
}