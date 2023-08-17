package com.samyx.service.impl;

import com.samyx.models.dao.ICTipoDeIdentificacionDao;
import com.samyx.models.entity.CTipoDeIdentificacion;
import com.samyx.service.interfaces.ICTipoDeIdentificacionService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CTipoDeIdentificacionServiceImpl implements ICTipoDeIdentificacionService {
	@Autowired
	private ICTipoDeIdentificacionDao _tipoDeIdentificacionDao;

	public List<CTipoDeIdentificacion> findAll() {
		return (List<CTipoDeIdentificacion>) this._tipoDeIdentificacionDao.findAll();
	}

	public CTipoDeIdentificacion findById(Long id) {
		return this._tipoDeIdentificacionDao.findById(id).orElse(null);
	}
}
