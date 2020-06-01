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

import com.bienes.model.Factura;
import com.bienes.model.Pago;
import com.bienes.repository.PagoRepository;
import com.bienes.service.IPagoService;

@Service
public class PagoServiceJpa implements IPagoService {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private PagoRepository pagoRepo;
	
	@Override
	public Pago buscarPorId(Integer idPago) {
		Optional<Pago> optional = pagoRepo.findById(idPago);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public List<Pago> buscarPorIds(List<Integer> idsPago) {
		List<Pago> r = pagoRepo.buscarPorIds(idsPago);
		if(r.size() > 0) {
			return r;
		}else {
			return null;
		}
	}
	
	@Override
	public void guardar(Pago pago) {
		pagoRepo.save(pago);		
	}

	@Override
	public void eliminar(Integer idPago) {
		pagoRepo.deleteById(idPago);		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Pago> findAll(Pageable pageable) {
		return pagoRepo.findAll(pageable);
	}
	
	@Override
	public Page<Pago> findTodo(String id, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pago> query = cb.createQuery(Pago.class);
        Root<Pago> pago = query.from(Pago.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(id != null) {
        	predicates.add(cb.equal(pago.get("id"),id));
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
        		
        TypedQuery<Pago> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Pago>(typedQuery.getResultList(), pageable,total);
	}
	
}
