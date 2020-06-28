package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Rubro;

public interface RubroRepository extends PagingAndSortingRepository<Rubro, Integer> {
	
	@Query("select p from Rubro p where p.id in(?1)")
	public List<Rubro> buscarPorIds(List<Integer> ids);
	
	public List<Rubro> findByNombreLikeIgnoreCaseOrderByNombreAsc(String nombre);
}
