package com.samyx.models.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.FEMensajeReceptor;
import com.samyx.models.entity.Usuario;

public interface IFEMensajeReceptorDao extends JpaRepository<FEMensajeReceptor, Long> {
	@Query("SELECT c FROM FEMensajeReceptor c INNER JOIN c.usuario user INNER JOIN c.emisor e WHERE c.emisor.id = ?1 AND  ( UPPER(c.emisorMr) LIKE %?2% OR UPPER(c.identificacionEmisorMr) LIKE %?2% OR UPPER(c.clave) LIKE %?2% ) ORDER BY c.id DESC")
	Page<FEMensajeReceptor> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT c FROM FEMensajeReceptor c JOIN FETCH c.usuario user JOIN FETCH c.emisor e WHERE c.emisor.id=?1 AND c.id=?2")
	FEMensajeReceptor findMrByEmisorIdAndId(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM FEMensajeReceptor c WHERE c.clave=?1")
	FEMensajeReceptor findMrByClave(String paramString);

	@Query("SELECT c FROM FEMensajeReceptor c JOIN FETCH c.usuario user JOIN FETCH c.emisor e WHERE c.emisor.id=?1 AND (c.estadoHacienda = '' OR c.estadoHacienda IS NULL)")
	List<FEMensajeReceptor> findAllByEmisorId(Long paramLong);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE FEMensajeReceptor c SET c.estadoHacienda=?1, fechaAceptacion=?2, fileXmlAceptacion=?3 WHERE c.clave=?4")
	void updateEstadoDocByClave(String paramString1, String paramString2, String paramString3, String paramString4);

	@Query("SELECT c FROM FEMensajeReceptor c JOIN FETCH c.emisor e WHERE e.id=:emisorId AND (c.fechaCreacion BETWEEN :f1 AND :f2 AND c.estadoHacienda IN (:estadoHacienda) AND c.condicionImpuesto IN(:condicionImpuesto) AND c.moneda IN(:moneda)) AND c.codigoActividadEmisor=:actividad")
	List<FEMensajeReceptor> findAllMR(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("estadoHacienda") List<String> paramList1,
			@Param("condicionImpuesto") List<String> paramList2, @Param("moneda") List<String> paramList3,
			@Param("actividad") String paramString);

	@Query("SELECT c FROM FEMensajeReceptor c JOIN FETCH c.emisor e WHERE e.id=:emisorId AND (CAST(c.fechaEmision as date) BETWEEN :f1 AND :f2 AND c.estadoHacienda IN (:estadoHacienda) AND c.condicionImpuesto IN(:condicionImpuesto) AND c.moneda IN(:moneda)) AND c.codigoActividadEmisor=:actividad")
	List<FEMensajeReceptor> findAllMRFechaEmision(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("estadoHacienda") List<String> paramList1,
			@Param("condicionImpuesto") List<String> paramList2, @Param("moneda") List<String> paramList3,
			@Param("actividad") String paramString);

	@Modifying
	@Query("UPDATE FEMensajeReceptor c SET c.usuarioAplico = ?1, c.estadoInventario=?2, c.fechaAplicacionInventario=?3 WHERE c.id=?4 AND c.emisor.id =?5")
	void aplicarFacturaCompra(Usuario paramUsuario, String paramString, Date paramDate, Long paramLong1,
			Long paramLong2);
}
