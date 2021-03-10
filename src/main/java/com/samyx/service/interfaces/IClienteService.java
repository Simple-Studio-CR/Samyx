package com.samyx.service.interfaces;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.samyx.models.entity.Cliente;

public interface IClienteService {
	void updateCorreo(String paramString, Long paramLong);

	Page<Cliente> findAllByIdUserId(Long paramLong, String paramString1, String paramString2, Pageable paramPageable);

	void save(Cliente paramCliente);

	void deleteByIdByUserIdAndIdEmisor(Long paramLong1, Long paramLong2);

	Cliente findByIdByUserId(Long paramLong1, Long paramLong2, String paramString);

	List<Cliente> findByIdentificacionOrNombreCompleto(Long paramLong, String paramString1, String paramString2);

	List<Cliente> findAllClienteProveedor(Long paramLong, String paramString);
}
