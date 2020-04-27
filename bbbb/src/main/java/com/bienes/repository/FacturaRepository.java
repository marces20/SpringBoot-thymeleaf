package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Factura; 

public interface FacturaRepository extends PagingAndSortingRepository<Factura, Integer> {
	
	@Query("select p from Factura p where p.id in(?1)")
	public List<Factura> buscarPorIds(List<Integer> ids);
}
