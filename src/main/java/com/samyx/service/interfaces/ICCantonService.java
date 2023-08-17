package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CCanton;

public interface ICCantonService {
	List<CCanton> findAllByIdProvincia(Long paramLong);

	CCanton findById(Long paramLong);
}
