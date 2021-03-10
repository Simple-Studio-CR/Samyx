package com.samyx.service.impl;

import com.samyx.models.dao.IFEFacturaDao;
import com.samyx.models.entity.FEFactura;
import com.samyx.service.interfaces.IFEFacturaService;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class FEFacturaServiceImpl implements IFEFacturaService {
	@Autowired
	private IFEFacturaDao _dao;

	@PersistenceContext
	EntityManager em;

	@Transactional
	public void save(FEFactura entity) {
		this._dao.save(entity);
	}

	public FEFactura findMaxFacturaByEmisor(Long emisorId) {
		return this._dao.findMaxFacturaByEmisor(emisorId);
	}

	public Page<FEFactura> findAllByEmisorId(Long id, String q, Pageable pageable) {
		return this._dao.findAllByEmisorId(id, q, pageable);
	}

	public Page<FEFactura> findProformasByEmisorId(Long id, String q, Pageable pageable) {
		return this._dao.findProformasByEmisorId(id, q, pageable);
	}

	public FEFactura findByEmisorById(Long emisorId, Long id) {
		return this._dao.findByEmisorById(emisorId, id);
	}

	@Transactional
	public void updateEstadoByIdFactura(Long emisorId, Long id) {
		this._dao.updateEstadoByIdFactura(emisorId, id);
	}

	public FEFactura findByEmisorByIdAndClave(Long emisorId, String clave) {
		return this._dao.findByEmisorByIdAndClave(emisorId, clave);
	}

	public List<FEFactura> reporteDocumentosGenerados(@Param("emisorId") Long emisorId, @Param("f1") Date f1,
			@Param("f2") Date f2, @Param("td") List<String> td, @Param("estado") List<String> estado,
			@Param("cv") List<String> cv, @Param("mp") List<String> mp, @Param("m") List<String> m, String actividad) {
		return this._dao.reporteDocumentosGenerados(emisorId, f1, f2, td, estado, cv, mp, m, actividad);
	}

	public List<FEFactura> reporteDocumentosGeneradosByUser(Long emisorId, Date f1, Date f2, List<String> td,
			List<String> estado, List<String> cv, List<String> mp, List<String> m, String actividad, Long usuario) {
		return this._dao.reporteDocumentosGeneradosByUser(emisorId, f1, f2, td, estado, cv, mp, m, actividad, usuario);
	}

	public Double totalVentasByCondicionVentaAndMedioPago(Long emisorId, Long usuarioId, String condicionVenta,
			String medioPago, Date fechaEmisionFe, String moneda, List<String> tiposDocs) {
		return this._dao.totalVentasByCondicionVentaAndMedioPago(emisorId, usuarioId, condicionVenta, medioPago,
				fechaEmisionFe, moneda, tiposDocs);
	}

	public Double totalVentasByCondicionVentaCredito(Long emisorId, Long usuarioId, String condicionVenta,
			Date fechaEmisionFe, String moneda) {
		return this._dao.totalVentasByCondicionVentaCredito(emisorId, usuarioId, condicionVenta, fechaEmisionFe,
				moneda);
	}

	public List<FEFactura> reporteDocumentosGeneradosBySucursalAndTerminal(Long emisorId, Long sucursal, Long terminal,
			Date f1, Date f2, List<String> td, List<String> estado, List<String> cv, List<String> mp, List<String> m,
			String actividad) {
		return this._dao.reporteDocumentosGeneradosBySucursalAndTerminal(emisorId, sucursal, terminal, f1, f2, td,
				estado, cv, mp, m, actividad);
	}

	public List<FEFactura> reporteDocumentosGeneradosByUserBySucursalAndTerminal(Long emisorId, Long sucursal,
			Long terminal, Date f1, Date f2, List<String> td, List<String> estado, List<String> cv, List<String> mp,
			List<String> m, String actividad, Long usuario) {
		return this._dao.reporteDocumentosGeneradosByUserBySucursalAndTerminal(emisorId, sucursal, terminal, f1, f2, td,
				estado, cv, mp, m, actividad, usuario);
	}

	public List<FEFactura> reporteDocumentosGeneradosFEC(Long emisorId, Date f1, Date f2, List<String> td,
			List<String> estado, List<String> cv, List<String> mp, List<String> m) {
		return this._dao.reporteDocumentosGeneradosFEC(emisorId, f1, f2, td, estado, cv, mp, m);
	}

	public List<FEFactura> reporteDocumentosGeneradosFECBySucursalAndTerminal(Long emisorId, Long sucursal,
			Long terminal, Date f1, Date f2, List<String> td, List<String> estado, List<String> cv, List<String> mp,
			List<String> m) {
		return this._dao.reporteDocumentosGeneradosFECBySucursalAndTerminal(emisorId, sucursal, terminal, f1, f2, td,
				estado, cv, mp, m);
	}

	public List<FEFactura> reporteDocumentosGeneradosFECByUser(Long emisorId, Date f1, Date f2, List<String> td,
			List<String> estado, List<String> cv, List<String> mp, List<String> m, Long usuario) {
		return this._dao.reporteDocumentosGeneradosFECByUser(emisorId, f1, f2, td, estado, cv, mp, m, usuario);
	}

	public List<FEFactura> reporteDocumentosGeneradosFECByUserBySucursalAndTerminal(Long emisorId, Long sucursal,
			Long terminal, Date f1, Date f2, List<String> td, List<String> estado, List<String> cv, List<String> mp,
			List<String> m, Long usuario) {
		return this._dao.reporteDocumentosGeneradosFECByUserBySucursalAndTerminal(emisorId, sucursal, terminal, f1, f2,
				td, estado, cv, mp, m, usuario);
	}

	public Double devolucionesNc(Long emisorId, Long usuarioId, Date fechaEmisionFe, String moneda) {
		return this._dao.devolucionesNc(emisorId, usuarioId, fechaEmisionFe, moneda);
	}
}
