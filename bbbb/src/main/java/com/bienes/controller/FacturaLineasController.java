package com.bienes.controller;

 
import java.util.Date;
import java.util.HashMap; 
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bienes.model.Factura;
import com.bienes.model.FacturaLinea;
import com.bienes.service.IFacturaLineaService;
import com.bienes.service.IFacturaService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.NumberUtils;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/facturalineas")
public class FacturaLineasController {
	
	@Autowired
	private IFacturaLineaService serviceFacturaLinea;   
	
	@Autowired
	private IFacturaService serviceFactura;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@GetMapping("/index/{factura_id}")
	public String mostrarIndex(
									
								  HttpServletRequest request,
								  
								  Model model,
								  @PathVariable("factura_id") Integer idFactura,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "25") Integer pageSize,
								  @RequestParam(required = false) String numero
							  ){
			
		
		Factura factura = serviceFactura.buscarPorId(idFactura);		
		if(factura != null) {
			Pageable pageRequest = PageRequest.of(page, pageSize);
			Page<FacturaLinea> facturaLineas = serviceFacturaLinea.getPageByFacturaId(pageRequest, factura);
			PageRender<FacturaLinea> pageRender = new PageRender<FacturaLinea>(request.getRequestURI()+"?"+request.getQueryString(), facturaLineas,pageSize,page);
			
			model.addAttribute("page", pageRender);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("facturaLineas", facturaLineas);
			model.addAttribute("utils", NumberUtils.class);
		}	
		return "factura/facturaLinea/index";
	}
	
	@GetMapping("/crear/{factura_id}")
	public String crear(@ModelAttribute FacturaLinea facturaLinea, Model model, @PathVariable("factura_id") Integer idFactura) {	
		model.addAttribute("disabled", false);
		model.addAttribute("idFactura", idFactura);
		return "factura/facturaLinea/form";
	}
	
	@GetMapping("/editar/{linea_factura_id}")
	public String editar(@ModelAttribute FacturaLinea facturaLinea, Model model, @PathVariable("linea_factura_id") Integer idLineaFactura) {	
		FacturaLinea fl = serviceFacturaLinea.buscarPorId(idLineaFactura);
		model.addAttribute("disabled", false);
		model.addAttribute("idFactura", fl.getFactura().getId());
		model.addAttribute("facturaLinea", fl);
		
		return "factura/facturaLinea/form";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ModelAndView guardar(@Valid @ModelAttribute FacturaLinea facturaLinea, BindingResult result, Model model, RedirectAttributes attributes ) {	
		
		ModelAndView modelAndView = new ModelAndView("factura/facturaLinea/form");
		
		if (result.hasErrors()){
			
			modelAndView.addObject("disabled", false);
			modelAndView.addObject("idFacturaLinea", facturaLinea.getId());
			modelAndView.addObject("idFactura", facturaLinea.getFactura().getId());
			modelAndView.addObject("msgalert", "Error no se ha podido guardar la linea factura. "+result.getFieldError().toString());
			
			return modelAndView;
		}	
		
		try {
			boolean nuevo = false;
			if(facturaLinea.getId() == null) {
				facturaLinea.setCreate_date(new Date());
				facturaLinea.setCreate_user(serviceUsuario.getUserLogged());
				nuevo = true;
			}else {
				facturaLinea.setWrite_date(new Date());
				facturaLinea.setWrite_user(serviceUsuario.getUserLogged());
			}
			serviceFacturaLinea.guardar(facturaLinea);
			modelAndView.addObject("idFactura", facturaLinea.getId());
			modelAndView.addObject("msgsuccess", "Los datos de la linea fueron guardados!"); 
			
			return null;		
		} catch (Exception e) {
			modelAndView.addObject("idFactura", facturaLinea.getId());
			modelAndView.addObject("msgalert", "Error no se ha podido guardar la linea factura. "+e.getMessage());
			return modelAndView;
		}	
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> eliminar(@PathVariable("id") int idFacturaLinea, RedirectAttributes attributes) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		boolean error = false;
		String errorStr = "";
		try {
			serviceFacturaLinea.eliminar(idFacturaLinea);
			attributes.addFlashAttribute("msg", "La factura fue eliminada!.");
			r.put("success", true);
		} catch (Exception e) {
			errorStr += "Error no se ha podido eliminar la linea factura."+e;
			r.put("error", errorStr);
		}	
		return r;
	}
}
