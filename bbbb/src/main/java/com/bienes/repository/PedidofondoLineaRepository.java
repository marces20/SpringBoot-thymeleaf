package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Pedidofondo;
import com.bienes.model.PedidofondoLinea;

public interface PedidofondoLineaRepository extends PagingAndSortingRepository<PedidofondoLinea, Integer> {
	
	@Query("select p from PedidofondoLinea p where p.pedidofondo = ?1 ")
	public List<PedidofondoLinea> getByPedidoFondo(Pedidofondo p); 
}
