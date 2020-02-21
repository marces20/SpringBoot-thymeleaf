package com.bienes.controller;

import java.util.Date;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	 
	@RequestMapping(value = "/save", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ModelAndView guardar(HttpServletRequest result, Model model, RedirectAttributes attributes ) {	
		
		ModelAndView modelAndView = new ModelAndView("expediente/modal/pasarOtroServicio");
	     
		try {
			if(result.getParameter("id") == null) {
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
				
				System.out.print("2222222222222"+e.getId());
				
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
				
				System.out.print("33333333333333333----"+e.getId());
				if(!error) {
					
					System.out.print("--------------------"+e.getId());
					
					ExpedienteMovimiento em = new ExpedienteMovimiento();
					
					em.setUsuario(u);
					em.setOrganigrama(o);
					em.setExpediente(e);
					
					em.setFecha_llegada(new Date());
					em.setCancelado(false);
					
					serviceExpeMovimiento.guardar(em);
					
					ExpedienteMovimiento ma = serviceExpeMovimiento.getMovimientoAnterior(em); //ExpedienteMovimiento.getMovimientoAnterior(f);
					if(ma != null){
						ma.setFecha_salida(new Date());
						serviceExpeMovimiento.guardar(ma);
					}
					
					
					
	 				
					modelAndView.addObject("msgsuccess", "Los datos del movimiento fueron guardados!");
				}else {
					modelAndView.addObject("msgalert", errorStr);
				}
						
				
				 
			}
			
			
		    return modelAndView;
			
		} catch (RollbackException ex) {
			System.out.print("-RollbackException-----"+ex.getMessage());
			modelAndView.addObject("msgalert", "Error no se ha podido guardar el movimiento ");
			return modelAndView;
			
		}	
		
	}
}
