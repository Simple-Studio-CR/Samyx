package com.samyx.service.impl;

import com.samyx.models.dao.IUsuarioDao;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.IUsuarioService;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
	@Autowired
	private IUsuarioDao _usuarioDao;

	@Transactional
	public void save(Usuario usuario) {
		this._usuarioDao.save(usuario);
	}

	public Usuario findByUsername(String username) {
		return this._usuarioDao.findByUsername(username);
	}

	@Transactional
	public void updatePassword(String pw, Long id) {
		this._usuarioDao.updatePassword(pw, id);
	}

	public Usuario findById(Long id) {
		return this._usuarioDao.findById(id).orElse(null);
	}

	public List<Usuario> findAll() {
		return (List<Usuario>) this._usuarioDao.findAll();
	}

	public void deleteById(Long id) {
		this._usuarioDao.deleteById(id);
	}

	public Usuario findByUsuarioOrEmail(String term) {
		return this._usuarioDao.findByUsuarioOrEmail(term);
	}

	public Usuario verificarSiExisteByUsuarioOrEmail(String username, String email) {
		return this._usuarioDao.verificarSiExisteByUsuarioOrEmail(username, email);
	}
}
