package com.samyx.service.impl;

import com.samyx.models.dao.ICTipoDeCambioDao;
import com.samyx.models.entity.CTipoDeCambio;
import com.samyx.service.interfaces.ICTipoDeCambioService;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CTipoDeCambioServiceImpl implements ICTipoDeCambioService {
	@Autowired
	private ICTipoDeCambioDao _dao;

	public void save(CTipoDeCambio entity) {
		this._dao.save(entity);
	}

	public Page<CTipoDeCambio> findAllByEmisorId(Pageable pageable) {
		return this._dao.findAllByEmisorId(pageable);
	}

	public List<CTipoDeCambio> findAllDivisas() {
		return this._dao.findAllDivisas();
	}

	public CTipoDeCambio findById(Long id) {
		return this._dao.findById(id).orElse(null);
	}

	public CTipoDeCambio tipoDeCambioPorMonedaAndFecha(String moneda, Date fecha) {
		return this._dao.tipoDeCambioPorMonedaAndFecha(moneda, fecha);
	}
}
