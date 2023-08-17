package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CTerminalUsuario;

public interface ICTerminalUsuarioService {
	List<CTerminalUsuario> findAllBySucursal(Long paramLong);

	void save(CTerminalUsuario paramCTerminalUsuario);

	void deleteAccesoUsuarioById(Long paramLong);

	List<CTerminalUsuario> findAllByEmisorAndSucursal(Long paramLong1, Long paramLong2);

	List<CTerminalUsuario> findAllBySucursalByUsuario(Long paramLong1, Long paramLong2);

	CTerminalUsuario findSucursalTerminalBySucursalByTerminal(Long paramLong1, Long paramLong2, Long paramLong3);
}
