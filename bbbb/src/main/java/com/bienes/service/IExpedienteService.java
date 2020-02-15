package com.bienes.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Expediente;

public interface IExpedienteService {
	
	Expediente buscarPorId(Integer idExpediente);
	
	void guardar(Expediente expediente);
	
	// Ejercicio: Método que elimina un usuario de la base de datos.
	void eliminar(Integer idExpediente);
	
	// Ejercicio: Implementar método que recupera todos los usuarios. Usar vista de listUsuarios.html
	Page<Expediente> findAll(Pageable pageable);
	
	//List<Usuario> findUsuarioTodo(String username);
	Page<Expediente> findExpedienteTodo(String nombre,Pageable pageable);
}
