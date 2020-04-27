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

import com.bienes.model.Pedidofondo;
import com.bienes.service.IEjercicioService;
import com.bienes.service.IPedidofondoService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/pedidofondos")
public class PedidofondosController {
	
	@Autowired
	private IPedidofondoService servicePedidofondo;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@Autowired
	private IEjercicioService serviceEjercicio;
	
	@GetMapping("/index")
	public String mostrarIndex(
								  HttpServletRequest request,
								  Model model,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "25") Integer pageSize,
								  @RequestParam(required = false) String numero
							  ){
			
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Pedidofondo> pedidofondos = servicePedidofondo.findTodo(numero, pageRequest);
		PageRender<Pedidofondo> pageRender = new PageRender<Pedidofondo>(request.getRequestURI()+"?"+request.getQueryString(), pedidofondos,pageSize,page);
		
		model.addAttribute("numero", numero);
		model.addAttribute("titulo", "Listado de Facturas");
		model.addAttribute("pedidofondos", pedidofondos);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
		return "pedidofondo/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Pedidofondo pedidofondo, Model model) {	
		model.addAttribute("disabled", false);
		return "pedidofondo/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idPedidofondo, Model model) {		
		Pedidofondo pedidofondo = servicePedidofondo.buscarPorId(idPedidofondo);		
		model.addAttribute("pedidofondo",pedidofondo);
		model.addAttribute("disabled", false);
		
		return "pedidofondo/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable("id") int idPedidofondo, Model model) {		
		Pedidofondo pedidofondo = servicePedidofondo.buscarPorId(idPedidofondo);	 ;
		
		model.addAttribute("pedidofondo",pedidofondo);
		model.addAttribute("disabled", true);
		return "pedidofondo/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Pedidofondo pedidofondo, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar el pedido. "+result.getFieldError().toString());
			model.addAttribute("disabled", false);
			return "pedidofondo/form";
		}	
		
		try {
			boolean nuevo = false;
			if(pedidofondo.getId() == null) {
				pedidofondo.setCreate_date(new Date());
				pedidofondo.setCreate_user(serviceUsuario.getUserLogged());
				nuevo = true;
			}else {
				pedidofondo.setWrite_date(new Date());
				pedidofondo.setWrite_user(serviceUsuario.getUserLogged());
			}
			servicePedidofondo.guardar(pedidofondo);
			
			attributes.addFlashAttribute("msgsuccess", "Los datos del pedido fueron guardados!");
			return "redirect:/pedidofondos/show/"+pedidofondo.getId();
		} catch (Exception e) {
			model.addAttribute("disabled", false);
			model.addAttribute("msgalert", "Error no se ha podido guardar el pedido."+e);
			return "pedidofondo/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idPedidofondo, RedirectAttributes attributes) {
		try {
			servicePedidofondo.eliminar(idPedidofondo);
			attributes.addFlashAttribute("msg", "El pedido fue eliminado!.");
		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar el pedido."+e);
			return "redirect:/pedidofondos/index";
		}	
		return "redirect:/pedidofondos/index";
	}
	
	@ModelAttribute
	public void listaEjercicios(Model model) {
		model.addAttribute("ejercicios", serviceEjercicio.findAll());
	}
}
