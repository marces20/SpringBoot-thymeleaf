package com.bienes.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Pagos")
public class Pago implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pagos_seq_gen")
	@SequenceGenerator(name = "pagos_seq_gen", sequenceName = "pagos_id_seq",allocationSize=1)
	private Integer id;
	
	@NotNull
	public BigDecimal monto;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date fecha;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="pedidofondo_id", nullable=false)
	private Pedidofondo pedidofondo;	
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="factura_id", nullable=false)
	private Factura factura;	
	
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

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Pedidofondo getPedidofondo() {
		return pedidofondo;
	}

	public void setPedidofondo(Pedidofondo pedidofondo) {
		this.pedidofondo = pedidofondo;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
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
	
	private static final long serialVersionUID = 1L;
}
