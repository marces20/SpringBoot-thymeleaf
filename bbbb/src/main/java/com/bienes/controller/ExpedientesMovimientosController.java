package com.bienes.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import com.bienes.model.Expediente;
import com.bienes.model.ExpedienteMovimiento;
import com.bienes.model.Organigrama;
import com.bienes.model.Usuario;
import com.bienes.service.IExpedienteMovimientoService;
import com.bienes.service.IExpedienteService;
import com.bienes.service.IOrganigramaService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.ArrayUtils;
import com.bienes.view.pdf.ExpedienteMovimientoPdfView;

@Controller
@RequestMapping("/expedientesmovimientos")
public class ExpedientesMovimientosController {
	
	@Autowired
	private IExpedienteMovimientoService serviceExpeMovimiento;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@Autowired
	private IExpedienteService serviceExpediente;
	
	@Autowired
	private IOrganigramaService serviceOrganigrama;
	
	/*@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Integer id, Model model, RedirectAttributes flash) {
		
		Expediente e = serviceExpediente.buscarPorId(id);
		ExpedienteMovimiento um = serviceExpeMovimiento.getLastMovimiento(e);

		model.addAttribute("expedienteMovimiento", um);
		 
		return "expediente/expedienteMovimiento";
	}*/
	
	@GetMapping("/ver/{id}")
    public ModelAndView report(@PathVariable(value = "id") Integer id) {
        
        Map<String, Object> model = new HashMap<>();

        Expediente e = serviceExpediente.buscarPorId(id);
		ExpedienteMovimiento um = serviceExpeMovimiento.getLastMovimiento(e);

		model.put("expedienteMovimiento", um);

        return new ModelAndView(new ExpedienteMovimientoPdfView(), model);
    }
	
	@RequestMapping(value = "/cancelar", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> cancelar(HttpServletRequest result, Model model, RedirectAttributes attributes ) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		try {
			boolean error = false;
			String errorStr = "";
			Expediente e = null;
			if(result.getParameter("expediente_id") != null) {
				e = serviceExpediente.buscarPorId(new Integer(result.getParameter("expediente_id")));
				if(e == null) {
					error =true;
					errorStr += "No se puede seleccionar el Expediente";
				}
			}else {
				error =true;
				errorStr += "No se puede seleccionar el Expediente";
			}
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Usuario u = null;
			if(userDetails.getUsername() != null) {
				 u = serviceUsuario.buscarPorUserName(userDetails.getUsername());
				 if(u == null) {
					 errorStr += "No se puede seleccionar el Usuario.";
					 error =true;
				 }else {
					 
					 Integer idOrgaUsuario  = u.getOrganigrama().getId();
					  
					 
					 if(!serviceExpeMovimiento.isLastMovimientoServicioUsuario(e,idOrgaUsuario)) {
						 
						errorStr += "El expediente no se encuentra en el servicio del Usuario.";
						error =true;
					 }
				 }
			}else {
				error =true;
				errorStr += "No se puede seleccionar el Usuario.";
			}
			
			ExpedienteMovimiento um = serviceExpeMovimiento.getLastMovimiento(e);
			ExpedienteMovimiento ma = null;
			if(um != null) {
				
				if(um.getUsuario().getId().compareTo(u.getId()) == 0) {
					ma = serviceExpeMovimiento.getMovimientoAnterior(um);
					
					if(ma == null) {
						error =true;
						errorStr += "No se puede seleccionar el Movimiento Anterior.";
					}
				}else {
					error =true;
					errorStr += "No se puede cancelar porque el movimiento no fue realizado por este Usuario.";
					
				}
				
			}else {
				error =true;
				errorStr += "No se puede seleccionar el Movimiento.";
			}
			
			if(!error) {
				um.setCancelado(true);
				serviceExpeMovimiento.guardar(um);
				
				ma.setFecha_salida(null);
				serviceExpeMovimiento.guardar(ma);
				r.put("success", true);
			}else {
				r.put("error", errorStr);
			}
			
			
			
			return r;
			
			
			
		}catch (Exception e) {
			r.put("error", "Error no se puede cancelar el movimiento. "+e.toString());
			return r;
		}
		
	}
	
