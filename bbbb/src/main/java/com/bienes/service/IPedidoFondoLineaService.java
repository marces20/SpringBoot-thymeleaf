package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Pedidofondo;
import com.bienes.model.PedidofondoLinea;

public interface IPedidoFondoLineaService {
	
	List<PedidofondoLinea> getByPedidoFondo(Pedidofondo f);
	
	Page<PedidofondoLinea> getPageByPedidoFondo(Pageable pageable,Pedidofondo pedidoFondo);
	
	void guardar(PedidofondoLinea pedidofondoLinea);
	
	void eliminar(Integer idPedidofondoLinea);
	
	Page<PedidofondoLinea> findAll(Pageable pageable); 
	
	PedidofondoLinea buscarPorId(Integer idPedidofondoLinea);
}
