package com.samyx.service.impl;

import com.samyx.models.dao.ICInventarioAjusteDao;
import com.samyx.models.entity.CInventarioAjuste;
import com.samyx.service.interfaces.ICInventarioMovimientoAjusteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CInventarioMovimientoAjusteServiceImpl implements ICInventarioMovimientoAjusteService {
	@Autowired
	private ICInventarioAjusteDao _dao;

	public void save(CInventarioAjuste entity) {
		this._dao.save(entity);
	}

	public Page<CInventarioAjuste> findAllByEmisorId(Long emisorId, String q, Pageable pageable) {
		return this._dao.findAllByEmisorId(emisorId, q, pageable);
	}

	public CInventarioAjuste findByEmisorIdAndIdFactura(Long emisorId, Long facturaId) {
		return this._dao.findByEmisorIdAndIdFactura(emisorId, facturaId);
	}

	public CInventarioAjuste numeroAjuste(Long emisorId) {
		return this._dao.numeroAjuste(emisorId);
	}
}
