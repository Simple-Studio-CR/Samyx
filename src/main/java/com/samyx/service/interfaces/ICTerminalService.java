package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CTerminal;

public interface ICTerminalService {
	List<CTerminal> findAllTerminalBySucursalByEmisor(Long paramLong);

	List<CTerminal> findAllTerminalBySucursal(Long paramLong);

	CTerminal findById(Long paramLong);

	void save(CTerminal paramCTerminal);
}
