package com.samyx.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.samyx.models.entity.FEMensajeReceptorAutomatico;

public interface IFEMensajeReceptorAutomaticoDao extends JpaRepository<FEMensajeReceptorAutomatico, Long> {
	@Query("SELECT c FROM FEMensajeReceptorAutomatico c WHERE (c.receptorTipoIdentificacion=?1 AND c.receptorIdentificacion=?2) AND c.estado=?3 AND ( UPPER(c.emisorFactura) LIKE %?4% OR UPPER(c.emisorIdentificacion) LIKE %?4% OR UPPER(c.clave) LIKE %?4% )")
	Page<FEMensajeReceptorAutomatico> findAllByReceptor(String paramString1, String paramString2, String paramString3,
			String paramString4, Pageable paramPageable);

	@Modifying
	@Query(value = "UPDATE fe_mensaje_receptor_automatico SET estado=?1 WHERE id=?2", nativeQuery = true)
	void updateEstadoMrInbox(String paramString, Long paramLong);
}
