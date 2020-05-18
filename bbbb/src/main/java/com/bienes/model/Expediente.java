package com.bienes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Expedientes")
public class Expediente implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "expedientes_seq_gen")
	@SequenceGenerator(name = "expedientes_seq_gen", sequenceName = "expedientes_id_seq",allocationSize=1)
	private Integer id;
	
	@NotEmpty
	private String nombre;
	
	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date fecha;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="ejercicio_id", nullable=false)
	private Ejercicio ejercicio;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="iniciador_id", nullable=false)
	private IniciadorExpediente iniciador;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="organigrama_id", nullable=false)
	private Organigrama organigrama;
	 
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy = "expediente", fetch = FetchType.LAZY)
	@OrderBy("id DESC")
	@JsonIgnore
    private List<ExpedienteMovimiento> expedienteMovimiento;
	
	@ManyToOne
    @JoinColumn(name="create_user_id")
	private Usuario create_user;
	
	@ManyToOne
    @JoinColumn(name="write_user_id")
	private Usuario write_user;
	private Date create_date;
	private Date write_date;
	
	
	public Expediente() {
		expedienteMovimiento = new ArrayList<ExpedienteMovimiento>();
	}
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	public IniciadorExpediente getIniciador() {
		return iniciador;
	}
	public void setIniciador(IniciadorExpediente iniciador) {
		this.iniciador = iniciador;
	}
	public Organigrama getOrganigrama() {
		return organigrama;
	}
	public void setOrganigrama(Organigrama organigrama) {
		this.organigrama = organigrama;
	}
	public Usuario getCreate_user() {
		return create_user;
	}
	public void setCreate_user(Usuario create_user) {
		this.create_user = create_user;
	}
	public Usuario getWrite_user() {
		return write_user;
	}
	public void setWrite_user(Usuario write_user) {
		this.write_user = write_user;
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
	public List<ExpedienteMovimiento> getExpedienteMovimiento() {
		return expedienteMovimiento;
	}
	public void setExpedienteMovimiento(List<ExpedienteMovimiento> expedienteMovimiento) {
		this.expedienteMovimiento = expedienteMovimiento;
	}
	
	public String getNombreCompleto() {
		return this.getNombre()+"/"+this.getEjercicio().getNombre();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 
}
