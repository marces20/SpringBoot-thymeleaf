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
		
		
		/*if (result.hasErrors()){
			model.addAttribute("disabled", false);
			return "expediente/form";
		}	*/
		
		 
		
		System.out.print("1111 "+ result.getQueryString());
		//Map<String, String> 
		ModelAndView modelAndView = new ModelAndView("expediente/modal/pasarOtroServicio");
	     
		
		try {
			if(result.getParameter("id") == null) {
				boolean error = false;
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				Usuario u = null;
				if(userDetails.getUsername() != null) {
					 u = serviceUsuario.buscarPorUserName(userDetails.getUsername());
					 if(u == null) {
						 modelAndView.addObject("errorUsuario", "No se puede seleccionar el Usuario");
						 error =true;
					 }
				}else {
					error =true;
					modelAndView.addObject("errorUsuario", "No se puede seleccionar el Usuario");
				}
				
				Expediente e = null;
				if(result.getParameter("expediente_id") != null) {
					
					e = serviceExpediente.buscarPorId(new Integer(result.getParameter("expediente_id")));
					if(e == null) {
						error =true;
						modelAndView.addObject("errorExpediente", "No se puede seleccionar el Expediente");
					}
				}else {
					error =true;
					modelAndView.addObject("errorExpediente", "No se puede seleccionar el Expediente");
				}
				
				Organigrama o = null;
				if(!result.getParameter("organigrama_id").isEmpty() && result.getParameter("organigrama_id") != null) {
					
					o = serviceOrganigrama.buscarPorId(new Integer(result.getParameter("organigrama_id")));
					if(o == null) {
						modelAndView.addObject("errorOrganigrama", "No se puede seleccionar el Servicio");
						error =true;
					}
				}else {
					modelAndView.addObject("errorOrganigrama", "No se puede seleccionar el Servicio");
					error =true;
				}
				
				
				if(!error) {
					ExpedienteMovimiento em = new ExpedienteMovimiento();
					
					em.setUsuario(u);
					em.setFecha_llegada(new Date());
					em.setCancelado(false);
					em.setOrganigrama(o);
					em.setExpediente(e);
					
					serviceExpeMovimiento.guardar(em);
	 				
					modelAndView.addObject("msgsuccess", "Los datos del movimiento fueron guardados!");
				}else {
					modelAndView.addObject("msgalert", "Error no se ha podido guardar el movimiento.<br>No se puede seleccionar el Organigrama.");
				}
						
				
						
				/*
				 if(Usuario.getUsurioSesion().organigrama == null){
					flash("error", "Este usuario no tiene asignado un Servicio/Depto");
					return ok(crearExpedienteMovimiento.render(lineaForm));
				}
				if(!ExpedienteMovimiento.isLastMovimientoServicioUsuario(f.expediente_id,Usuario.getUsurioSesion().organigrama_id)) {
					flash("error", "No puede realizar el pase por que pertence a otro servicio.");
					return ok(crearExpedienteMovimiento.render(lineaForm));
				}
				
				Date ahora = new Date();
				Integer codigo = ExpedienteMovimiento.getSecuenciaExpedienteMovimientoCodigo();
				
				f.usuario_id = new Long(Usuario.getUsuarioSesion());
				f.fecha_llegada = ahora;
				f.cancelado = false;
				f.codigo = codigo;
				f.save();
				 
				 */
				
				//ExpedienteMovimiento ma = ExpedienteMovimiento.getMovimientoAnterior(f);
				//if(ma != null){
				//	ma.fecha_salida = ahora;
				//	ma.update();
				//}
				
				//expediente.setCreate_date(new Date());
				//expediente.setCreate_user(1);
			}else {
				//expediente.setWrite_date(new Date());
				//expediente.setWrite_user(write_user);
			}
			
			
		    return modelAndView;
			
			//attributes.addFlashAttribute("msg", "Los datos del expediente fueron guardados!");
			//return "expediente/modal/pasarOtroServicio";
		} catch (RollbackException ex) {
			modelAndView.addObject("msgalert", "Error no se ha podido guardar el movimiento");
			return modelAndView;
			
		}	
		
	}
}
