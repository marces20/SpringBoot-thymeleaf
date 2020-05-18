package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Producto; 

public interface ProductoRepository extends PagingAndSortingRepository<Producto, Integer> {
	
	@Query("select p from Producto p where p.id in(?1)")
	public List<Producto> buscarPorIds(List<Integer> ids);
	
	public List<Producto> findByNombreLikeIgnoreCaseOrderByNombreAsc(String nombre);
}
