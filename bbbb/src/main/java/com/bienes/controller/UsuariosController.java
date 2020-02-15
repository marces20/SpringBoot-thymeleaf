package com.bienes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.validation.BindingResult;
import com.bienes.model.Usuario;
import com.bienes.service.IUsuariosService;
import com.bienes.util.PageRender;

import ch.qos.logback.classic.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private IUsuariosService serviceUsuarios;
    
	@GetMapping("/index")
	public String mostrarIndex(
								  HttpServletRequest request,
								   
								  Model model,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "5") Integer pageSize,
								  @RequestParam(required = false) String username,
								  @RequestParam(required = false) String nombre,
								  @RequestParam(required = false) Integer estatus
							  ){
			
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Usuario> usuarios = serviceUsuarios.findUsuarioTodo(username,nombre,estatus, pageRequest);
		PageRender<Usuario> pageRender = new PageRender<Usuario>(request.getRequestURI()+"?"+request.getQueryString(), usuarios);
		
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("page", pageRender);
		
		return "usuario/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Usuario usuario, Model model) {		
		model.addAttribute("disabled", false);
		return "usuario/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idUsuario, Model model) {		
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);		
		model.addAttribute("usuario", usuario);
		model.addAttribute("disabled", false);
		return "usuario/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idUsuario, Model model) {		
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);			
		
		model.addAttribute("usuario", usuario);
		model.addAttribute("disabled", true);
		return "usuario/form";
	}
	
	@PostMapping("/save")
	public String guardar(@ModelAttribute Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		model.addAttribute("disabled", false);
		if (result.hasErrors()){
			System.out.println("Existieron errores");
			return "usuario/form";
		}	
		
		
		serviceUsuarios.guardar(usuario);
		attributes.addFlashAttribute("msg", "Los datos del usuario fueron guardados!");
			
		return "redirect:/usuarios/index";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		    	
		// Eliminamos el usuario
    	serviceUsuarios.eliminar(idUsuario);
			
		attributes.addFlashAttribute("msg", "El usuario fue eliminado!.");
		return "redirect:/usuarios/index";
	}
	
	@GetMapping("/changeStatus/{id}/{tipoAccion}")
	public String changeStatus(@PathVariable("id") int idUsuario,@PathVariable("tipoAccion") int tipoAccion, RedirectAttributes attributes) {		
    	serviceUsuarios.changeStatus(idUsuario,tipoAccion);
		attributes.addFlashAttribute("msg", "El usuario fue activado y ahora tiene acceso al sistema.");		
		return "redirect:/usuarios/index";
	}
}
