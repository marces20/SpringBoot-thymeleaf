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
import com.bienes.model.PedidofondoLinea; 
import com.bienes.repository.PedidofondoLineaRepository;
import com.bienes.service.IPedidoFondoLineaService;

@Service
public class PedidofondoLineaServiceJpa implements IPedidoFondoLineaService{
	
	@Autowired
	private PedidofondoLineaRepository pedidofondoLineaRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<PedidofondoLinea> getByPedidoFondo(Pedidofondo p) {
		return pedidofondoLineaRepo.getByPedidoFondo(p);
	}
	
	@Override
	public PedidofondoLinea buscarPorId(Integer idPedidofondoLinea) {
		Optional<PedidofondoLinea> optional = pedidofondoLineaRepo.findById(idPedidofondoLinea);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public void guardar(PedidofondoLinea pedidoFondoLinea) {
		pedidofondoLineaRepo.save(pedidoFondoLinea);		
	}
	
	@Override
	public void eliminar(Integer idPedidofondoLinea) {
		pedidofondoLineaRepo.deleteById(idPedidofondoLinea);		
	}
	

	@Override
	@Transactional(readOnly = true)
	public Page<PedidofondoLinea> findAll(Pageable pageable) {
		return pedidofondoLineaRepo.findAll(pageable);
	}
	
	@Override
	public Page<PedidofondoLinea> getPageByPedidoFondo(Pageable pageable,Pedidofondo pedidoFondo) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PedidofondoLinea> query = cb.createQuery(PedidofondoLinea.class);
        Root<PedidofondoLinea> fl = query.from(PedidofondoLinea.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        
        if(pedidoFondo != null) {
        	predicates.add(cb.equal(fl.get("pedidofondo"), pedidoFondo));
        }
        
        //query.select(usuario).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
         
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
     		
		Integer totalRows = entityManager.createQuery(query).getResultList().size();
        		
        TypedQuery<PedidofondoLinea> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<PedidofondoLinea>(typedQuery.getResultList(), pageable,total);
	}
}
