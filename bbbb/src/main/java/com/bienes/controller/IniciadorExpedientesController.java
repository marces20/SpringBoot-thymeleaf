package com.bienes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bienes.model.IniciadorExpediente;
import com.bienes.model.Organigrama;
import com.bienes.service.IIniciadorExpedienteService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/iniciador-expedientes")
public class IniciadorExpedientesController {
	
	@Autowired
	private IIniciadorExpedienteService servicioIniciador;
	
	@GetMapping(value = "/suggest-iniciador/{nombre}", produces = { "application/json" })
	public @ResponseBody List<IniciadorExpediente> cargarIniciador(@PathVariable String nombre) {
		
		return servicioIniciador.findByNombre(nombre);
	}
	
	@GetMapping("/modalBuscar")
	public String modalBuscar(HttpServletRequest request,
								@ModelAttribute IniciadorExpediente iniciador,
								Model model,
								@RequestParam(defaultValue = "0") Integer page, 
								@RequestParam(defaultValue = "25") Integer pageSize,
								@RequestParam(required = false) String nombre) {
		
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<IniciadorExpediente> iniciadores = servicioIniciador.findIniciadorExpedienteTodo(nombre, pageRequest);
		PageRender<IniciadorExpediente> pageRender = new PageRender<IniciadorExpediente>(request.getRequestURI()+"?"+request.getQueryString(), iniciadores,pageSize,page);
		
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("iniciadores", iniciadores);
		
		return "iniciadorExpediente/modalBusquedaIniciadorExpediente";
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> get(HttpServletRequest result, Model model, RedirectAttributes attributes ) {
		Map<String, Object> r = new HashMap<String, Object>();
		
		IniciadorExpediente o = null;
		boolean error = false;
		String errorStr = "";
		try {
			if(result.getParameter("iniciador_id") != null) {
				o = servicioIniciador.buscarPorId(new Integer(result.getParameter("iniciador_id")));
				if(o == null) {
					error =true;
					errorStr += "No se puede seleccionar el Iniciador";
				}else {
					r.put("success", true);
					r.put("id", o.getId());
					r.put("nombre", o.getNombre()); 
				}
			}else {
				error =true;
				errorStr += "No se puede obtener iniciador_id";
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
			r.put("message", "Error no se puede obtener el iniciador. "+e.toString());
			return r;
		}
	}
}
