package com.bienes.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Usuarios")
public class Usuario implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_seq_gen")
	@SequenceGenerator(name = "users_seq_gen", sequenceName = "usuarios_id_seq",allocationSize=1)
	private Integer id;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	@Email
	private String email;
	
	private Integer estatus;
	
	@ManyToOne
    @JoinColumn(name="organigrama_id")
	private Organigrama organigrama;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getEstatus() {
		return estatus;
	}
	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}	
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="usuario_perfil",
			   joinColumns = @JoinColumn(name="id_usuario"),
			   inverseJoinColumns = @JoinColumn(name="id_perfil")			
			)
	private List<Perfil> perfiles;

	public List<Perfil> getPerfiles() {
		return perfiles;
	}
	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}
	public Organigrama getOrganigrama() {
		return organigrama;
	}
	public void setOrganigrama(Organigrama organigrama) {
		this.organigrama = organigrama;
	}
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
