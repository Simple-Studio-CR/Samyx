package com.samyx.service.interfaces;

import java.util.List;

import com.samyx.models.entity.CCabys;
import com.samyx.models.entity.EmisorCabys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmisorCabysService {
  Page<EmisorCabys> findAllByIdEmisorId(Long paramLong, String paramString, Pageable paramPageable);
  
  List<EmisorCabys> findAll(Long paramLong);
  
  void save(EmisorCabys paramEmisorCabys);
  
  void deleteByIdAndEmisorId(Long paramLong1, Long paramLong2);
  
  List<CCabys> findByNombreCabysIgnoreCase(String paramString1, String paramString2, int paramInt);
  
  CCabys findCCabysById(Long paramLong);
}
