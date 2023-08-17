package com.samyx.service.impl;

import com.samyx.models.dao.IFEItemFacturaDao;
import com.samyx.service.interfaces.IFEItemFacturaService;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FEItemFacturaServiceImpl implements IFEItemFacturaService {
	@Autowired
	private IFEItemFacturaDao _dao;

	@Transactional
	public void deleteById(Long id) {
		this._dao.deleteById(id);
	}
}
