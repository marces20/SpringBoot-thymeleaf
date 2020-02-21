package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Expediente;
import com.bienes.model.ExpedienteMovimiento;

public interface ExpedienteMovimientoRepository extends PagingAndSortingRepository<ExpedienteMovimiento, Integer> {
	
	@Query("select p from ExpedienteMovimiento p where p.expediente = ?1 ")
	public List<ExpedienteMovimiento> getByExpedienteId(Integer idExpediente);
	
	@Query("select p from ExpedienteMovimiento p where p.expediente = ?1 AND p.cancelado <> true order by id desc")
	public List<ExpedienteMovimiento> getLastMovimiento(Expediente expediente);
	
	@Query("select p from ExpedienteMovimiento p where p.id <> ?1 AND p.expediente = ?2 AND p.cancelado <> true order by id desc")
	public List<ExpedienteMovimiento> getMovimientoAnterior(Integer idMovimiento,Expediente expediente);
	
	 
}
