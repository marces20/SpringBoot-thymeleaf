package com.bienes.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienes.model.Organigrama;
import com.bienes.repository.OrganigramaRepository;
import com.bienes.service.IOrganigramaService;

@Service
public class OrganigramaServiceJpa implements IOrganigramaService{
	
	@Autowired
	private OrganigramaRepository organigramaRepo;

	@Override
	public List<Organigrama> findByNombre(String nombre) {
		return organigramaRepo.findByNombreLikeIgnoreCase("%"+nombre+"%");
	}
	
	
}