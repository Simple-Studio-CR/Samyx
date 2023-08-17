package com.samyx.service.interfaces;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.CTipoDeCambio;

public interface ICTipoDeCambioService {
	void save(CTipoDeCambio paramCTipoDeCambio);

	Page<CTipoDeCambio> findAllByEmisorId(Pageable paramPageable);

	List<CTipoDeCambio> findAllDivisas();

	CTipoDeCambio findById(Long paramLong);

	CTipoDeCambio tipoDeCambioPorMonedaAndFecha(String paramString, Date paramDate);
}
