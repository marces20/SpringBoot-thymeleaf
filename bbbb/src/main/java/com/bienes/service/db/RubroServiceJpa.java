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

import com.bienes.model.Producto;
import com.bienes.model.Rubro;
import com.bienes.repository.RubroRepository;
import com.bienes.service.IRubroService;

@Service
public class RubroServiceJpa implements IRubroService{
	
	@Autowired
	private RubroRepository rubroRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Rubro buscarPorId(Integer idRubro) {
		Optional<Rubro> optional = rubroRepo.findById(idRubro);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public List<Rubro> buscarPorIds(List<Integer> idsRubro) {
		List<Rubro> r = rubroRepo.buscarPorIds(idsRubro);
		if(r.size() > 0) {
			return r;
		}else {
			return null;
		}
	}
	
	@Override
	public void guardar(Rubro rubro) {
		rubroRepo.save(rubro);
		
	}

	@Override
	public void eliminar(Integer idRubro) {
		rubroRepo.deleteById(idRubro);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Rubro> findAll(Pageable pageable) {
		return rubroRepo.findAll(pageable);
	}
	
	@Override
	public List<Rubro> findByNombre(String nombre) {
		return rubroRepo.findByNombreLikeIgnoreCaseOrderByNombreAsc("%"+nombre+"%");
	}
	
	@Override
	public Page<Rubro> findTodo(String nombre, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rubro> query = cb.createQuery(Rubro.class);
        Root<Rubro> rubro = query.from(Rubro.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(nombre != null) {
        	predicates.add(cb.like(cb.lower(rubro.get("nombre")),"%"+nombre.toLowerCase()+"%"));
        }
        
        //query.select(usuario).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
         
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
         
        System.out.println("--------totalRegistrosPorPagina-------- "+totalRegistrosPorPagina);
		System.out.println("--------paginaAtual-------- "+paginaAtual);
		System.out.println("------primeiroRegistro---------- "+primeiroRegistro);
		System.out.println("------nombre---------- "+nombre);
		System.out.println("------query---------- "+query);
		
		
		Integer totalRows = entityManager.createQuery(query).getResultList().size();
        		
        TypedQuery<Rubro> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Rubro>(typedQuery.getResultList(), pageable,total);
	}
	
}
