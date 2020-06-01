package com.bienes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Pago;

public interface PagoRepository extends PagingAndSortingRepository<Pago, Integer> {
	
	@Query("select p from Pago p where p.id in(?1)")
	public List<Pago> buscarPorIds(List<Integer> ids);
}
