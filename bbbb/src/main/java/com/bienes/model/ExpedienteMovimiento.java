package com.bienes.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "expedientes_movimientos")
public class ExpedienteMovimiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "expedientes_movimiento_seq_gen")
	@SequenceGenerator(name = "expedientes_movimiento_seq_gen", sequenceName = "expedientes_movimientos_id_seq",allocationSize=1)
	private Integer id;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="organigrama_id", nullable=false)
	private Organigrama organigrama;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="usuario_id", nullable=false)
	private Usuario usuario;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="expediente_id", nullable=false)
	private Expediente expediente;
	
	@NotNull
	private  Date fecha_llegada;
	
	private Date fecha_salida;
	
	private String descripcion;
	
	private boolean cancelado;
	
	private Integer codigo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Organigrama getOrganigrama() {
		return organigrama;
	}

	public void setOrganigrama(Organigrama organigrama) {
		this.organigrama = organigrama;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Date getFecha_llegada() {
		return fecha_llegada;
	}

	public void setFecha_llegada(Date fecha_llegada) {
		this.fecha_llegada = fecha_llegada;
	}

	public Date getFecha_salida() {
		return fecha_salida;
	}

	public void setFecha_salida(Date fecha_salida) {
		this.fecha_salida = fecha_salida;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isCancelado() {
		return cancelado;
	}

	public void setCancelado(boolean cancelado) {
		this.cancelado = cancelado;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return "ExpedienteMovimiento [id=" + id + ", organigrama=" + organigrama + ", usuario=" + usuario
				+ ", expediente=" + expediente + ", fecha_llegada=" + fecha_llegada + ", fecha_salida=" + fecha_salida
				+ ", descripcion=" + descripcion + ", cancelado=" + cancelado + ", codigo=" + codigo + "]";
	} 
	
	
}
