package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.IniciadorExpediente;

public interface IIniciadorExpedienteService {
	
	IniciadorExpediente buscarPorId(Integer idIniciador);
	
	List<IniciadorExpediente> findByNombre(String nombre);
	
	Page<IniciadorExpediente> findIniciadorExpedienteTodo(String nombre,Pageable pageable);
}
