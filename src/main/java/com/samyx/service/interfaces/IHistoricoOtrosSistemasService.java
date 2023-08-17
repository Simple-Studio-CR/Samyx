package com.samyx.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.HistoricoOtrosSistemas;

public interface IHistoricoOtrosSistemasService {
	Page<HistoricoOtrosSistemas> findAllByIdEmisorAndId(Long paramLong, String paramString, Pageable paramPageable);

	HistoricoOtrosSistemas findByIdEmisorAndId(Long paramLong1, Long paramLong2);
}
