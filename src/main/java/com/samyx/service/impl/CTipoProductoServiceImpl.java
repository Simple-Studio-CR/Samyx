package com.samyx.service.impl;

import com.samyx.models.dao.ICTipoProductoDao;
import com.samyx.models.entity.CTipoProducto;
import com.samyx.service.interfaces.ICTipoProductoService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CTipoProductoServiceImpl implements ICTipoProductoService {
	@Autowired
	private ICTipoProductoDao _tipoProductoDao;

	public List<CTipoProducto> findAll() {
		return (List<CTipoProducto>) this._tipoProductoDao.findAll();
	}
}
