package com.bienes.repository;


import com.bienes.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface UsuariosRepository extends PagingAndSortingRepository<Usuario, Integer> {
	
	@Modifying
    @Query("UPDATE Usuario u SET u.estatus = :paramTipoAccion WHERE u.id = :paramIdUsuario")
    int changeStatus(@Param("paramIdUsuario") int idUsuario,@Param("paramTipoAccion") int tipoAccion);	
	
	@Query("select p from Usuario p where p.username = ?1")
	public List<Usuario> buscarPorUserName(String username);
}
