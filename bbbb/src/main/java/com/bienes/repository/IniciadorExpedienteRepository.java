package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.IniciadorExpediente;
import com.bienes.model.Organigrama;

public interface IniciadorExpedienteRepository extends PagingAndSortingRepository<IniciadorExpediente, Integer> {
	
	@Query("select p from IniciadorExpediente p where p.nombre like %?1%")
	public List<IniciadorExpediente> findByNombre(String nombre);
	
	public List<IniciadorExpediente> findByNombreLikeIgnoreCase(String nombre);
}
