package com.samyx.service.impl;

import com.samyx.models.dao.ICMedioDePagoDao;
import com.samyx.models.entity.CMedioDePago;
import com.samyx.service.interfaces.ICMedioDePagoService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CMedioDePagoServiceImpl implements ICMedioDePagoService {
	@Autowired
	private ICMedioDePagoDao _medioDePagoDao;

	@Transactional(readOnly = true)
	public List<CMedioDePago> findAll() {
		return (List<CMedioDePago>) this._medioDePagoDao.findAll();
	}

	public List<CMedioDePago> findAllIn(List<Long> md) {
		return this._medioDePagoDao.findAllIn(md);
	}

	public CMedioDePago findById(Long id) {
		return this._medioDePagoDao.findById(id).orElse(null);
	}
}
