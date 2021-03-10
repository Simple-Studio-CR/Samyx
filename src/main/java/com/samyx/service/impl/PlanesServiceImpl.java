package com.samyx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samyx.models.dao.IPlanesDao;
import com.samyx.models.entity.Planes;
import com.samyx.service.interfaces.IPlanesService;

@Service
public class PlanesServiceImpl implements IPlanesService{

	@Autowired
	private IPlanesDao _planesDao;
	
	@Override
	public List<Planes> findAll() {
		// TODO Auto-generated method stub
		return (List<Planes>) this._planesDao.findAll();
	}

	@Override
	public Planes findById(Long id) {
		// TODO Auto-generated method stub
		return this._planesDao.findById(id).orElse(null);
	}
	
	

}
