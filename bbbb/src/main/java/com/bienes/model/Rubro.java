package com.bienes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rubros")
public class Rubro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "rubros_seq_gen")
	@SequenceGenerator(name = "rubros_seq_gen", sequenceName = "rubros_id_seq",allocationSize=1)
	private Integer id;
	
	@NotNull
	private String nombre;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	private static final long serialVersionUID = 1L;

}
