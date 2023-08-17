package com.samyx.service.impl;

import com.samyx.models.dao.ICTipoDocumentoExoneracionOAutorizacionDao;
import com.samyx.models.entity.CTipoDocumentoExoneracionOAutorizacion;
import com.samyx.service.interfaces.ICTipoDocumentoExoneracionOAutorizacionService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CTipoDocumentoExoneracionOAutorizacionServiceImpl
		implements ICTipoDocumentoExoneracionOAutorizacionService {
	@Autowired
	private ICTipoDocumentoExoneracionOAutorizacionDao _dao;

	public List<CTipoDocumentoExoneracionOAutorizacion> findAll() {
		return (List<CTipoDocumentoExoneracionOAutorizacion>) this._dao.findAll();
	}
}
