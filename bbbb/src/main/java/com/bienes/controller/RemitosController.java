package com.bienes.controller;

import java.util.Date;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bienes.model.Remito;
import com.bienes.service.IRemitoService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/remitos")
public class RemitosController {
	

	@Autowired
	private IRemitoService serviceRemito;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@GetMapping("/index")
	public String mostrarIndex(
								  HttpServletRequest request,
								  Model model,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "25") Integer pageSize,
								  @RequestParam(required = false) String numero
							  ){
			
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Remito> remitos = serviceRemito.findTodo(numero, pageRequest);
		PageRender<Remito> pageRender = new PageRender<Remito>(request.getRequestURI()+"?"+request.getQueryString(), remitos,pageSize,page);
		
		model.addAttribute("numero", numero);
		model.addAttribute("titulo", "Listado de Remitors");
		model.addAttribute("remitos", remitos);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
		return "remito/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Remito remito, Model model) {	
		model.addAttribute("disabled", false);
		return "remito/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idRemito, Model model) {		
		Remito remito = serviceRemito.buscarPorId(idRemito);		
		model.addAttribute("remito",remito);
		model.addAttribute("disabled", false);
		
		return "remito/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idRemito, Model model) {		
		Remito remito = serviceRemito.buscarPorId(idRemito);	 ;
		
		model.addAttribute("remito",remito);
		model.addAttribute("disabled", true);
		return "remito/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Remito remito, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar el remito. "+result.getFieldError().toString());
			model.addAttribute("disabled", false);
			return "remito/form";
		}
		
		try {
			boolean nuevo = false;
			if(remito.getId() == null) {
				remito.setCreate_date(new Date());
				remito.setCreate_user(serviceUsuario.getUserLogged());
				nuevo = true;
			}else {
				remito.setWrite_date(new Date());
				remito.setWrite_user(serviceUsuario.getUserLogged());
			}
			serviceRemito.guardar(remito);
			
			 
			
			attributes.addFlashAttribute("msgsuccess", "Los datos del remito fueron guardados!");
			return "redirect:/remitos/show/"+remito.getId();
		} catch (Exception e) {
			model.addAttribute("disabled", false);
			model.addAttribute("msgalert", "Error no se ha podido guardar el remito."+e);
			return "remito/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idRemito, RedirectAttributes attributes) {
		
		try {
			serviceRemito.eliminar(idRemito);
			attributes.addFlashAttribute("msg", "El remito fue eliminado!.");
		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar el pedido."+e);
			return "redirect:/remitos/index";
		}	
		return "redirect:/remitos/index";
	}
	
}
