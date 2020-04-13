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

import com.bienes.model.Organigrama;
import com.bienes.service.IOrganigramaService;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/organigramas")
public class OrganigramasController {
	
	@Autowired
	private IOrganigramaService serviceOrganigrama;
	
	@GetMapping(value = "/suggest-organigrama/{nombre}", produces = { "application/json" })
	public @ResponseBody List<Organigrama> cargarOrganigrama(@PathVariable String nombre) {
		return serviceOrganigrama.findByNombre(nombre);
	}
	
	@GetMapping("/modalBuscar")
	public String modalBuscar(HttpServletRequest request,
								@ModelAttribute Organigrama organigrama,
								Model model,
								@RequestParam(defaultValue = "0") Integer page, 
								@RequestParam(defaultValue = "25") Integer pageSize,
								@RequestParam(required = false) String nombre) {
		
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<Organigrama> organigramas = serviceOrganigrama.findOrganigramaTodo(nombre, pageRequest);
		PageRender<Organigrama> pageRender = new PageRender<Organigrama>(request.getRequestURI()+"?"+request.getQueryString(), organigramas,pageSize,page);
		
		
		model.addAttribute("nombre", nombre);
		model.addAttribute("page", pageRender);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("organigramas", organigramas);
		
		return "organigrama/modalBusquedaOrganigrama";
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> get(HttpServletRequest result, Model model, RedirectAttributes attributes ) {
		Map<String, Object> r = new HashMap<String, Object>();
		
		Organigrama o = null;
		boolean error = false;
		String errorStr = "";
		try {
			if(result.getParameter("organigrama_id") != null) {
				o = serviceOrganigrama.buscarPorId(new Integer(result.getParameter("organigrama_id")));
				if(o == null) {
					error =true;
					errorStr += "No se puede seleccionar el Organigrama";
				}else {
					r.put("success", true);
					r.put("id", o.getId());
					r.put("nombre", o.getNombre()); 
				}
			}else {
				error =true;
				errorStr += "No se puede obtener organigrama_id";
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
			r.put("message", "Error no se puede obtener el organigrama. "+e.toString());
			return r;
		}
	}
	
	/*
	 public static Result get(int id){
		Expediente expediente = Expediente.find.select("id, nombre, descripcion").where().eq("activo", true).eq("id", id).findUnique();
		
		ObjectNode obj = Json.newObject();
	    ArrayNode nodo = obj.arrayNode();
		ObjectNode restJs = Json.newObject();
		
		if(expediente == null) {
			restJs.put("success", false);
			restJs.put("message", "No se encuentra el expediente");
		} else {
			restJs.put("success", true);
			restJs.put("id", expediente.id);
			restJs.put("nombre", expediente.getExpedienteEjercicio());
			restJs.put("descripcion", expediente.descripcion);
		}
		nodo.add(restJs);
		return ok(restJs);
	}
	 */
	
}
