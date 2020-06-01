package com.bienes.service.db;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienes.model.Estado; 
import com.bienes.repository.EstadoRepository;
import com.bienes.service.IEstadoService;

@Service
public class EstadoServiceJpa implements IEstadoService{
	@Autowired
	private EstadoRepository estadoRepo;
	
	
	@Override
	public Estado buscarPorId(Integer idEstado) {
		Optional<Estado> optional = estadoRepo.findById(idEstado);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
}
