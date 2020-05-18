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

import com.bienes.model.Proveedor; 
import com.bienes.repository.ProveedorRepository;
import com.bienes.service.IProveedorService;

@Service
public class ProveedorServiceJpa  implements IProveedorService {
	
	@Autowired
	private ProveedorRepository proveedorRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Proveedor buscarPorId(Integer idProveedor) {
		Optional<Proveedor> optional = proveedorRepo.findById(idProveedor);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public List<Proveedor> buscarPorIds(List<Integer> idsProveedor) {
		List<Proveedor> r = proveedorRepo.buscarPorIds(idsProveedor);
		if(r.size() > 0) {
			return r;
		}else {
			return null;
		}
	}

	@Override
	public void guardar(Proveedor proveedor) {
		proveedorRepo.save(proveedor);
		
	}

	@Override
	public void eliminar(Integer idProveedor) {
		proveedorRepo.deleteById(idProveedor);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Proveedor> findAll(Pageable pageable) {
		return proveedorRepo.findAll(pageable);
	}
	
	@Override
	public List<Proveedor> findByNombre(String nombre) {
		return proveedorRepo.findByNombreLikeIgnoreCaseOrderByNombreAsc("%"+nombre+"%");
	}

	@Override
	public Page<Proveedor> findProveedorTodo(String nombre, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Proveedor> query = cb.createQuery(Proveedor.class);
        Root<Proveedor> proveedor = query.from(Proveedor.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(nombre != null) {
        	predicates.add(cb.like(proveedor.get("nombre"),"%"+nombre+"%"));
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
        		
        TypedQuery<Proveedor> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Proveedor>(typedQuery.getResultList(), pageable,total);
	}

}
