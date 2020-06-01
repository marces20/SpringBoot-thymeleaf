package com.bienes.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "estados")
public class Estado implements Serializable{
 
	public static final Integer PEDIDOFONDO_BORRADOR = 1;
	public static final Integer PEDIDOFONDO_APROBADO = 2;
	public static final Integer PAGO_BORRADOR = 3;
	public static final Integer PAGO_PAGADO = 4; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "estados_seq_gen")
	@SequenceGenerator(name = "estados_seq_gen", sequenceName = "estados_id_seq",allocationSize=1)
	private Integer id;
	
	@NotNull
	private String nombre;
	
	@NotNull
	private String modelo;
	
	@NotNull
	private String css;

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

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}



	private static final long serialVersionUID = 1L;
	
	
}
