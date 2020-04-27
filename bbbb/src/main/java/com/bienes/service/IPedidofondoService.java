package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Pedidofondo;

public interface IPedidofondoService {
	
	Pedidofondo buscarPorId(Integer idPedidofondo);
	
	List<Pedidofondo> buscarPorIds(List<Integer> idsPedidofondo);
	
	void guardar(Pedidofondo pedidofondo);
	
	void eliminar(Integer idPedidofondo);
	
	Page<Pedidofondo> findAll(Pageable pageable);
	
	Page<Pedidofondo> findTodo(String numero,Pageable pageable);
}
