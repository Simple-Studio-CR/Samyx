package com.samyx.models.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.FEFacturasCXC;

public interface IFEFacturasCXCDao extends CrudRepository<FEFacturasCXC, Long> {
	@Query("SELECT c FROM FEFacturasCXC c LEFT JOIN c.cliente cl WHERE c.emisor.id = ?1 AND c.estadoPago=?2 AND (UPPER(cl.nombreCompleto) LIKE %?3% OR cl.identificacion LIKE %?3%) GROUP BY cl.identificacion ORDER BY cl.nombreCompleto DESC")
	Page<FEFacturasCXC> findAllCXCCanceladas(Long paramLong, String paramString1, String paramString2,
			Pageable paramPageable);

	@Query(value = "SELECT * FROM cxc_pendientes WHERE emisor_id = ?1 AND estado_pago=?2 AND (UPPER(nombre_completo) LIKE %?3% OR identificacion LIKE %?3%)", nativeQuery = true)
	Page<FEFacturasCXC> findAllCXC(Long paramLong, String paramString1, String paramString2, Pageable paramPageable);

	@Query("SELECT c FROM FEFacturasCXC c WHERE c.clave=?1")
	FEFacturasCXC obtenerIdFECXCPorClave(String paramString);

	@Query(value = "SELECT * FROM cxc_pendientes WHERE emisor_id = ?1 AND cliente_id=?2)", nativeQuery = true)
	FEFacturasCXC estadoClienteParaFacturar(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM FEFacturasCXC c LEFT JOIN c.cliente cl WHERE c.emisor.id = ?1 AND cl.id=?2 AND c.estadoPago=?3 ORDER BY c.fechaVencimientoFe ASC")
	List<FEFacturasCXC> findAllCXCDetalle(Long paramLong1, Long paramLong2, String paramString);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE FEFacturasCXC c SET c.estadoPago=?1 WHERE c.emisor.id=?2 AND c.id=?3")
	void modificaEstadoFacturaCXC(String paramString, Long paramLong1, Long paramLong2);

	@Modifying
	@Query(value = "UPDATE fe_factura_cxc SET estado_pago=?1 WHERE emisor_id=?2 AND numero_factura=?3", nativeQuery = true)
	void anularFacturaCXC(String paramString, Long paramLong1, Long paramLong2);

	@Query(value = "SELECT * FROM cxc_pendientes WHERE emisor_id = ?1 AND cliente_id=?2", nativeQuery = true)
	List<FEFacturasCXC> findAllCXCByCliente(Long paramLong, String paramString);

	@Query(value = "SELECT SUM(monto_abondo) monto_abono FROM cxc_registro_pagos WHERE emisor_id=?1 AND medio_de_pago=?2 AND user_id=?3 AND fecha_registro_pago >= ?4 AND moneda=?5", nativeQuery = true)
	Double findAllCXCPagadas(Long paramLong1, String paramString1, Long paramLong2, Date paramDate,
			String paramString2);
}