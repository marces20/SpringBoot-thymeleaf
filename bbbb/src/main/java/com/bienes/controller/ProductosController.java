package com.bienes.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bienes.model.Producto; 
import com.bienes.service.IProductoService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/productos")
public class ProductosController {
	
	@Autowired
	private IProductoService serviceProducto;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@GetMapping("/index")
	public String mostrarIndex(
								  HttpServletRequest request,
								  Model model,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "25") Integer pageSize,
								  @RequestParam(required = false) String nombre
							  ){
			
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Producto> productos = serviceProducto.findTodo(nombre, pageRequest);
		PageRender<Producto> pageRender = new PageRender<Producto>(request.getRequestURI()+"?"+request.getQueryString(), productos,pageSize,page);
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("titulo", "Listado de Productos");
		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
		return "producto/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Producto producto, Model model) {	
		model.addAttribute("disabled", false);
		return "producto/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idProducto, Model model) {		
		Producto producto = serviceProducto.buscarPorId(idProducto);		
		model.addAttribute("producto", producto);
		model.addAttribute("disabled", false);
		
		return "producto/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idProducto, Model model) {		
		Producto producto = serviceProducto.buscarPorId(idProducto);	 ;
		
		model.addAttribute("producto", producto);
		model.addAttribute("disabled", true);
		return "producto/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Producto producto, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar el producto. "+result.getFieldError().toString());
			model.addAttribute("disabled", false);
			return "producto/form";
		}
		
		try {
			boolean nuevo = false;
			if(producto.getId() == null) {
				producto.setCreate_date(new Date());
				producto.setCreate_user(serviceUsuario.getUserLogged());
				nuevo = true;
			}else {
				producto.setWrite_date(new Date());
				producto.setWrite_user(serviceUsuario.getUserLogged());
			}
			serviceProducto.guardar(producto);
			
			 
			
			attributes.addFlashAttribute("msgsuccess", "Los datos del producto fueron guardados!");
			return "redirect:/productos/show/"+producto.getId();
		} catch (Exception e) {
			model.addAttribute("disabled", false);
			model.addAttribute("msgalert", "Error no se ha podido guardar el producto."+e);
			return "producto/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idProducto, RedirectAttributes attributes) {
		    	
		try {
			serviceProducto.eliminar(idProducto);
			attributes.addFlashAttribute("msg", "El producto fue eliminado!.");

		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar el producto."+e);
			return "redirect:/productos/index";
		}	
		return "redirect:/productos/index";
	}
	
	@GetMapping(value = "/suggest-producto/{nombre}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProducto(@PathVariable String nombre) {
		return serviceProducto.findByNombre(nombre);
	}
	
	@GetMapping("/modalBuscar")
	public String modalBuscar(HttpServletRequest request,
								@ModelAttribute Producto producto,
								Model model,
								@RequestParam(defaultValue = "0") Integer page, 
								@RequestParam(defaultValue = "25") Integer pageSize,
								@RequestParam(required = false) String nombre) {
		
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Producto> productos = serviceProducto.findTodo(nombre, pageRequest);
		PageRender<Producto> pageRender = new PageRender<Producto>(request.getRequestURI()+"?"+request.getQueryString(), productos,pageSize,page);
		
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("productos", productos);
		
		return "producto/modalBusquedaProducto";
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> get(HttpServletRequest result, Model model, RedirectAttributes attributes ) {
		Map<String, Object> r = new HashMap<String, Object>();
		
		Producto o = null;
		boolean error = false;
		String errorStr = "";
		try {
			if(result.getParameter("producto_id") != null) {
				o = serviceProducto.buscarPorId(new Integer(result.getParameter("producto_id")));
				if(o == null) {
					error =true;
					errorStr += "No se puede seleccionar el producto";
				}else {
					r.put("success", true);
					r.put("id", o.getId());
					r.put("nombre", o.getNombre()); 
				}
			}else {
				error =true;
				errorStr += "No se puede obtener producto_id";
			}
			
			if(!error) {
				r.put("success", true);
				return r;
			}else {
				r.put("success", false);
				r.put("message", errorStr);
			}
			
			return r;
		}catch (Exception e) {
			r.put("success", false);
			r.put("message", "Error no se puede obtener el producto. "+e.toString());
			return r;
		}
	}
}
