package com.samyx.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.Moneda;

public interface IMonedaDao extends CrudRepository<Moneda, Long> {
}
