package com.bienes.service;

import java.util.List;

import com.bienes.model.Organigrama;

public interface IOrganigramaService {
	
	List<Organigrama> findByNombre(String nombre);
}
