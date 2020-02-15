package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Organigrama;

public interface OrganigramaRepository extends PagingAndSortingRepository<Organigrama, Integer> {
	
	@Query("select p from Organigrama p where p.nombre like %?1%")
	public List<Organigrama> findByNombre(String nombre);
	
	public List<Organigrama> findByNombreLikeIgnoreCase(String nombre);
}
