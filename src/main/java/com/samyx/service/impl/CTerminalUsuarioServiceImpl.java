package com.samyx.service.impl;

import com.samyx.models.dao.ICTerminalUsuarioDao;
import com.samyx.models.entity.CTerminalUsuario;
import com.samyx.service.interfaces.ICTerminalUsuarioService;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CTerminalUsuarioServiceImpl implements ICTerminalUsuarioService {
	@Autowired
	private ICTerminalUsuarioDao _dao;

	@Transactional
	public void save(CTerminalUsuario entity) {
		this._dao.save(entity);
	}

	public List<CTerminalUsuario> findAllBySucursal(Long sucursalId) {
		return this._dao.findAllBySucursal(sucursalId);
	}

	@Transactional
	public void deleteAccesoUsuarioById(Long id) {
		this._dao.deleteAccesoUsuarioById(id);
	}

	public List<CTerminalUsuario> findAllByEmisorAndSucursal(Long emisorId, Long userId) {
		return this._dao.findAllByEmisorAndSucursal(emisorId, userId);
	}

	public List<CTerminalUsuario> findAllBySucursalByUsuario(Long sucursalId, Long userId) {
		return this._dao.findAllBySucursalByUsuario(sucursalId, userId);
	}

	public CTerminalUsuario findSucursalTerminalBySucursalByTerminal(Long sucursalId, Long terminalId, Long userId) {
		return this._dao.findSucursalTerminalBySucursalByTerminal(sucursalId, terminalId, userId);
	}
}
