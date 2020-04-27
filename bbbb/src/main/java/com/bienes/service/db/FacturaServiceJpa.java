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
import com.bienes.repository.FacturaRepository;
import com.bienes.service.IFacturaService;

@Service
public class FacturaServiceJpa implements IFacturaService {
	
	@Autowired
	private FacturaRepository facturaRepo;
	
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public Factura buscarPorId(Integer idFactura) {
		Optional<Factura> optional = facturaRepo.findById(idFactura);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<Factura> buscarPorIds(List<Integer> idsFactura) {
		List<Factura> r = facturaRepo.buscarPorIds(idsFactura);
		if(r.size() > 0) {
			return r;
		}else {
			return null;
		}
	}

	@Override
	public void guardar(Factura factura) {
		facturaRepo.save(factura);		
	}

	@Override
	public void eliminar(Integer idFactura) {
		facturaRepo.deleteById(idFactura);		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Factura> findAll(Pageable pageable) {
		return facturaRepo.findAll(pageable);
	}

	@Override
	public Page<Factura> findTodo(String numero, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Factura> query = cb.createQuery(Factura.class);
        Root<Factura> factura = query.from(Factura.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(numero != null) {
        	predicates.add(cb.like(factura.get("numero"),"%"+numero+"%"));
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
        		
        TypedQuery<Factura> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Factura>(typedQuery.getResultList(), pageable,total);
	}	
}
