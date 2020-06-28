package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Rubro;

public interface IRubroService {
	
	Rubro buscarPorId(Integer idRubro);
	
	List<Rubro> buscarPorIds(List<Integer> idRubro);
	
	void guardar(Rubro rubro);
	
	void eliminar(Integer idRubro);
	
	Page<Rubro> findAll(Pageable pageable);
	
	Page<Rubro> findTodo(String nombre,Pageable pageable);
	
	List<Rubro> findByNombre(String nombre);
}
