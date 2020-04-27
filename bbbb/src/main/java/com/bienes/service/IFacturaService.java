package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Factura;

public interface IFacturaService {
	
	Factura buscarPorId(Integer idFactura);
	
	List<Factura> buscarPorIds(List<Integer> idsFactura);
	
	void guardar(Factura factura);
	
	void eliminar(Integer idFactura);
	
	Page<Factura> findAll(Pageable pageable);
	
	Page<Factura> findTodo(String numero,Pageable pageable);
}
