package com.bienes.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bienes.model.Factura;
import com.bienes.model.FacturaLinea;

public interface IFacturaLineaService {
	
	List<FacturaLinea> getByFacturaId(Factura f);
	
	Page<FacturaLinea> getPageByFacturaId(Pageable pageable,Factura factura);
	
	void guardar(FacturaLinea facturaLinea);
	
	void eliminar(Integer idFacturaLinea);
	
	Page<FacturaLinea> findAll(Pageable pageable); 
	
	FacturaLinea buscarPorId(Integer idFacturaLinea);
}
