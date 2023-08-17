package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.Planes;

public interface IPlanesService {

	List<Planes> findAll();

	Planes findById(Long paramLong);
}
