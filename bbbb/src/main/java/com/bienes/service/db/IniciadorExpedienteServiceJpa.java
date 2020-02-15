package com.bienes.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienes.model.IniciadorExpediente;
import com.bienes.repository.IniciadorExpedienteRepository;
import com.bienes.service.IIniciadorExpedienteService; 

@Service
public class IniciadorExpedienteServiceJpa  implements IIniciadorExpedienteService{
	
	@Autowired 
	private IniciadorExpedienteRepository iniciadorRepo;
	
	@Override
	public List<IniciadorExpediente> findByNombre(String nombre) {
		
		return iniciadorRepo.findByNombreLikeIgnoreCase("%"+nombre+"%");
	}
	
	
}
