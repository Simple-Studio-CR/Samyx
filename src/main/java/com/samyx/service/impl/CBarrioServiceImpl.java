package com.samyx.service.impl;

import com.samyx.models.dao.ICBarrioDao;
import com.samyx.models.entity.CBarrio;
import com.samyx.service.interfaces.ICBarrioService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CBarrioServiceImpl implements ICBarrioService {
	@Autowired
	private ICBarrioDao _barrioDao;

	public List<CBarrio> findAllByIdDistrito(Long id) {
		return this._barrioDao.findAllByIdDistrito(id);
	}

	public CBarrio findById(Long id) {
		return this._barrioDao.findById(id).orElse(null);
	}
}
