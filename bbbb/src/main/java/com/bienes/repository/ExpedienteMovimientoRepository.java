package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.ExpedienteMovimiento;

public interface ExpedienteMovimientoRepository extends PagingAndSortingRepository<ExpedienteMovimiento, Integer> {
	
	@Query("select p from ExpedienteMovimiento p where p.expediente = ?1 ")
	public List<ExpedienteMovimiento> getByExpedienteId(Integer idExpediente);
}
