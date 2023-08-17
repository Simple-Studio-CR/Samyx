package com.samyx.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.FEFacturaItem;

public interface IFEItemFacturaDao extends CrudRepository<FEFacturaItem, Long> {
}
