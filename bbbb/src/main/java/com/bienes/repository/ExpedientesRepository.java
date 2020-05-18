package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.bienes.model.Expediente;

public interface ExpedientesRepository extends PagingAndSortingRepository<Expediente, Integer> {
	
	@Query("select p from Expediente p where p.id in(?1)")
	public List<Expediente> buscarPorIds(List<Integer> ids);
	
	public List<Expediente> findByNombreLikeIgnoreCaseOrderByNombreAsc(String nombre);
}
