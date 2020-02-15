package com.bienes.service;

import com.bienes.model.ExpedienteMovimiento;

public interface IExpedienteMovimientoService {
	
	ExpedienteMovimiento buscarPorId(Integer idExpedienteMovimiento);
	
	void guardar(ExpedienteMovimiento expedienteMovimiento);
	
	void eliminar(Integer idExpedienteMovimiento);
}
