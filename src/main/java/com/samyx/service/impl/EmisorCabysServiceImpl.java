package com.samyx.service.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.samyx.models.dao.IEmisorCabysDao;
import com.samyx.models.entity.CCabys;
import com.samyx.models.entity.EmisorCabys;
import com.samyx.service.interfaces.IEmisorCabysService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmisorCabysServiceImpl implements IEmisorCabysService {
  @Autowired
  private IEmisorCabysDao _dao;
  
  @PersistenceContext
  EntityManager em;
  
  public Page<EmisorCabys> findAllByIdEmisorId(Long emisorId, String q, Pageable pageable) {
    return this._dao.findAllByIdEmisorId(emisorId, q, pageable);
  }
  
  public void save(EmisorCabys entity) {
    this._dao.save(entity);
  }
  
  @Transactional
  public void deleteByIdAndEmisorId(Long emisorId, Long id) {
    this._dao.deleteByIdAndEmisorId(emisorId, id);
  }
  
  public List<CCabys> findByNombreCabysIgnoreCase(String term, String i, int limiteRegistros) {
    return this.em.createQuery("SELECT c FROM CCabys c WHERE (UPPER(c.productoServicio) LIKE :term OR UPPER(c.codigoHacienda) LIKE :term) AND UPPER(c.impuesto) = :tarifa", CCabys.class)
      
      .setParameter("term", "%" + term.toUpperCase() + "%")
      .setParameter("tarifa", i)
      .setMaxResults(limiteRegistros)
      .getResultList();
  }
  
  public CCabys findCCabysById(Long id) {
    return (CCabys)this.em.createQuery("SELECT c FROM CCabys c WHERE c.id = :id", CCabys.class)
      
      .setParameter("id", id)
      .getSingleResult();
  }
  
  public List<EmisorCabys> findAll(Long emisorId) {
    return this._dao.findAllByEmisorId(emisorId);
  }
}