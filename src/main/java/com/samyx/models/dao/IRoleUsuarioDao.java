package com.samyx.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.RolesUsuario;

public interface IRoleUsuarioDao extends CrudRepository<RolesUsuario, Long> {
}
