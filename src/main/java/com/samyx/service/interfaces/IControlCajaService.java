package com.samyx.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.ControlCaja;

public interface IControlCajaService {
	Page<ControlCaja> findByAllByEmisor(Long paramLong, String paramString, Pageable paramPageable);

	Page<ControlCaja> findByAllByEmisorByUserId(Long paramLong1, Long paramLong2, String paramString,
			Pageable paramPageable);

	void save(ControlCaja paramControlCaja);

	ControlCaja findByIdAndEmisorId(Long paramLong1, Long paramLong2);

	Integer countByUsuarioAndEmpresa(Long paramLong1, Long paramLong2);
}
