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
import com.bienes.repository.ProductoRepository;
import com.bienes.service.IProductoService;

@Service
public class ProductoServiceJpa implements IProductoService {
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Producto buscarPorId(Integer idProducto) {
		Optional<Producto> optional = productoRepo.findById(idProducto);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public List<Producto> buscarPorIds(List<Integer> idsProducto) {
		List<Producto> r = productoRepo.buscarPorIds(idsProducto);
		if(r.size() > 0) {
			return r;
		}else {
			return null;
		}
	}

	@Override
	public void guardar(Producto producto) {
		productoRepo.save(producto);
		
	}

	@Override
	public void eliminar(Integer idProducto) {
		productoRepo.deleteById(idProducto);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAll(Pageable pageable) {
		return productoRepo.findAll(pageable);
	}

	@Override
	public Page<Producto> findTodo(String nombre, Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
        Root<Producto> producto = query.from(Producto.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(nombre != null) {
        	predicates.add(cb.like(producto.get("nombre"),"%"+nombre+"%"));
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
        		
        TypedQuery<Producto> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Producto>(typedQuery.getResultList(), pageable,total);
	}
}
