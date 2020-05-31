package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Factura;
import com.bienes.model.FacturaLinea;

public interface FacturaLineaRepository extends PagingAndSortingRepository<FacturaLinea, Integer> {
	
	@Query("select p from FacturaLinea p where p.factura = ?1 ")
	public List<FacturaLinea> getByFactura(Factura f); 
}
