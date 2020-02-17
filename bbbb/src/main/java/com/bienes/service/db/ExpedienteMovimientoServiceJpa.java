package com.bienes.service.db;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienes.model.ExpedienteMovimiento;
import com.bienes.repository.ExpedienteMovimientoRepository;
import com.bienes.service.IExpedienteMovimientoService;

@Service
public class ExpedienteMovimientoServiceJpa implements IExpedienteMovimientoService{
	
	@Autowired 
	private ExpedienteMovimientoRepository expeMoviRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public ExpedienteMovimiento buscarPorId(Integer idExpedienteMovimiento) {
		Optional<ExpedienteMovimiento> optional = expeMoviRepo.findById(idExpedienteMovimiento);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void guardar(ExpedienteMovimiento expedienteMovimiento) {
		expeMoviRepo.save(expedienteMovimiento);
		
	}

	@Override
	public void eliminar(Integer idExpedienteMovimiento) {
		expeMoviRepo.deleteById(idExpedienteMovimiento);
		
	}

	@Override
	public List<ExpedienteMovimiento> getByExpedienteId(Integer idExpediente) {
		return expeMoviRepo.getByExpedienteId(idExpediente);
	}
	
	
}
