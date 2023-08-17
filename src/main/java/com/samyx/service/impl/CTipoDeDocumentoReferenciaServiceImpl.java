package com.samyx.service.impl;

import com.samyx.models.dao.ICTipoDeDocumentoReferenciaDao;
import com.samyx.models.entity.CTipoDeDocumentoReferencia;
import com.samyx.service.interfaces.ICTipoDeDocumentoReferenciaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CTipoDeDocumentoReferenciaServiceImpl implements ICTipoDeDocumentoReferenciaService {
	@Autowired
	private ICTipoDeDocumentoReferenciaDao _dao;

	public List<CTipoDeDocumentoReferencia> findAll() {
		return (List<CTipoDeDocumentoReferencia>) this._dao.findAll();
	}
}
