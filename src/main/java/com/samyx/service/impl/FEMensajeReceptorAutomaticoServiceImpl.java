package com.samyx.service.impl;

import com.samyx.models.dao.IFEMensajeReceptorAutomaticoDao;
import com.samyx.models.entity.FEMensajeReceptorAutomatico;
import com.samyx.service.interfaces.IFEMensajeReceptorAutomaticoService;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FEMensajeReceptorAutomaticoServiceImpl implements IFEMensajeReceptorAutomaticoService {
	@Autowired
	private IFEMensajeReceptorAutomaticoDao _dao;

	public Page<FEMensajeReceptorAutomatico> findAllByReceptor(String tipoIdentificacion, String identificacion,
			String estado, String q, Pageable pageable) {
		return this._dao.findAllByReceptor(tipoIdentificacion, identificacion, estado, q, pageable);
	}

	public FEMensajeReceptorAutomatico findById(Long id) {
		return this._dao.findById(id).orElse(null);
	}

	@Transactional
	public void updateEstadoMrInbox(String estado, Long idMrInbox) {
		this._dao.updateEstadoMrInbox(estado, idMrInbox);
	}
}
