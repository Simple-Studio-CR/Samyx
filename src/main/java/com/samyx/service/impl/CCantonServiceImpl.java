package com.samyx.service.impl;

import com.samyx.models.dao.ICCantonDao;
import com.samyx.models.entity.CCanton;
import com.samyx.service.interfaces.ICCantonService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CCantonServiceImpl implements ICCantonService {
	@Autowired
	private ICCantonDao _cantonDao;

	public List<CCanton> findAllByIdProvincia(Long id) {
		return this._cantonDao.findAllByIdProvincia(id);
	}

	public CCanton findById(Long id) {
		return this._cantonDao.findById(id).orElse(null);
	}
}
