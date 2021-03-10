package com.samyx.service.impl;

import com.samyx.models.dao.ICOtrosCargosTipoDocumentoDao;
import com.samyx.models.entity.COtrosCargosTipoDocumento;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class COtrosCargosTipoDocumentoImpl {
	@Autowired
	private ICOtrosCargosTipoDocumentoDao _dao;

	public List<COtrosCargosTipoDocumento> findAll() {
		return (List<COtrosCargosTipoDocumento>) this._dao.findAll();
	}
}
