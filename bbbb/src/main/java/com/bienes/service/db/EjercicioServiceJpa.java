package com.bienes.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bienes.model.Ejercicio;
import com.bienes.repository.EjercicioRepository;
import com.bienes.service.IEjercicioService;

@Service
public class EjercicioServiceJpa implements IEjercicioService{
	
	@Autowired
	private EjercicioRepository repoEjercicio;

	@Override
	public List<Ejercicio> findAll() {
		return (List<Ejercicio>) repoEjercicio.findAll(Sort.by("id").descending());
	}
}
