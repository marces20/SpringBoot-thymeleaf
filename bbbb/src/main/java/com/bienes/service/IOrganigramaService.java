package com.bienes.service;

import java.util.List;

import com.bienes.model.Organigrama;

public interface IOrganigramaService {
	
	Organigrama buscarPorId(Integer idOrganigrama);
	
	List<Organigrama> findByNombre(String nombre);
}
