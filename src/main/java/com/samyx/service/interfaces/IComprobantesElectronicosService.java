package com.samyx.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.ComprobantesElectronicos;

public interface IComprobantesElectronicosService {
	void save(ComprobantesElectronicos paramComprobantesElectronicos);

	void UpdateComprobantesElectronicos(String paramString1, String paramString2, Integer paramInteger, Long paramLong);

	void UpdateComprobantesElectronicosApi(String paramString1, String paramString2, String paramString3,
			Integer paramInteger, Long paramLong);

	ComprobantesElectronicos findByClaveDocumento(String paramString);

	Page<ComprobantesElectronicos> findAllComprobante(String paramString, Pageable paramPageable);

	Page<ComprobantesElectronicos> findAllComprobanteByEmisor(String paramString1, String paramString2,
			Pageable paramPageable);

	ComprobantesElectronicos findById(Long paramLong);
}
