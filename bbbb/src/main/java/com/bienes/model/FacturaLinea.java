package com.bienes.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Table(name = "Factura_Lineas")
public class FacturaLinea {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "factura_lineas_seq_gen")
	@SequenceGenerator(name = "factura_lineas_seq_gen", sequenceName = "factura_lineas_id_seq",allocationSize=1)
	private Integer id;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="factura_id", nullable=false)
	private Factura factura;	
	
	@NotNull
	@ManyToOne
    @JoinColumn(name="producto_id", nullable=false)
	private Producto producto;	
	
	@NotNull
	public BigDecimal precio;
	
	@NotNull
	public BigDecimal cantidad;
		
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
	
	public BigDecimal getTotal() {
		return getPrecio().multiply(getCantidad()).setScale(2, RoundingMode.HALF_UP);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
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
