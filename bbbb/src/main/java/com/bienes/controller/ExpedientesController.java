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
import com.bienes.service.IUsuariosService;
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
		Page<Expediente> expedientes = serviceExpedientes.findExpedienteTodo(nombre, pageRequest);
		PageRender<Expediente> pageRender = new PageRender<Expediente>(request.getRequestURI()+"?"+request.getQueryString(), expedientes,pageSize,page);
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("titulo", "Listado de expedientes");
		model.addAttribute("expedientes", expedientes);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
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
		model.addAttribute("ultimoMovimiento", (ultimoMovimiento != null)?ultimoMovimiento.getOrganigrama().getNombre():"");
		
		return "expediente/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idExpediente, Model model) {		
		Expediente expediente = serviceExpedientes.buscarPorId(idExpediente);	
		ExpedienteMovimiento ultimoMovimiento = serviceExpedienteMovimientos.getLastMovimiento(expediente);
		
		model.addAttribute("ultimoMovimiento", (ultimoMovimiento != null)?ultimoMovimiento.getOrganigrama().getNombre():"");
		model.addAttribute("expediente", expediente);
		model.addAttribute("disabled", true);
		return "expediente/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Expediente expediente, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar el expediente. "+result.getFieldError().toString());
			model.addAttribute("disabled", false);
			return "expediente/form";
		}
		
		try {
			boolean nuevo = false;
			if(expediente.getId() == null) {
				expediente.setCreate_date(new Date());
				expediente.setCreate_user(serviceUsuario.getUserLogged());
				nuevo = true;
			}else {
				expediente.setWrite_date(new Date());
				expediente.setWrite_user(serviceUsuario.getUserLogged());
			}
			serviceExpedientes.guardar(expediente);
			
			if(expediente.getId() != null && nuevo) {
				ExpedienteMovimiento em = new ExpedienteMovimiento();
				
				em.setUsuario(serviceUsuario.getUserLogged());
				em.setOrganigrama(serviceUsuario.getUserLogged().getOrganigrama());
				em.setExpediente(expediente);
				em.setFecha_llegada(new Date());
				em.setCancelado(false);
				
				serviceExpedienteMovimientos.guardar(em);
			}
			
			attributes.addFlashAttribute("msgsuccess", "Los datos del expediente fueron guardados!");
			return "redirect:/expedientes/show/"+expediente.getId();
		} catch (Exception e) {
			model.addAttribute("disabled", false);
			model.addAttribute("msgalert", "Error no se ha podido guardar el expediente."+e);
			return "expediente/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		    	
		try {
			serviceExpedientes.eliminar(idUsuario);
			attributes.addFlashAttribute("msg", "El expediente fue eliminado!.");

		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar el expediente."+e);
			return "redirect:/expedientes/index";
		}	
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
