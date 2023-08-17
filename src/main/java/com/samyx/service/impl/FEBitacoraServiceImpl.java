package com.samyx.service.impl;

import com.samyx.models.dao.IFEBitacoraDao;
import com.samyx.models.entity.FEBitacora;
import com.samyx.service.interfaces.IFEBitacoraService;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FEBitacoraServiceImpl implements IFEBitacoraService {
	@Autowired
	private IFEBitacoraDao _dao;

	public List<FEBitacora> findByIdFactura(Long id) {
		return this._dao.findByIdFactura(id);
	}

	@Transactional
	public void save(FEBitacora entity) {
		this._dao.save(entity);
	}

	public List<FEBitacora> findAllByEmisorId(Long id) {
		return this._dao.findAllByEmisorId(id);
	}

	@Transactional
	public void updateEstadoDocByClave(String estado, String fechaAceptacion, String nameXmlRespuesta, String clave) {
		this._dao.updateEstadoDocByClave(estado, fechaAceptacion, nameXmlRespuesta, clave);
	}

	public int countFe(String identificacion) {
		return this._dao.countFe(identificacion);
	}

	public int countNd(String identificacion) {
		return this._dao.countNd(identificacion);
	}

	public int countNc(String identificacion) {
		return this._dao.countNc(identificacion);
	}

	public int countTe(String identificacion) {
		return this._dao.countTe(identificacion);
	}

	public int countFec(String identificacion) {
		return this._dao.countFec(identificacion);
	}

	public int countFee(String identificacion) {
		return this._dao.countFee(identificacion);
	}

	public FEBitacora findById(Long id) {
		return this._dao.findById(id).orElse(null);
	}
}
