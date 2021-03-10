package com.samyx.service.impl;

import com.samyx.models.dao.IFEMensajeReceptorDao;
import com.samyx.models.entity.FEMensajeReceptor;
import com.samyx.models.entity.Usuario;
import com.samyx.service.interfaces.IFEMensajeReceptorService;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FEMensajeReceptorServiceImpl implements IFEMensajeReceptorService {
	@Autowired
	private IFEMensajeReceptorDao _dao;

	@Transactional
	public void save(FEMensajeReceptor entity) {
		this._dao.save(entity);
	}

	public Page<FEMensajeReceptor> findAllByEmisorId(Long emisorId, String q, Pageable pageable) {
		return this._dao.findAllByEmisorId(emisorId, q, pageable);
	}

	public FEMensajeReceptor findMrByEmisorIdAndId(Long emisorId, Long id) {
		return this._dao.findMrByEmisorIdAndId(emisorId, id);
	}

	public List<FEMensajeReceptor> findAllByEmisorId(Long id) {
		return this._dao.findAllByEmisorId(id);
	}

	@Transactional
	public void updateEstadoDocByClave(String estado, String fechaAceptacion, String fileXmlAceptacion, String clave) {
		this._dao.updateEstadoDocByClave(estado, fechaAceptacion, fileXmlAceptacion, clave);
	}

	public FEMensajeReceptor findMrByClave(String clave) {
		return this._dao.findMrByClave(clave);
	}

	public List<FEMensajeReceptor> findAllMR(Long emisorId, Date f1, Date f2, List<String> estadoHacienda,
			List<String> condicionImpuesto, List<String> m, String actividad) {
		return this._dao.findAllMR(emisorId, f1, f2, estadoHacienda, condicionImpuesto, m, actividad);
	}

	public List<FEMensajeReceptor> findAllMRFechaEmision(Long emisorId, Date f1, Date f2, List<String> estadoHacienda,
			List<String> condicionImpuesto, List<String> m, String actividad) {
		return this._dao.findAllMRFechaEmision(emisorId, f1, f2, estadoHacienda, condicionImpuesto, m, actividad);
	}

	@Transactional
	public void aplicarFacturaCompra(Usuario usuario, String estadoInventario, Date fechaAplicacionInventario,
			Long idMr, Long emisorId) {
		this._dao.aplicarFacturaCompra(usuario, estadoInventario, fechaAplicacionInventario, idMr, emisorId);
	}
}
