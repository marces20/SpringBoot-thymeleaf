package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Organigrama;

public interface IOrganigramaService {
	
	Organigrama buscarPorId(Integer idOrganigrama);
	
	List<Organigrama> findByNombre(String nombre);
	
	Page<Organigrama> findOrganigramaTodo(String nombre,Pageable pageable);
}
