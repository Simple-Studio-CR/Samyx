package com.samyx.service.interfaces;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.Producto;

public interface ICProductoService {
	Page<Producto> findAllByIdEmpresaId(Long paramLong, String paramString1, String paramString2,
			Pageable paramPageable);

	void save(Producto paramProducto);

	void deleteByIdByEmpresaId(Long paramLong1, Long paramLong2);

	Producto findByIdByEmpresaId(Long paramLong1, Long paramLong2);

	List<Producto> findByCodigoAndNombreProductoIgnoreCase(Long paramLong, String paramString1, String paramString2,
			int paramInt);

	List<Producto> findByCodigoProductoIgnoreCase(Long paramLong, String paramString);

	Producto findById(Long paramLong);

	void registroEntradasInventUpdatePrecios(Double paramDouble1, Double paramDouble2, Double paramDouble3,
			Double paramDouble4, Double paramDouble5, Double paramDouble6, Double paramDouble7, Long paramLong);

	void registroEntradasInvent(Double paramDouble, Long paramLong);

	void registroSalidasInvent(Double paramDouble, Long paramLong);

	List<Producto> reportePorExistencia(Long paramLong, String paramString, Double paramDouble);

	List<Producto> reporteProductosTodo(Long paramLong);

	void aplicarInventarioMr(Double paramDouble1, Double paramDouble2, Double paramDouble3, Double paramDouble4,
			Double paramDouble5, Double paramDouble6, Double paramDouble7, Long paramLong1, Long paramLong2);

	void aplicarInventarioMrLinea(String paramString, Double paramDouble1, Double paramDouble2, Double paramDouble3,
			Double paramDouble4, Long paramLong1, Long paramLong2, Date paramDate, Double paramDouble5,
			Long paramLong3);
}
