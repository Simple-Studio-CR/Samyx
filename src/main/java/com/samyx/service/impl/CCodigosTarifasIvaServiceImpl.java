package com.samyx.service.impl;

import com.samyx.models.dao.ICCodigosTarifasIvaDao;
import com.samyx.models.entity.CCodigosTarifasIva;
import com.samyx.service.interfaces.ICCodigosTarifasIvaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CCodigosTarifasIvaServiceImpl implements ICCodigosTarifasIvaService {
	@Autowired
	private ICCodigosTarifasIvaDao _dao;

	public List<CCodigosTarifasIva> findAll() {
		return (List<CCodigosTarifasIva>) this._dao.findAll();
	}

	public CCodigosTarifasIva findById(Long id) {
		return this._dao.findById(id).orElse(null);
	}
}
