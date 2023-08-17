package com.samyx.service.interfaces;

import com.samyx.models.entity.Role;

public interface IRoleService {
	void save(Role paramRole);

	Role findById(Long paramLong);

	void deleteById(Long paramLong);

	void addRole(String paramString, Long paramLong);
}
