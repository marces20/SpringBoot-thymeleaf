package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Proveedor;

public interface ProveedorRepository extends PagingAndSortingRepository<Proveedor, Integer> {
	
	@Query("select p from Proveedor p where p.id in(?1)")
	public List<Proveedor> buscarPorIds(List<Integer> ids);
	
	public List<Proveedor> findByNombreLikeIgnoreCaseOrderByNombreAsc(String nombre);
}
