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
import com.bienes.model.FacturaLinea;
import com.bienes.repository.FacturaLineaRepository;
import com.bienes.service.IFacturaLineaService; 

@Service
public class FacturaLineaServiceJpa implements IFacturaLineaService {
	
	@Autowired
	private FacturaLineaRepository facturaLineaRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<FacturaLinea> getByFacturaId(Factura f) {
		return facturaLineaRepo.getByFactura(f);
	}
	
	@Override
	public FacturaLinea buscarPorId(Integer idFacturaLinea) {
		Optional<FacturaLinea> optional = facturaLineaRepo.findById(idFacturaLinea);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public Page<FacturaLinea> getPageByFactura(Pageable pageable,Factura factura) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FacturaLinea> query = cb.createQuery(FacturaLinea.class);
        Root<FacturaLinea> fl = query.from(FacturaLinea.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(factura != null) {
        	predicates.add(cb.equal(fl.get("factura"), factura));
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
        		
        TypedQuery<FacturaLinea> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<FacturaLinea>(typedQuery.getResultList(), pageable,total);
	}

	@Override
	public void guardar(FacturaLinea facturaLinea) {
		facturaLineaRepo.save(facturaLinea);		
	}
	
	@Override
	public void eliminar(Integer idFacturaLinea) {
		facturaLineaRepo.deleteById(idFacturaLinea);		
	}
	

	@Override
	@Transactional(readOnly = true)
	public Page<FacturaLinea> findAll(Pageable pageable) {
		return facturaLineaRepo.findAll(pageable);
	}

	

	 
	
}
