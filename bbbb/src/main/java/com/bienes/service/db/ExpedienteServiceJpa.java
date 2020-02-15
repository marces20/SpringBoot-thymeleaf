package com.bienes.service.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bienes.model.Expediente;
import com.bienes.repository.ExpedientesRepository;
import com.bienes.service.IExpedienteService;

@Service
public class ExpedienteServiceJpa implements IExpedienteService {
	
	@Autowired
	private ExpedientesRepository expedienteRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Expediente buscarPorId(Integer idExpediente) {
		Optional<Expediente> optional = expedienteRepo.findById(idExpediente);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void guardar(Expediente expediente) {
		expedienteRepo.save(expediente);
		
	}

	@Override
	public void eliminar(Integer idExpediente) {
		expedienteRepo.deleteById(idExpediente);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Expediente> findAll(Pageable pageable) {
		return expedienteRepo.findAll(pageable);
	}

	@Override
	public Page<Expediente> findExpedienteTodo(String nombre, Pageable pageable) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Expediente> query = cb.createQuery(Expediente.class);
        Root<Expediente> expediente = query.from(Expediente.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(nombre != null) {
        	predicates.add(cb.like(expediente.get("nombre"),"%"+nombre+"%"));
        }
        
        //query.select(usuario).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
         
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
         
        System.out.println("--------totalRegistrosPorPagina-------- "+totalRegistrosPorPagina);
		System.out.println("--------paginaAtual-------- "+paginaAtual);
		System.out.println("------primeiroRegistro---------- "+primeiroRegistro);
		
		Integer totalRows = entityManager.createQuery(query).getResultList().size();
        		
        TypedQuery<Expediente> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Expediente>(typedQuery.getResultList(), pageable,total);
	}

}
