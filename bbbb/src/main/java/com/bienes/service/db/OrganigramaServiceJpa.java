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

import com.bienes.model.Expediente;
import com.bienes.model.Organigrama;
import com.bienes.repository.OrganigramaRepository;
import com.bienes.service.IOrganigramaService;

@Service
public class OrganigramaServiceJpa implements IOrganigramaService{
	
	@Autowired
	private OrganigramaRepository organigramaRepo;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Organigrama> findByNombre(String nombre) {
		return organigramaRepo.findByNombreLikeIgnoreCaseOrderByNombreAsc("%"+nombre+"%");
	}

	@Override
	public Organigrama buscarPorId(Integer idOrganigrama) {
		Optional<Organigrama> optional = organigramaRepo.findById(idOrganigrama);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public Page<Organigrama> findOrganigramaTodo(String nombre, Pageable pageable) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Organigrama> query = cb.createQuery(Organigrama.class);
        Root<Organigrama> organigrama = query.from(Organigrama.class);
 
        List<Predicate> predicates = new ArrayList<>();
        
        if(nombre != null) {
        	predicates.add(
        					cb.like(
        							cb.lower(organigrama.get("nombre"))
        							,"%"+nombre.toLowerCase()+"%")
        					);
        }
        
        //query.select(usuario).where(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        
        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
         
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
         
       
		
		Integer totalRows = entityManager.createQuery(query).getResultList().size();
        		
        TypedQuery<Organigrama> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        Long total = totalRows.longValue();
         
        return new PageImpl<Organigrama>(typedQuery.getResultList(), pageable,total);
	}
	
	
	
}
