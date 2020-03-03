package com.bienes.service.db;

import com.bienes.service.IUsuariosService;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import com.bienes.model.Usuario;
import com.bienes.repository.UsuariosRepository; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; 

@Service
public class UsuariosServiceJpa implements IUsuariosService {
	
	@Autowired
	private UsuariosRepository usuariosRepo; 
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public Usuario buscarPorId(Integer idUsuario) {
		Optional<Usuario> optional = usuariosRepo.findById(idUsuario);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public Usuario buscarPorUserName(String userName) {
		List<Usuario> optional = usuariosRepo.buscarPorUserName(userName);
		if (!optional.isEmpty()) {
			return optional.get(0);
		}
		return null;
	}
	
	public void guardar(Usuario usuario) {
		usuariosRepo.save(usuario);
	}

	public void eliminar(Integer idUsuario) {
		usuariosRepo.deleteById(idUsuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> findAll(Pageable pageable) {
		return usuariosRepo.findAll(pageable);
	}
	
	@Transactional
	@Override
	public int changeStatus(int idUsuario,int tipoAccion) {
		return usuariosRepo.changeStatus(idUsuario,tipoAccion);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> findUsuarioTodo(String username,String nombre,Integer estatus,Pageable pageable) {
		
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
        Root<Usuario> usuario = query.from(Usuario.class);
 
       // Path<String> usernamePath = usuario.get("username");
 
        List<Predicate> predicates = new ArrayList<>();
        
        if(username != null){
        	predicates.add(cb.like(usuario.get("username"),"%"+username+"%"));
        }
        
        if(nombre != null) {
        	predicates.add(cb.like(usuario.get("nombre"),"%"+nombre+"%"));
        }
        
        if(estatus != null) {
        	predicates.add(cb.equal(usuario.get("estatus"), estatus));
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
        		
        TypedQuery<Usuario> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
        
        
        
        Long total = totalRows.longValue();
         
        
        return new PageImpl<Usuario>(typedQuery.getResultList(), pageable,total);
         
    }
	
	public Usuario getUserLogged() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		if(userDetails.getUsername() != null) {
			return buscarPorUserName(userDetails.getUsername());
		}else {
			return null;
		}
	}
	
	
}	

