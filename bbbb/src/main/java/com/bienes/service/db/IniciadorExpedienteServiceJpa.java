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

import com.bienes.model.IniciadorExpediente;
import com.bienes.model.Organigrama;
import com.bienes.repository.IniciadorExpedienteRepository;
import com.bienes.service.IIniciadorExpedienteService; 

@Service
public class IniciadorExpedienteServiceJpa  implements IIniciadorExpedienteService{
	
	@Autowired 
	private IniciadorExpedienteRepository iniciadorRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<IniciadorExpediente> findByNombre(String nombre) {
		
		return iniciadorRepo.findByNombreLikeIgnoreCaseOrderByNombreAsc("%"+nombre+"%");
	}
	
	@Override
	public IniciadorExpediente buscarPorId(Integer idIniciador) {
		Optional<IniciadorExpediente> optional = iniciadorRepo.findById(idIniciador);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
	public Page<IniciadorExpediente> findIniciadorExpedienteTodo(String nombre, Pageable pageable) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<IniciadorExpediente> query = cb.createQuery(IniciadorExpediente.class);
        Root<IniciadorExpediente> iniciador = query.from(IniciadorExpediente.class);
 
        List<Predicate> predicates = new ArrayList<>();
        
        if(nombre != null) {
        	predicates.add(
        					cb.like(
        							cb.lower(iniciador.get("nombre"))
        							,"%"+nombre.toLowerCase()+"%")
        					);
        }
        
        //query.select(usuario).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
         
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
         
       
		
		Integer totalRows = entityManager.createQuery(query).getResultList().size();
        		
        TypedQuery<IniciadorExpediente> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<IniciadorExpediente>(typedQuery.getResultList(), pageable,total);
	}
	
}
