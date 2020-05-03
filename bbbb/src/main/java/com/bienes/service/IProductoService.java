package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Producto; 

public interface IProductoService {
	
	Producto buscarPorId(Integer idProducto);
	
	List<Producto> buscarPorIds(List<Integer> idProducto);
	
	void guardar(Producto producto);
	
	void eliminar(Integer idProducto);
	
	Page<Producto> findAll(Pageable pageable);
	
	Page<Producto> findTodo(String nombre,Pageable pageable);
}
