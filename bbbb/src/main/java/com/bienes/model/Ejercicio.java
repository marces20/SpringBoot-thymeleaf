package com.bienes.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "Ejercicios")
public class Ejercicio implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ejercicios_seq_gen")
	@SequenceGenerator(name = "ejercicios_seq_gen", sequenceName = "ejercicios_id_seq",allocationSize=1)
	private Integer id;
	private String nombre;
	
	private Date fecha_inicio;
	private Date fecha_fin;
	
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
	public Date getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	public Date getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
