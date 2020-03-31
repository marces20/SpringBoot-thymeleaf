package com.bienes.service;

import java.util.List;

import com.bienes.model.Expediente;
import com.bienes.model.ExpedienteMovimiento;

public interface IExpedienteMovimientoService {
	
	ExpedienteMovimiento buscarPorId(Integer idExpedienteMovimiento);
	
	List<ExpedienteMovimiento> getByExpedienteId(Integer idExpediente);
	
	List<ExpedienteMovimiento> getByCodigo(Integer codigo);
	
	void guardar(ExpedienteMovimiento expedienteMovimiento);
	
	void eliminar(Integer idExpedienteMovimiento);
	
	ExpedienteMovimiento getLastMovimiento(Expediente expediente);
	
	ExpedienteMovimiento getMovimientoAnterior(ExpedienteMovimiento expedienteMovimiento);
	
	String getMovimientoAnteriorEnTiempo(ExpedienteMovimiento expedienteMovimiento);
	
	boolean isLastMovimientoServicioUsuario(Expediente expediente,Integer idOrga);
	
	boolean isLastMovimientoUsuario(Expediente expediente,Integer idUsuario);
	
	String tiempoEnMovimiento(ExpedienteMovimiento f);
	
	Integer getSecuenceCodigo();
}
