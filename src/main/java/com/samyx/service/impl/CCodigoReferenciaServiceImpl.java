package com.samyx.service.impl;

import com.samyx.models.dao.ICCodigoReferenciaDao;
import com.samyx.models.entity.CCodigoReferencia;
import com.samyx.service.interfaces.ICCodigoReferenciaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CCodigoReferenciaServiceImpl implements ICCodigoReferenciaService {
	@Autowired
	private ICCodigoReferenciaDao _codigoReferenciaDao;

	public List<CCodigoReferencia> findAll() {
		return (List<CCodigoReferencia>) this._codigoReferenciaDao.findAll();
	}

	public CCodigoReferencia findById(Long id) {
		return this._codigoReferenciaDao.findById(id).orElse(null);
	}
}
