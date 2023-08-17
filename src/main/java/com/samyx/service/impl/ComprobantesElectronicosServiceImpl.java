package com.samyx.service.impl;

import com.samyx.models.dao.IComprobantesElectronicosDao;
import com.samyx.models.entity.ComprobantesElectronicos;
import com.samyx.service.interfaces.IComprobantesElectronicosService;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ComprobantesElectronicosServiceImpl implements IComprobantesElectronicosService {
	@Autowired
	private IComprobantesElectronicosDao _comprobantesElectronicosDao;

	@Transactional
	public void save(ComprobantesElectronicos entidad) {
		this._comprobantesElectronicosDao.save(entidad);
	}

	public ComprobantesElectronicos findByClaveDocumento(String clave) {
		return this._comprobantesElectronicosDao.findByClaveDocumento(clave);
	}

	public Page<ComprobantesElectronicos> findAllComprobante(String q, Pageable pageable) {
		return this._comprobantesElectronicosDao.findAllComprobante(q, pageable);
	}

	public ComprobantesElectronicos findById(Long id) {
		return this._comprobantesElectronicosDao.findById(id).orElse(null);
	}

	@Transactional
	public void UpdateComprobantesElectronicos(String responseCodeSend, String indEstado, Integer reconsultas,
			Long id) {
		this._comprobantesElectronicosDao.UpdateComprobantesElectronicos(responseCodeSend, indEstado, reconsultas, id);
	}

	public Page<ComprobantesElectronicos> findAllComprobanteByEmisor(String identificacionEmisor, String q,
			Pageable pageable) {
		return this._comprobantesElectronicosDao.findAllComprobanteByEmisor(identificacionEmisor, q, pageable);
	}

	@Transactional
	public void UpdateComprobantesElectronicosApi(String responseCodeSend, String indEstado, String emailDistribucion,
			Integer reconsultas, Long id) {
		this._comprobantesElectronicosDao.UpdateComprobantesElectronicosApi(responseCodeSend, indEstado,
				emailDistribucion, reconsultas, id);
	}
}
