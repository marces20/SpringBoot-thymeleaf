package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
 
import com.bienes.model.Remito;

public interface RemitoRepository extends PagingAndSortingRepository<Remito, Integer> {
	
	@Query("select p from Remito p where p.id in(?1)")
	public List<Remito> buscarPorIds(List<Integer> ids);
}
