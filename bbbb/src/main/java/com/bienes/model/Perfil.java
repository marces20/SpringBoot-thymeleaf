package com.bienes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Perfiles")
public class Perfil {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "perfiles_seq_gen")
	@SequenceGenerator(name = "perfiles_seq_gen", sequenceName = "perfiles_id_seq",allocationSize=1)
	private Integer id;
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

	@Override
	public String toString() {
		return "Perfil [id=" + id + ", perfil=" + nombre + "]";
	}
}
