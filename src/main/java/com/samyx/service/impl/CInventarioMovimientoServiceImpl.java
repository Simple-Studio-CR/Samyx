package com.samyx.service.impl;

import com.samyx.models.dao.ICInventarioMovimientoDao;
import com.samyx.models.entity.CInventarioMovimiento;
import com.samyx.service.interfaces.ICInventarioMovimientoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CInventarioMovimientoServiceImpl implements ICInventarioMovimientoService {
	@Autowired
	private ICInventarioMovimientoDao _dao;

	public void save(CInventarioMovimiento entity) {
		this._dao.save(entity);
	}

	public Page<CInventarioMovimiento> findAllByEmisorId(Long emisorId, String q, Pageable pageable) {
		return this._dao.findAllByEmisorId(emisorId, q, pageable);
	}

	public CInventarioMovimiento findByEmisorIdAndIdFactura(Long emisorId, Long facturaId) {
		return this._dao.findByEmisorIdAndIdFactura(emisorId, facturaId);
	}

	public CInventarioMovimiento secuenciaFacturaCompra(Long emisorId) {
		return this._dao.secuenciaFacturaCompra(emisorId);
	}
}
