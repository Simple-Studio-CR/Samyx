package com.samyx.service.impl;

import com.samyx.models.dao.IRoleUsuarioDao;
import com.samyx.models.entity.RolesUsuario;
import com.samyx.service.interfaces.IRolesUsuarioService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesUsuarioServiceImpl implements IRolesUsuarioService {
	@Autowired
	private IRoleUsuarioDao _rolesUsuarioDao;

	public List<RolesUsuario> findAll() {
		return (List<RolesUsuario>) this._rolesUsuarioDao.findAll();
	}
}
