package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.FEBitacora;

public interface IFEBitacoraDao extends CrudRepository<FEBitacora, Long> {
	@Query("SELECT c FROM FEBitacora c WHERE c.factura.id = ?1")
	List<FEBitacora> findByIdFactura(Long paramLong);

	@Query("SELECT c FROM FEBitacora c WHERE c.factura.emisor.id = ?1 AND (c.estadoHacienda = '' OR c.estadoHacienda IS NULL)")
	List<FEBitacora> findAllByEmisorId(Long paramLong);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE FEBitacora c SET c.estadoHacienda=?1, fechaAceptacion=?2, nameXmlRespuesta=?3 WHERE c.clave=?4")
	void updateEstadoDocByClave(String paramString1, String paramString2, String paramString3, String paramString4);

	@Query("SELECT COUNT(c) FROM FEBitacora c WHERE c.tipoDocumento = 'FE' AND c.factura.emisor.identificacion = ?1")
	int countFe(String paramString);

	@Query("SELECT COUNT(c) FROM FEBitacora c WHERE c.tipoDocumento = 'ND' AND c.factura.emisor.identificacion = ?1")
	int countNd(String paramString);

	@Query("SELECT COUNT(c) FROM FEBitacora c WHERE c.tipoDocumento = 'NC' AND c.factura.emisor.identificacion = ?1")
	int countNc(String paramString);

	@Query("SELECT COUNT(c) FROM FEBitacora c WHERE c.tipoDocumento = 'TE' AND c.factura.emisor.identificacion = ?1")
	int countTe(String paramString);

	@Query("SELECT COUNT(c) FROM FEBitacora c WHERE c.tipoDocumento = 'FEC' AND c.factura.emisor.identificacion = ?1")
	int countFec(String paramString);

	@Query("SELECT COUNT(c) FROM FEBitacora c WHERE c.tipoDocumento = 'FEE' AND c.factura.emisor.identificacion = ?1")
	int countFee(String paramString);
}