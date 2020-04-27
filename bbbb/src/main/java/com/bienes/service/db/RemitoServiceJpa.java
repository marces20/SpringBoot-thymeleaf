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

import com.bienes.model.Remito;
import com.bienes.repository.RemitoRepository;
import com.bienes.service.IRemitoService;

@Service
public class RemitoServiceJpa implements IRemitoService {
	
	@Autowired
	private RemitoRepository remitoRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Remito buscarPorId(Integer idRemito) {
		Optional<Remito> optional = remitoRepo.findById(idRemito);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<Remito> buscarPorIds(List<Integer> idsRemito) {
		List<Remito> r = remitoRepo.buscarPorIds(idsRemito);
		if(r.size() > 0) {
			return r;
		}else {
			return null;
		}
	}
	
	@Override
	public void guardar(Remito remito) {
		remitoRepo.save(remito);		
	}

	@Override
	public void eliminar(Integer idRemito) {
		remitoRepo.deleteById(idRemito);		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Remito> findAll(Pageable pageable) {
		return remitoRepo.findAll(pageable);
	}
	
	@Override
	public Page<Remito> findTodo(String numero, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Remito> query = cb.createQuery(Remito.class);
        Root<Remito> remito = query.from(Remito.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(numero != null) {
        	predicates.add(cb.like(remito.get("numero"),"%"+numero+"%"));
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
        		
        TypedQuery<Remito> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Remito>(typedQuery.getResultList(), pageable,total);
	}
}
