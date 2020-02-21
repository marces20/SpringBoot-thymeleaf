package com.bienes.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "iniciador_expedientes")
public class IniciadorExpediente implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "iniciador_expedientes_seq_gen")
	@SequenceGenerator(name = "iniciador_expedientes_seq_gen", sequenceName = "iniciador_expedientes_id_seq",allocationSize=1)
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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
