package com.samyx.service.interfaces;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.EmpresaUsuarioAcceso;

public interface IEmpresaUsuarioAccesoService {
	EmpresaUsuarioAcceso findById(Long paramLong);

	EmpresaUsuarioAcceso findEmpresaByEmisorAndUsuarioById(Long paramLong1, Long paramLong2);

	Page<EmpresaUsuarioAcceso> findByAllEmisoresByUserId(Long paramLong, String paramString, Pageable paramPageable);

	void save(EmpresaUsuarioAcceso paramEmpresaUsuarioAcceso);

	List<EmpresaUsuarioAcceso> findEmpresaUsuarioById(Long paramLong);

	List<EmpresaUsuarioAcceso> findUsuariosByEmpresa(Long paramLong);

	List<EmpresaUsuarioAcceso> findUsuariosByEmpresaAdmin(Long paramLong);
}
