package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Pago;

public interface IPagoService {
	
	Pago buscarPorId(Integer idPago);
	
	List<Pago> buscarPorIds(List<Integer> idsPago);
	
	void guardar(Pago pago);
	
	void eliminar(Integer idPago);
	
	Page<Pago> findAll(Pageable pageable);
	
	Page<Pago> findTodo(String numero,Pageable pageable);
	 
}
