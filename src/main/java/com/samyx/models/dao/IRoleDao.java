package com.samyx.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.Role;

public interface IRoleDao extends CrudRepository<Role, Long> {
}
