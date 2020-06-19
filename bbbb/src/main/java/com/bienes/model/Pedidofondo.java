package com.bienes.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "pedido_fondos")
public class Pedidofondo implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pedido_fondos_seq_gen")
	@SequenceGenerator(name = "pedido_fondos_seq_gen", sequenceName = "pedido_fondos_id_seq",allocationSize=1)
	private Integer id;
	
	@NotNull
	private String nombre;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="ejercicio_id", nullable=false)
	private Ejercicio ejercicio;
	
	@ManyToOne
    @JoinColumn(name="estado_id", nullable=false)
	private Estado estado;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date create_date;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date write_date;
	
	@ManyToOne
    @JoinColumn(name="write_user_id")
	private Usuario write_user;
	
	@ManyToOne
    @JoinColumn(name="create_user_id")
	private Usuario create_user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNumero(String nombre) {
		this.nombre = nombre;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getWrite_date() {
		return write_date;
	}

	public void setWrite_date(Date write_date) {
		this.write_date = write_date;
	}

	public Usuario getWrite_user() {
		return write_user;
	}

	public void setWrite_user(Usuario write_user) {
		this.write_user = write_user;
	}

	public Usuario getCreate_user() {
		return create_user;
	}

	public void setCreate_user(Usuario create_user) {
		this.create_user = create_user;
	}
	
	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public String getNombreCompleto() {
		return this.getNombre()+"/"+this.getEjercicio().getNombre();
	}


	private static final long serialVersionUID = 1L;
}
