package com.samyx.service.impl;

import com.samyx.models.dao.ICActividadEconomicaDao;
import com.samyx.models.entity.CActividadEconomica;
import com.samyx.service.interfaces.ICActividadEconomicaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CActividadEconomicaServiceImpl implements ICActividadEconomicaService {
	@Autowired
	private ICActividadEconomicaDao _dao;

	public List<CActividadEconomica> findAll() {
		return (List<CActividadEconomica>) this._dao.findAll();
	}
}
