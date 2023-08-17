package com.samyx.service.interfaces;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.samyx.models.entity.FEFactura;

public interface IFEFacturaService {
	void save(FEFactura paramFEFactura);

	FEFactura findMaxFacturaByEmisor(Long paramLong);

	Page<FEFactura> findAllByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	Page<FEFactura> findProformasByEmisorId(Long paramLong, String paramString, Pageable paramPageable);

	FEFactura findByEmisorById(Long paramLong1, Long paramLong2);

	void updateEstadoByIdFactura(Long paramLong1, Long paramLong2);

	FEFactura findByEmisorByIdAndClave(Long paramLong, String paramString);

	List<FEFactura> reporteDocumentosGenerados(@Param("emisorId") Long paramLong, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5, String paramString);

	List<FEFactura> reporteDocumentosGeneradosByUser(@Param("emisorId") Long paramLong1, @Param("f1") Date paramDate1,
			@Param("f2") Date paramDate2, @Param("td") List<String> paramList1,
			@Param("estado") List<String> paramList2, @Param("cv") List<String> paramList3,
			@Param("mp") List<String> paramList4, @Param("m") List<String> paramList5,
			@Param("actividad") String paramString, @Param("usuario") Long paramLong2);

	List<FEFactura> reporteDocumentosGeneradosBySucursalAndTerminal(Long paramLong1, Long paramLong2, Long paramLong3,
			Date paramDate1, Date paramDate2, List<String> paramList1, List<String> paramList2, List<String> paramList3,
			List<String> paramList4, List<String> paramList5, String paramString);

	List<FEFactura> reporteDocumentosGeneradosByUserBySucursalAndTerminal(Long paramLong1, Long paramLong2,
			Long paramLong3, Date paramDate1, Date paramDate2, List<String> paramList1, List<String> paramList2,
			List<String> paramList3, List<String> paramList4, List<String> paramList5, String paramString,
			Long paramLong4);

	List<FEFactura> reporteDocumentosGeneradosFEC(Long paramLong, Date paramDate1, Date paramDate2,
			List<String> paramList1, List<String> paramList2, List<String> paramList3, List<String> paramList4,
			List<String> paramList5);

	List<FEFactura> reporteDocumentosGeneradosFECBySucursalAndTerminal(Long paramLong1, Long paramLong2,
			Long paramLong3, Date paramDate1, Date paramDate2, List<String> paramList1, List<String> paramList2,
			List<String> paramList3, List<String> paramList4, List<String> paramList5);

	List<FEFactura> reporteDocumentosGeneradosFECByUser(Long paramLong1, Date paramDate1, Date paramDate2,
			List<String> paramList1, List<String> paramList2, List<String> paramList3, List<String> paramList4,
			List<String> paramList5, Long paramLong2);

	List<FEFactura> reporteDocumentosGeneradosFECByUserBySucursalAndTerminal(Long paramLong1, Long paramLong2,
			Long paramLong3, Date paramDate1, Date paramDate2, List<String> paramList1, List<String> paramList2,
			List<String> paramList3, List<String> paramList4, List<String> paramList5, Long paramLong4);

	Double totalVentasByCondicionVentaAndMedioPago(Long paramLong1, Long paramLong2, String paramString1,
			String paramString2, Date paramDate, String paramString3, List<String> paramList);

	Double totalVentasByCondicionVentaCredito(Long paramLong1, Long paramLong2, String paramString1, Date paramDate,
			String paramString2);

	Double devolucionesNc(Long paramLong1, Long paramLong2, Date paramDate, String paramString);
}