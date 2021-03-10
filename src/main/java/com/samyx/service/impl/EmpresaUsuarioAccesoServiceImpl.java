package com.samyx.service.impl;

import com.samyx.models.dao.IEmpresaUsuarioAccesoDao;
import com.samyx.models.entity.EmpresaUsuarioAcceso;
import com.samyx.service.interfaces.IEmpresaUsuarioAccesoService;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmpresaUsuarioAccesoServiceImpl implements IEmpresaUsuarioAccesoService {
	@Autowired
	private IEmpresaUsuarioAccesoDao _empresaUsuarioDao;

	public EmpresaUsuarioAcceso findById(Long id) {
		return this._empresaUsuarioDao.findById(id).orElse(null);
	}

	@Transactional
	public void save(EmpresaUsuarioAcceso entity) {
		this._empresaUsuarioDao.save(entity);
	}

	public List<EmpresaUsuarioAcceso> findEmpresaUsuarioById(Long userId) {
		return this._empresaUsuarioDao.findEmpresaUsuarioById(userId);
	}

	public List<EmpresaUsuarioAcceso> findUsuariosByEmpresa(Long emisorId) {
		return this._empresaUsuarioDao.findUsuariosByEmpresa(emisorId);
	}

	public EmpresaUsuarioAcceso findEmpresaByEmisorAndUsuarioById(Long emisorId, Long userId) {
		return this._empresaUsuarioDao.findEmpresaByEmisorAndUsuarioById(emisorId, userId);
	}

	public List<EmpresaUsuarioAcceso> findUsuariosByEmpresaAdmin(Long emisorId) {
		return this._empresaUsuarioDao.findUsuariosByEmpresaAdmin(emisorId);
	}

	public Page<EmpresaUsuarioAcceso> findByAllEmisoresByUserId(Long userId, String q, Pageable pageable) {
		return this._empresaUsuarioDao.findByAllEmisoresByUserId(userId, q, pageable);
	}
}
