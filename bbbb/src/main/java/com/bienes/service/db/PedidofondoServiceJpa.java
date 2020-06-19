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

import com.bienes.model.Pedidofondo;
import com.bienes.repository.PedidofondoRepository;
import com.bienes.service.IPedidofondoService;

@Service
public class PedidofondoServiceJpa implements IPedidofondoService {
	
	@Autowired
	private PedidofondoRepository pedidofondoRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Pedidofondo buscarPorId(Integer idPedidofondo) {
		Optional<Pedidofondo> optional = pedidofondoRepo.findById(idPedidofondo);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<Pedidofondo> buscarPorIds(List<Integer> idsPedidofondo) {
		List<Pedidofondo> r = pedidofondoRepo.buscarPorIds(idsPedidofondo);
		if(r.size() > 0) {
			return r;
		}else {
			return null;
		}
	}
	
	@Override
	public void guardar(Pedidofondo pedidofondo) {
		pedidofondoRepo.save(pedidofondo);		
	}

	@Override
	public void eliminar(Integer idPedidofondo) {
		pedidofondoRepo.deleteById(idPedidofondo);		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Pedidofondo> findAll(Pageable pageable) {
		return pedidofondoRepo.findAll(pageable);
	}
	
	@Override
	public Page<Pedidofondo> findTodo(String nombre, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedidofondo> query = cb.createQuery(Pedidofondo.class);
        Root<Pedidofondo> pedidofondo = query.from(Pedidofondo.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(nombre != null) {
        	predicates.add(cb.like(pedidofondo.get("nombre"),"%"+nombre+"%"));
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
        		
        TypedQuery<Pedidofondo> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Pedidofondo>(typedQuery.getResultList(), pageable,total);
	}
}
