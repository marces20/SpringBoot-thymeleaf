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

import com.bienes.model.Estado; 
import com.bienes.model.Pago;
import com.bienes.service.IEstadoService;
import com.bienes.service.IPagoService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.NumberUtils;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/pagos")
public class PagosController {
	
	@Autowired
	private IPagoService servicePago;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@Autowired
	private IEstadoService serviceEstado;
	
	@GetMapping("/index")
	public String mostrarIndex(
								  HttpServletRequest request,
								  Model model,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "25") Integer pageSize,
								  @RequestParam(required = false) String id
							  ){
			
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Pago> pagos = servicePago.findTodo(id, pageRequest);
		PageRender<Pago> pageRender = new PageRender<Pago>(request.getRequestURI()+"?"+request.getQueryString(), pagos,pageSize,page);
		
		model.addAttribute("numero", id);
		model.addAttribute("titulo", "Listado de Pagos");
		model.addAttribute("pagos", pagos);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
		model.addAttribute("utils", NumberUtils.class);
		return "pago/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Pago pago, Model model) {	
		model.addAttribute("disabled", false);
		return "pago/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idPago, Model model) {		
		Pago pago = servicePago.buscarPorId(idPago);		
		model.addAttribute("pago", pago);
		model.addAttribute("disabled", false);
		
		return "pago/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(HttpServletRequest request,
					   @PathVariable("id") int idPago, 
			           Model model,
					   @RequestParam(defaultValue = "0") Integer page, 
			           @RequestParam(defaultValue = "25") Integer pageSize) {
		
		Pago pago = servicePago.buscarPorId(idPago);		
		model.addAttribute("pago",pago);
		model.addAttribute("disabled", true); 
		
		 
		return "pago/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Pago pago, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		model.addAttribute("disabled", false);
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar el pago. "+result.getFieldError().toString());
			return "pago/form";
		}	
		
		try {
			boolean nuevo = false;
			
			if(pago.getId() == null) {
				pago.setCreate_date(new Date());
				pago.setCreate_user(serviceUsuario.getUserLogged());
				pago.setEstado(serviceEstado.buscarPorId(Estado.PAGO_BORRADOR));
				nuevo = true;
			}else {
				pago.setWrite_date(new Date());
				pago.setWrite_user(serviceUsuario.getUserLogged());
			}
			servicePago.guardar(pago);
			
			attributes.addFlashAttribute("msgsuccess", "Los datos del pago fueron guardados!");
			return "redirect:/pagos/show/"+pago.getId();
		} catch (Exception e) {
			model.addAttribute("msgalert", "Error no se ha podido guardar del pago. e"+ e.getMessage());
			return "pago/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idPago, RedirectAttributes attributes) {
		    	
		try {
			servicePago.eliminar(idPago);
			attributes.addFlashAttribute("msg", "El pago fue eliminada!.");

		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar el pago."+e);
			return "redirect:/pagos/index";
		}	
		return "redirect:/pagos/index";
	}
}
