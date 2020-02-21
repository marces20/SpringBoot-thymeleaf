package com.bienes.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "organigramas")
public class Organigrama implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "organigramas_seq_gen")
	@SequenceGenerator(name = "organigramas_seq_gen", sequenceName = "organigramas_id_seq",allocationSize=1)
	private Integer id;
	private String nombre;
	
	private String codigo_expediente;
	
	private Integer padre_id;

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

	public String getCodigo_expediente() {
		return codigo_expediente;
	}

	public void setCodigo_expediente(String codigo_expediente) {
		this.codigo_expediente = codigo_expediente;
	}

	public Integer getPadre_id() {
		return padre_id;
	}

	public void setPadre_id(Integer padre_id) {
		this.padre_id = padre_id;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
