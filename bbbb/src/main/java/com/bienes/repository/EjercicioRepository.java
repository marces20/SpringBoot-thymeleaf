package com.bienes.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bienes.model.Ejercicio;

public interface EjercicioRepository extends PagingAndSortingRepository<Ejercicio, Integer> {

}
