package com.samyx.models.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.FEFactura;

public interface IFEFacturaDao extends PagingAndSortingRepository<FEFactura, Long> {
	@Query("SELECT MAX(c) FROM FEFactura c WHERE c.emisor.id = ?1")
	FEFactura findMaxFacturaByEmisor(Long paramLong);

	@Query("SELECT c FROM FEFactura c LEFT JOIN c.cliente cl WHERE c.emisor.id = ?1 AND c.estado != 'P' AND (CAST(c.numeroFactura as string) LIKE %?2% OR UPPER(c.cliente.nombreCompleto) LIKE %?2% OR c.cliente.identificacion LIKE %?2% OR c.clave LIKE %?2%)")
	Page<FEFactura> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT c FROM FEFactura c LEFT JOIN c.cliente cl WHERE c.emisor.id = ?1 AND c.estado='P' AND (CAST(c.numeroFactura as string) LIKE %?2% OR UPPER(c.cliente.nombreCompleto) LIKE %?2% OR c.cliente.identificacion LIKE %?2% OR c.clave LIKE %?2%) ORDER BY c.numeroFactura DESC")
	Page<FEFactura> findProformasByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id =?1 AND c.id=?2")
	FEFactura findByEmisorById(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id =?1 AND c.clave=?2")
	FEFactura findByEmisorByIdAndClave(Long paramLong, String paramString);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE FEFactura c SET c.estado='N' WHERE c.emisor.id=?1 AND c.id=?2")
	void updateEstadoByIdFactura(Long paramLong1, Long paramLong2);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id = :emisorId AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m)) AND c.codigoActividadEmisor=:actividad")
	List<FEFactura> reporteDocumentosGenerados(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5,
			@Param("actividad") String paramString);

	@Query("SELECT c FROM FEFactura c JOIN FETCH c.sucursal s JOIN FETCH c.terminal t WHERE c.emisor.id = :emisorId AND s.id=:sucursal AND t.id=:terminal AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m)) AND c.codigoActividadEmisor=:actividad")
	List<FEFactura> reporteDocumentosGeneradosBySucursalAndTerminal(@Param("emisorId") Long paramLong1,
			@Param("sucursal") Long paramLong2, @Param("terminal") Long paramLong3, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5,
			@Param("actividad") String paramString);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id = :emisorId AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m)) AND c.codigoActividadEmisor=:actividad AND c.usuario.id = :usuario")
	List<FEFactura> reporteDocumentosGeneradosByUser(@Param("emisorId") Long paramLong1, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5,
			@Param("actividad") String paramString, @Param("usuario") Long paramLong2);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id = :emisorId  AND c.sucursal.id=:sucursal AND c.terminal.id=:terminal AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m)) AND c.codigoActividadEmisor=:actividad AND c.usuario.id = :usuario")
	List<FEFactura> reporteDocumentosGeneradosByUserBySucursalAndTerminal(@Param("emisorId") Long paramLong1,
			@Param("sucursal") Long paramLong2, @Param("terminal") Long paramLong3, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5,
			@Param("actividad") String paramString, @Param("usuario") Long paramLong4);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id = :emisorId AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m))")
	List<FEFactura> reporteDocumentosGeneradosAll(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id = :emisorId AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m))")
	List<FEFactura> reporteDocumentosGeneradosFEC(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5);

	@Query("SELECT c FROM FEFactura c JOIN FETCH c.sucursal s JOIN FETCH c.terminal t WHERE c.emisor.id = :emisorId AND s.id=:sucursal AND t.id=:terminal AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m))")
	List<FEFactura> reporteDocumentosGeneradosFECBySucursalAndTerminal(@Param("emisorId") Long paramLong1,
			@Param("sucursal") Long paramLong2, @Param("terminal") Long paramLong3, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id = :emisorId AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m)) AND c.usuario.id = :usuario")
	List<FEFactura> reporteDocumentosGeneradosFECByUser(@Param("emisorId") Long paramLong1,
			@Param("f1") Date paramDate1, @Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5,
			@Param("usuario") Long paramLong2);

	@Query("SELECT c FROM FEFactura c WHERE c.emisor.id = :emisorId  AND c.sucursal.id=:sucursal AND c.terminal.id=:terminal AND (c.fechaEmisionFe BETWEEN :f1 AND :f2 AND c.tipoDocumento IN(:td) AND c.estado IN(:estado) AND c.condVenta IN(:cv) AND c.medioPago IN(:mp) AND c.moneda.simboloMoneda IN(:m)) AND c.usuario.id = :usuario")
	List<FEFactura> reporteDocumentosGeneradosFECByUserBySucursalAndTerminal(@Param("emisorId") Long paramLong1,
			@Param("sucursal") Long paramLong2, @Param("terminal") Long paramLong3, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5,
			@Param("usuario") Long paramLong4);

	@Query("SELECT SUM(c.totalComprobante) FROM FEFactura c WHERE c.emisor.id =?1 AND c.usuario.id=?2 AND c.condVenta=?3 AND c.medioPago=?4 AND c.fechaEmisionFe >= ?5 AND c.moneda.simboloMoneda=?6 and c.estado ='A' AND c.tipoDocumento IN(?7)")
	Double totalVentasByCondicionVentaAndMedioPago(Long paramLong1, Long paramLong2, String paramString1,
			String paramString2, Date paramDate, String paramString3, List<String> paramList);

	@Query("SELECT SUM(c.totalComprobante) FROM FEFactura c WHERE c.emisor.id =?1 AND c.usuario.id=?2 AND c.fechaEmisionFe >= ?3 AND c.moneda.simboloMoneda=?4 AND c.estado ='A' AND UPPER(c.tipoDocumento) = 'NC'")
	Double devolucionesNc(Long paramLong1, Long paramLong2, Date paramDate, String paramString);

	@Query("SELECT SUM(c.totalComprobante) FROM FEFactura c WHERE c.emisor.id =?1 AND c.usuario.id=?2 AND c.condVenta=?3 AND c.fechaEmisionFe >= ?4 AND c.moneda.simboloMoneda=?5 and c.estado ='A'")
	Double totalVentasByCondicionVentaCredito(Long paramLong1, Long paramLong2, String paramString1, Date paramDate,
			String paramString2);
}
