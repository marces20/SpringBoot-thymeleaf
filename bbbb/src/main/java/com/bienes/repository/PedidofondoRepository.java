package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Pedidofondo; 

public interface PedidofondoRepository extends PagingAndSortingRepository<Pedidofondo, Integer> {
	
	@Query("select p from Pedidofondo p where p.id in(?1)")
	public List<Pedidofondo> buscarPorIds(List<Integer> ids);
}
