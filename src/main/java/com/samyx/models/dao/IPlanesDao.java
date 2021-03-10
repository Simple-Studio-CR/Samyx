package com.samyx.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.Planes;

public interface IPlanesDao extends CrudRepository<Planes, Long>{

}