	@RequestMapping(value = "/asignaramiservicio", method = RequestMethod.GET , produces = "application/json")
	public @ResponseBody Map<String, Object> asignarAMiServicio(HttpServletRequest result, Model model, RedirectAttributes attributes ) {
		
		Map<String, Object> r = new HashMap<String, Object>();
		
		try {
			boolean error = false;
			String errorStr = "";
			Expediente e = null;
			if(result.getParameter("expediente_id") != null) {
				e = serviceExpediente.buscarPorId(new Integer(result.getParameter("expediente_id")));
				if(e == null) {
					error =true;
					errorStr += "No se puede seleccionar el Expediente";
				}
			}else {
				error =true;
				errorStr += "No se puede seleccionar el Expediente";
			}
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Usuario u = null;
			
			if(userDetails.getUsername() != null) {
				 u = serviceUsuario.buscarPorUserName(userDetails.getUsername());
				 if(u == null) {
					 errorStr += "No se puede seleccionar el Usuario.";
					 error =true;
				 }else {
					 
					 if(u.getOrganigrama() == null) {
						errorStr += "No se puede determinar el Servicio del Usuario.";
						error =true;
					 }
					 
					  
				 }
			}else {
				error =true;
				errorStr += "No se puede seleccionar el Usuario.";
			}
			
			
			
			if(!error) {
				ExpedienteMovimiento em = new ExpedienteMovimiento();
				
				em.setUsuario(u);
				em.setOrganigrama(u.getOrganigrama());
				em.setExpediente(e);
				em.setDescripcion("AUTOASIGNACION");
				em.setFecha_llegada(new Date());
				em.setCancelado(false);
				
				serviceExpeMovimiento.guardar(em);
				
				ExpedienteMovimiento ma = serviceExpeMovimiento.getMovimientoAnterior(em); //ExpedienteMovimiento.getMovimientoAnterior(f);
				if(ma != null){
					ma.setFecha_salida(new Date());
					serviceExpeMovimiento.guardar(ma);
				}
				
				 
				r.put("success", true);
			}else {
				r.put("error", errorStr);
			}
			
			
			
			return r;
			
			
			
		}catch (Exception e) {
			r.put("error", "Error no se puede cancelar el movimiento. "+e.toString());
			return r;
		}
		
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ModelAndView guardar(HttpServletRequest result, Model model, RedirectAttributes attributes ) {	
		
		ModelAndView modelAndView = new ModelAndView("expediente/modal/pasarOtroServicio");
	     
		try {
			List<Integer> ids = new ArrayList<Integer>();
			
			if(result.getParameter("expediente_id") != null) {
				ids.add(new Integer(result.getParameter("expediente_id")));
			}else if(result.getParameter("check_listado[]") != null && result.getParameterValues("check_listado[]").length > 0) {
				ids = ArrayUtils.getSeleccionados(result.getParameterValues("check_listado[]"));
			}else {
				modelAndView.addObject("msgalert", "No se puede determinar el Expediente.");
				return modelAndView;
			}
			
			
			boolean error = false;
			String errorStr = "";
			Expediente e = null;
			if(result.getParameter("expediente_id") != null) {
				
				e = serviceExpediente.buscarPorId(new Integer(result.getParameter("expediente_id")));
				if(e == null) {
					error =true;
					errorStr += "No se puede seleccionar el Expediente1";
					modelAndView.addObject("msgalert", errorStr);
					return modelAndView;
				}
			}else {
				error =true;
				errorStr += "No se puede seleccionar el Expediente2";
				modelAndView.addObject("msgalert", errorStr);
				return modelAndView;
			}
			 
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Usuario u = null;
			if(userDetails.getUsername() != null) {
				 u = serviceUsuario.buscarPorUserName(userDetails.getUsername());
				 if(u == null) {
					 errorStr += "No se puede seleccionar el Usuario.";
					 error =true;
				 }else {
					 
					 Integer idOrgaUsuario  = u.getOrganigrama().getId();
					  
					 
					 if(!serviceExpeMovimiento.isLastMovimientoServicioUsuario(e,idOrgaUsuario)) {
						 
						errorStr += "El expediente no se encuentra en el servicio del Usuario.";
						error =true;
					 }
				 }
			}else {
				error =true;
				errorStr += "No se puede seleccionar el Usuario.";
			}
			
			
			
			Organigrama o = null;
			if(!result.getParameter("organigrama_id").isEmpty() && result.getParameter("organigrama_id") != null) {
				
				o = serviceOrganigrama.buscarPorId(new Integer(result.getParameter("organigrama_id")));
				if(o == null) {
					errorStr += "No se puede seleccionar el Servicio.";
					error =true;
				}
			}else {
				errorStr += "No se puede seleccionar el Servicio.";
				error =true;
			}
			
			 
			if(!error) {
				
				System.out.print("--------------------"+result.toString());
				
				ExpedienteMovimiento em = new ExpedienteMovimiento();
				
				em.setUsuario(u);
				em.setOrganigrama(o);
				em.setExpediente(e);
				em.setDescripcion(result.getParameter("descripcion"));
				em.setFecha_llegada(new Date());
				em.setCancelado(false);
				
				serviceExpeMovimiento.guardar(em);
				
				ExpedienteMovimiento ma = serviceExpeMovimiento.getMovimientoAnterior(em); //ExpedienteMovimiento.getMovimientoAnterior(f);
				if(ma != null){
					ma.setFecha_salida(new Date());
					serviceExpeMovimiento.guardar(ma);
				}
				
				
				
				modelAndView.addObject("expediente", e);
				modelAndView.addObject("msgsuccess", "Los datos del movimiento fueron guardados!");
			}else {
				modelAndView.addObject("msgalert", errorStr);
			}
				
			 
			
			
		    return modelAndView;		
		    
				 
			
		} catch (RollbackException ex) {
			System.out.print("-RollbackException-----"+ex.getMessage());
			modelAndView.addObject("msgalert", "Error no se ha podido guardar el movimiento ");
			return modelAndView;
			
		}	
		
	}
}
