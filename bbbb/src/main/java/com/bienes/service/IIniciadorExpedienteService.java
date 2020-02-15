package com.bienes.service;

import java.util.List;

import com.bienes.model.IniciadorExpediente;

public interface IIniciadorExpedienteService {
	
	List<IniciadorExpediente> findByNombre(String nombre);
}
