package com.samyx.service.impl;

import com.samyx.models.dao.IControlCajaDao;
import com.samyx.models.entity.ControlCaja;
import com.samyx.service.interfaces.IControlCajaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ControlCajaServiceImpl implements IControlCajaService {
	@Autowired
	private IControlCajaDao _dao;

	public Page<ControlCaja> findByAllByEmisor(Long id, String q, Pageable pageable) {
		return this._dao.findByAllByEmisor(id, q, pageable);
	}

	public void save(ControlCaja entity) {
		this._dao.save(entity);
	}

	public ControlCaja findByIdAndEmisorId(Long id, Long emisorId) {
		return this._dao.findByIdAndEmisorId(id, emisorId);
	}

	public Page<ControlCaja> findByAllByEmisorByUserId(Long id, Long userId, String q, Pageable pageable) {
		return this._dao.findByAllByEmisorByUserId(id, userId, q, pageable);
	}

	public Integer countByUsuarioAndEmpresa(Long emisorId, Long usuarioId) {
		return this._dao.countByUsuarioAndEmpresa(emisorId, usuarioId);
	}
}
