package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Expediente; 

public interface IExpedienteService {
	
	Expediente buscarPorId(Integer idExpediente);
	
	List<Expediente> buscarPorIds(List<Integer> idsExpediente);
	
	List<Expediente> findByNombre(String nombre);
	
	void guardar(Expediente expediente);
	
	// Ejercicio: Método que elimina un usuario de la base de datos.
	void eliminar(Integer idExpediente);
	
	// Ejercicio: Implementar método que recupera todos los usuarios. Usar vista de listUsuarios.html
	Page<Expediente> findAll(Pageable pageable);
	
	//List<Usuario> findUsuarioTodo(String username);
	Page<Expediente> findExpedienteTodo(String nombre,Pageable pageable);
}
