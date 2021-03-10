package com.samyx.models.dao;

import java.util.List;

import com.samyx.models.entity.EmisorCabys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IEmisorCabysDao extends CrudRepository<EmisorCabys, Long> {
  @Query("SELECT c FROM EmisorCabys c INNER JOIN c.emisor e WHERE e.id=?1 AND UPPER(c.productoServicio) LIKE %?2%")
  Page<EmisorCabys> findAllByIdEmisorId(Long paramLong, String paramString, Pageable paramPageable);
  
  @Query("SELECT c FROM EmisorCabys c INNER JOIN c.emisor e WHERE e.id=?1 ORDER BY c.codigoHacienda")
  List<EmisorCabys> findAllByEmisorId(Long paramLong);
  
  @Modifying
  @Query("DELETE FROM EmisorCabys c WHERE c.emisor.id = :emisor AND c.id=:id")
  void deleteByIdAndEmisorId(@Param("emisor") Long paramLong1, @Param("id") Long paramLong2);
}

