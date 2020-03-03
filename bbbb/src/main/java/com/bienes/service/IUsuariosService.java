package com.bienes.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 

import com.bienes.model.Usuario;

public interface IUsuariosService {

	Usuario buscarPorId(Integer idUsuario);
	
	Usuario buscarPorUserName(String userName);
	
	void guardar(Usuario usuario);
	
	// Ejercicio: Método que elimina un usuario de la base de datos.
	void eliminar(Integer idUsuario);
	
	// Ejercicio: Implementar método que recupera todos los usuarios. Usar vista de listUsuarios.html
	Page<Usuario> findAll(Pageable pageable);
	
	int changeStatus(int idUsuario,int tipoAccion);
	
	//List<Usuario> findUsuarioTodo(String username);
	Page<Usuario> findUsuarioTodo(String username,String nombre,Integer estatus,Pageable pageable);

	Usuario getUserLogged();
}
