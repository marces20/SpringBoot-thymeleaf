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

import com.bienes.model.Expediente;
import com.bienes.model.Proveedor;
import com.bienes.service.IProveedorService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/proveedores")
public class ProveedoresController {
	

	@Autowired
	private IProveedorService serviceProveedor;
	
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
		Page<Proveedor> proveedores = serviceProveedor.findProveedorTodo(nombre, pageRequest);
		PageRender<Proveedor> pageRender = new PageRender<Proveedor>(request.getRequestURI()+"?"+request.getQueryString(), proveedores,pageSize,page);
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("titulo", "Listado de Proveedores");
		model.addAttribute("proveedores", proveedores);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
		return "proveedor/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Proveedor proveedor, Model model) {	
		model.addAttribute("disabled", false);
		return "proveedor/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idProveedor, Model model) {		
		Proveedor proveedor = serviceProveedor.buscarPorId(idProveedor);		
		model.addAttribute("proveedor", proveedor);
		model.addAttribute("disabled", false);
		
		return "proveedor/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idProveedor, Model model) {		
		Proveedor proveedor = serviceProveedor.buscarPorId(idProveedor);	 ;
		
		model.addAttribute("proveedor", proveedor);
		model.addAttribute("disabled", true);
		return "proveedor/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Proveedor proveedor, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar el proveedor. "+result.getFieldError().toString());
			model.addAttribute("disabled", false);
			return "proveedor/form";
		}
		
		try {
			boolean nuevo = false;
			if(proveedor.getId() == null) {
				proveedor.setCreate_date(new Date());
				proveedor.setCreate_user(serviceUsuario.getUserLogged());
				nuevo = true;
			}else {
				proveedor.setWrite_date(new Date());
				proveedor.setWrite_user(serviceUsuario.getUserLogged());
			}
			serviceProveedor.guardar(proveedor);
			
			 
			
			attributes.addFlashAttribute("msgsuccess", "Los datos del proveedor fueron guardados!");
			return "redirect:/proveedores/show/"+proveedor.getId();
		} catch (Exception e) {
			model.addAttribute("disabled", false);
			model.addAttribute("msgalert", "Error no se ha podido guardar el proveedor."+e);
			return "proveedor/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idProveedor, RedirectAttributes attributes) {
		    	
		try {
			serviceProveedor.eliminar(idProveedor);
			attributes.addFlashAttribute("msg", "El proveedor fue eliminado!.");

		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar el proveedor."+e);
			return "redirect:/proveedores/index";
		}	
		return "redirect:/proveedores/index";
	}
	
	@GetMapping(value = "/suggest-proveedor/{nombre}", produces = { "application/json" })
	public @ResponseBody List<Proveedor> cargarProveedor(@PathVariable String nombre) {
		return serviceProveedor.findByNombre(nombre);
	}
	
	@GetMapping("/modalBuscar")
	public String modalBuscar(HttpServletRequest request,
								@ModelAttribute Proveedor proveedor,
								Model model,
								@RequestParam(defaultValue = "0") Integer page, 
								@RequestParam(defaultValue = "25") Integer pageSize,
								@RequestParam(required = false) String nombre) {
		
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Proveedor> proveedores = serviceProveedor.findProveedorTodo(nombre, pageRequest);
		PageRender<Proveedor> pageRender = new PageRender<Proveedor>(request.getRequestURI()+"?"+request.getQueryString(), proveedores,pageSize,page);
		
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("proveedores", proveedores);
		
		return "proveedor/modalBusquedaProveedor";
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> get(HttpServletRequest result, Model model, RedirectAttributes attributes ) {
		Map<String, Object> r = new HashMap<String, Object>();
		
		Proveedor o = null;
		boolean error = false;
		String errorStr = "";
		try {
			if(result.getParameter("proveedor_id") != null) {
				o = serviceProveedor.buscarPorId(new Integer(result.getParameter("proveedor_id")));
				if(o == null) {
					error =true;
					errorStr += "No se puede seleccionar el Proveedor";
				}else {
					r.put("success", true);
					r.put("id", o.getId());
					r.put("nombre", o.getNombre()); 
				}
			}else {
				error =true;
				errorStr += "No se puede obtener proveedor_id";
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
			r.put("message", "Error no se puede obtener el proveedor. "+e.toString());
			return r;
		}
	}
}
