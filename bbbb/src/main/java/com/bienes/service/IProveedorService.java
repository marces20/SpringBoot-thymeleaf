package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Proveedor;

public interface IProveedorService {
	
	Proveedor buscarPorId(Integer idProveedor);
	
	List<Proveedor> buscarPorIds(List<Integer> idProveedor);
	
	void guardar(Proveedor proveedor);
	
	void eliminar(Integer idProveedor);
	
	Page<Proveedor> findAll(Pageable pageable);
	
	Page<Proveedor> findProveedorTodo(String nombre,Pageable pageable);
}	
