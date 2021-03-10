package com.samyx.service.impl;

import com.samyx.models.dao.IClienteDao;
import com.samyx.models.entity.Cliente;
import com.samyx.service.interfaces.IClienteService;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements IClienteService {
	@Autowired
	private IClienteDao _clienteServiceDao;

	public Page<Cliente> findAllByIdUserId(Long id, String tipoCatalogo, String q, Pageable pageable) {
		return this._clienteServiceDao.findAllByIdUserId(id, tipoCatalogo, q, pageable);
	}

	@Transactional
	public void save(Cliente entity) {
		this._clienteServiceDao.save(entity);
	}

	@Transactional
	public void deleteByIdByUserIdAndIdEmisor(Long id, Long emisorId) {
		this._clienteServiceDao.deleteByIdByUserIdAndIdEmisor(id, emisorId);
	}

	public List<Cliente> findByIdentificacionOrNombreCompleto(Long emisorId, String tipoCatalogo, String term) {
		return this._clienteServiceDao.findByIdentificacionOrNombreCompleto(emisorId, tipoCatalogo, term);
	}

	public Cliente findByIdByUserId(Long id, Long emisorId, String tipoCatalogo) {
		return this._clienteServiceDao.findByIdByUserId(id, emisorId, tipoCatalogo);
	}

	public List<Cliente> findAllClienteProveedor(Long emisorId, String tipoCatalogo) {
		return this._clienteServiceDao.findAllClienteProveedor(emisorId, tipoCatalogo);
	}

	@Transactional
	public void updateCorreo(String correo, Long id) {
		this._clienteServiceDao.updateCorreo(correo, id);
	}
}
