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

import com.bienes.model.Rubro;
import com.bienes.service.IRubroService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/rubros")
public class RubrosController {
	
	@Autowired
	private IRubroService serviceRubro;
	
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
		Page<Rubro> rubros = serviceRubro.findTodo(nombre, pageRequest);
		PageRender<Rubro> pageRender = new PageRender<Rubro>(request.getRequestURI()+"?"+request.getQueryString(), rubros,pageSize,page);
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("titulo", "Listado de Rubros");
		model.addAttribute("rubros", rubros);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
		return "rubro/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Rubro rubro, Model model) {	
		model.addAttribute("disabled", false);
		return "rubro/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idRubro, Model model) {		
		Rubro rubro = serviceRubro.buscarPorId(idRubro);		
		model.addAttribute("rubro", rubro);
		model.addAttribute("disabled", false);
		
		return "rubro/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idRubro, Model model) {		
		Rubro rubro = serviceRubro.buscarPorId(idRubro);	 ;
		
		model.addAttribute("rubro", rubro);
		model.addAttribute("disabled", true);
		return "rubro/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Rubro rubro, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar el rubro. "+result.getFieldError().toString());
			model.addAttribute("disabled", false);
			return "rubro/form";
		}
		
		try {
			boolean nuevo = false;
			 
			serviceRubro.guardar(rubro);
			
			 
			
			attributes.addFlashAttribute("msgsuccess", "Los datos del rubro fueron guardados!");
			return "redirect:/rubros/show/"+rubro.getId();
		} catch (Exception e) {
			model.addAttribute("disabled", false);
			model.addAttribute("msgalert", "Error no se ha podido guardar el rubro."+e);
			return "rubro/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idRubro, RedirectAttributes attributes) {
		    	
		try {
			serviceRubro.eliminar(idRubro);
			attributes.addFlashAttribute("msg", "El rubro fue eliminado!.");

		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar el rubro."+e);
			return "redirect:/rubros/index";
		}	
		return "redirect:/rubros/index";
	}
	
	@GetMapping(value = "/suggest-rubro/{nombre}", produces = { "application/json" })
	public @ResponseBody List<Rubro> cargarRubro(@PathVariable String nombre) {
		return serviceRubro.findByNombre(nombre);
	}
	
	@GetMapping("/modalBuscar")
	public String modalBuscar(HttpServletRequest request,
								@ModelAttribute Rubro rubro,
								Model model,
								@RequestParam(defaultValue = "0") Integer page, 
								@RequestParam(defaultValue = "25") Integer pageSize,
								@RequestParam(required = false) String nombre) {
		
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Rubro> rubros = serviceRubro.findTodo(nombre, pageRequest);
		PageRender<Rubro> pageRender = new PageRender<Rubro>(request.getRequestURI()+"?"+request.getQueryString(), rubros,pageSize,page);
		
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("rubros", rubros);
		
		return "rubro/modalBusquedaRubro";
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> get(HttpServletRequest result, Model model, RedirectAttributes attributes ) {
		Map<String, Object> r = new HashMap<String, Object>();
		
		Rubro o = null;
		boolean error = false;
		String errorStr = "";
		try {
			if(result.getParameter("rubro_id") != null) {
				o = serviceRubro.buscarPorId(new Integer(result.getParameter("rubro_id")));
				if(o == null) {
					error =true;
					errorStr += "No se puede seleccionar el rubro";
				}else {
					r.put("success", true);
					r.put("id", o.getId());
					r.put("nombre", o.getNombre()); 
				}
			}else {
				error =true;
				errorStr += "No se puede obtener rubro_id";
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
			r.put("message", "Error no se puede obtener el rubro. "+e.toString());
			return r;
		}
	}
}
