package com.samyx.service.interfaces;

import java.util.Date;
import java.util.List;

import com.samyx.models.entity.Emisor;

public interface IEmisorService {
	Emisor findEmisorByIdentificacion(String paramString1, String paramString2);

	List<Emisor> findAll();

	Emisor findById(Long paramLong);

	void save(Emisor paramEmisor);

	void updateFechaCriptografica(Date paramDate1, Date paramDate2, Long paramLong);
}
