package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Remito;

public interface IRemitoService {
	
	Remito buscarPorId(Integer idRemito);
	
	List<Remito> buscarPorIds(List<Integer> idsRemito);
	
	void guardar(Remito remito);
	
	void eliminar(Integer idRemito);
	
	Page<Remito> findAll(Pageable pageable);
	
	Page<Remito> findTodo(String numero,Pageable pageable);
}
