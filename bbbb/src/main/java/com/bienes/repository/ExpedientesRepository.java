package com.bienes.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.bienes.model.Expediente;

public interface ExpedientesRepository extends PagingAndSortingRepository<Expediente, Integer> {

}
