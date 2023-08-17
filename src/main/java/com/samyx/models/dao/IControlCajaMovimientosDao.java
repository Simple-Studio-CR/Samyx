package com.samyx.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samyx.models.entity.ControlCajaMovimientos;

public interface IControlCajaMovimientosDao extends CrudRepository<ControlCajaMovimientos, Long> {
	@Query("SELECT c FROM ControlCajaMovimientos c JOIN FETCH c.controlCaja cc WHERE cc.emisor.id=?1 AND c.controlCaja.id=?2 ORDER BY c.fechaMovimiento DESC")
	List<ControlCajaMovimientos> findAllByEmisorAndIdCaja(Long paramLong1, Long paramLong2);

	@Query("SELECT SUM(c.montoMovimiento) FROM ControlCajaMovimientos c INNER JOIN c.controlCaja cc INNER JOIN c.moneda m WHERE cc.emisor.id=?1 AND c.tipoMovimiento=?2 AND cc.id=?3 AND m.simboloMoneda=?4")
	Double totalEntradasSalidasEfectivo(Long paramLong1, String paramString1, Long paramLong2, String paramString2);
}
