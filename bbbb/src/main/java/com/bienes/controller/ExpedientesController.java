package com.bienes.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import com.bienes.model.Expediente;
import com.bienes.model.ExpedienteMovimiento;
import com.bienes.service.IEjercicioService;
import com.bienes.service.IExpedienteMovimientoService;
import com.bienes.service.IExpedienteService;
import com.bienes.util.PageRender; 

@Controller
@RequestMapping("/expedientes")
public class ExpedientesController {
	
	@Autowired
	private IExpedienteService serviceExpedientes;
	
	@Autowired
	private IExpedienteMovimientoService serviceExpedienteMovimientos;
	
	@Autowired
	private IEjercicioService serviceEjercicio;
	
	@GetMapping("/index")
	public String mostrarIndex(
								  HttpServletRequest request,
								   
								  Model model,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "5") Integer pageSize,
								  @RequestParam(required = false) String nombre
							  ){
			
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Expediente> expedientes = serviceExpedientes.findExpedienteTodo(nombre, pageRequest);
		PageRender<Expediente> pageRender = new PageRender<Expediente>(request.getRequestURI()+"?"+request.getQueryString(), expedientes);
		
		model.addAttribute("titulo", "Listado de expedientes");
		model.addAttribute("expedientes", expedientes);
		model.addAttribute("page", pageRender);
		
		return "expediente/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Expediente expediente, Model model) {	
		model.addAttribute("disabled", false);
		
		
		return "expediente/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idExpediente, Model model) {		
		Expediente expediente = serviceExpedientes.buscarPorId(idExpediente);		
		model.addAttribute("expediente", expediente);
		model.addAttribute("disabled", false);
		
		ExpedienteMovimiento ultimoMovimiento = serviceExpedienteMovimientos.getLastMovimiento(expediente);
		model.addAttribute("ultimoMovimiento", ultimoMovimiento);
		
		return "expediente/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idExpediente, Model model) {		
		Expediente expediente = serviceExpedientes.buscarPorId(idExpediente);	
		ExpedienteMovimiento ultimoMovimiento = serviceExpedienteMovimientos.getLastMovimiento(expediente);
		
		model.addAttribute("ultimoMovimiento", ultimoMovimiento);
		model.addAttribute("expediente", expediente);
		model.addAttribute("disabled", true);
		return "expediente/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Expediente expediente, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		
		if (result.hasErrors()){
			model.addAttribute("disabled", false);
			return "expediente/form";
		}	
		
		try {
			if(expediente.getId() == null) {
				expediente.setCreate_date(new Date());
				//expediente.setCreate_user(1);
			}else {
				expediente.setWrite_date(new Date());
				//expediente.setWrite_user(write_user);
			}
			serviceExpedientes.guardar(expediente);
			attributes.addFlashAttribute("msgsuccess", "Los datos del expediente fueron guardados!");
			return "redirect:/expedientes/index";
		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido guardar el expediente."+e);
			return "redirect:/expedientes/index";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		    	
		// Eliminamos el expediente
		serviceExpedientes.eliminar(idUsuario);
		attributes.addFlashAttribute("msg", "El expediente fue eliminado!.");
		return "redirect:/expedientes/index";
	}
	
	@ModelAttribute
	public void listaEjercicios(Model model) {
		model.addAttribute("ejercicios", serviceEjercicio.findAll());
	}
	
	@ModelAttribute
	public void userAuth(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userAuth", auth.getName());
	}
	
	@GetMapping("/modalPasarOtroServicio")
	public String modalPasarOtroServicio(@ModelAttribute ExpedienteMovimiento expedienteMovimiento, Model model) {
		return "expediente/modal/pasarOtroServicio";
	}
}
