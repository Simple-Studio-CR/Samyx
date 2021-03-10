package com.samyx.service.impl;

import com.samyx.models.dao.IRoleDao;
import com.samyx.models.entity.Role;
import com.samyx.service.interfaces.IRoleService;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {
	@Autowired
	private IRoleDao _roleDao;

	@Autowired
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	public void save(Role role) {
		this._roleDao.save(role);
	}

	public Role findById(Long id) {
		return this._roleDao.findById(id).orElse(null);
	}

	public void deleteById(Long id) {
		this._roleDao.deleteById(id);
	}

	public void addRole(String rol, Long idUser) {
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
		String sql = "INSERT INTO authorities (authority,user_id) VALUES ('" + rol + "'," + idUser + ")";
		this.jdbcTemplate.update(sql);
	}
}
