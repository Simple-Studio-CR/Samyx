package com.samyx.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.samyx.models.entity.ComprobantesElectronicos;

public interface IComprobantesElectronicosDao extends JpaRepository<ComprobantesElectronicos, Long> {
	@Query("SELECT c FROM ComprobantesElectronicos c WHERE c.clave=?1")
	ComprobantesElectronicos findByClaveDocumento(String paramString);

	@Query("SELECT c FROM ComprobantesElectronicos c WHERE c.clave LIKE %?1% OR c.tipoDocumento LIKE %?1% OR c.identificacion LIKE %?1% OR UPPER(c.indEstado) LIKE %?1% ORDER BY c.id DESC")
	Page<ComprobantesElectronicos> findAllComprobante(String paramString, Pageable paramPageable);

	@Query("SELECT c FROM ComprobantesElectronicos c WHERE c.identificacion=?1 AND (c.clave LIKE %?2% OR c.tipoDocumento LIKE %?2% OR c.identificacion LIKE %?2% OR UPPER(c.indEstado) LIKE %?2%) ORDER BY c.id DESC")
	Page<ComprobantesElectronicos> findAllComprobanteByEmisor(String paramString1, String paramString2,
			Pageable paramPageable);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE ComprobantesElectronicos c SET c.responseCodeSend=?1, c.indEstado=?2, c.reconsultas=?3 WHERE c.id=?4")
	void UpdateComprobantesElectronicos(String paramString1, String paramString2, Integer paramInteger, Long paramLong);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE ComprobantesElectronicos c SET c.responseCodeSend=?1, c.indEstado=?2, c.emailDistribucion=?3, c.reconsultas=?4 WHERE c.id=?5")
	void UpdateComprobantesElectronicosApi(String paramString1, String paramString2, String paramString3,
			Integer paramInteger, Long paramLong);
}
