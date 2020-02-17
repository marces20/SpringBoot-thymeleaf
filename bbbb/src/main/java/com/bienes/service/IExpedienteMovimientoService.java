package com.bienes.service;

import java.util.List;

import com.bienes.model.ExpedienteMovimiento;

public interface IExpedienteMovimientoService {
	
	ExpedienteMovimiento buscarPorId(Integer idExpedienteMovimiento);
	
	List<ExpedienteMovimiento> getByExpedienteId(Integer idExpediente);
	
	void guardar(ExpedienteMovimiento expedienteMovimiento);
	
	void eliminar(Integer idExpedienteMovimiento);
}
