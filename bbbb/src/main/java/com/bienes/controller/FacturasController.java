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

import com.bienes.model.Expediente;
import com.bienes.model.Factura;
import com.bienes.model.FacturaLinea;
import com.bienes.service.IFacturaLineaService;
import com.bienes.service.IFacturaService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/facturas")
public class FacturasController {
	
	@Autowired
	private IFacturaService serviceFactura;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@Autowired
	private IFacturaLineaService serviceFacturaLinea;   
	
	@GetMapping("/index")
	public String mostrarIndex(
								  HttpServletRequest request,
								  Model model,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "25") Integer pageSize,
								  @RequestParam(required = false) String numero
							  ){
			
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Factura> facturas = serviceFactura.findTodo(numero, pageRequest);
		PageRender<Factura> pageRender = new PageRender<Factura>(request.getRequestURI()+"?"+request.getQueryString(), facturas,pageSize,page);
		
		model.addAttribute("numero", numero);
		model.addAttribute("titulo", "Listado de Facturas");
		model.addAttribute("facturas", facturas);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("disabled", true);
		return "factura/index";
	}
	
	@GetMapping("/create")
	public String crear(@ModelAttribute Factura factura, Model model) {	
		model.addAttribute("disabled", false);
		return "factura/form";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idFactura, Model model) {		
		Factura factura = serviceFactura.buscarPorId(idFactura);		
		model.addAttribute("factura", factura);
		model.addAttribute("disabled", false);
		
		return "factura/form";
	}
	
	@GetMapping("/show/{id}")
	public String show(HttpServletRequest request,
					   @PathVariable("id") int idFactura, 
			           Model model,
					   @RequestParam(defaultValue = "0") Integer page, 
			           @RequestParam(defaultValue = "25") Integer pageSize) {
		
		Factura factura = serviceFactura.buscarPorId(idFactura);		
		model.addAttribute("factura",factura);
		model.addAttribute("disabled", true);
		//model.addAttribute("facturaLineas", serviceFacturaLinea.getByFacturaId(factura));
		
		
		//model.addAttribute("facturaLineas", factura.getFacturaLinea());
		return "factura/form";
	}
	
	@PostMapping("/save")
	public String guardar(@Valid @ModelAttribute Factura factura, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		model.addAttribute("disabled", false);
		if (result.hasErrors()){
			model.addAttribute("msgalert", "Error no se ha podido guardar la factura. "+result.getFieldError().toString());
			return "factura/form";
		}	
		
		try {
			boolean nuevo = false;
			
			if(factura.getExpediente().getId() == null) {
				factura.setExpediente(null);
			}
			
			if(factura.getId() == null) {
				factura.setCreate_date(new Date());
				factura.setCreate_user(serviceUsuario.getUserLogged());
				nuevo = true;
			}else {
				factura.setWrite_date(new Date());
				factura.setWrite_user(serviceUsuario.getUserLogged());
			}
			serviceFactura.guardar(factura);
			
			attributes.addFlashAttribute("msgsuccess", "Los datos de la factura fueron guardados!");
			return "redirect:/facturas/show/"+factura.getId();
		} catch (Exception e) {
			model.addAttribute("msgalert", "Error no se ha podido guardar la factura. e"+ e.getMessage());
			return "factura/form";
		}	
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idFactura, RedirectAttributes attributes) {
		    	
		try {
			serviceFactura.eliminar(idFactura);
			attributes.addFlashAttribute("msg", "La factura fue eliminada!.");

		} catch (Exception e) {
			attributes.addFlashAttribute("msgalert", "Error no se ha podido eliminar la factura."+e);
			return "redirect:/facturas/index";
		}	
		return "redirect:/facturas/index";
	}
}
