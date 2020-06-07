package com.bienes.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.RollbackException;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bienes.model.Estado;
import com.bienes.model.Factura;
import com.bienes.model.Pedidofondo;
import com.bienes.model.PedidofondoLinea;
import com.bienes.service.IEjercicioService;
import com.bienes.service.IEstadoService;
import com.bienes.service.IFacturaService;
import com.bienes.service.IPedidoFondoLineaService;
import com.bienes.service.IPedidofondoService;
import com.bienes.service.IUsuariosService;
import com.bienes.util.ArrayUtils;
import com.bienes.util.NumberUtils;
import com.bienes.util.PageRender;

@Controller
@RequestMapping("/pedidofondos")
public class PedidofondosController {
	
	@Autowired
	private IPedidofondoService servicePedidofondo;
	
	@Autowired
	private IPedidoFondoLineaService servicePedidofondoLinea;
	
	@Autowired
	private IUsuariosService serviceUsuario;
	
	@Autowired
	private IEjercicioService serviceEjercicio;
	
	@Autowired
	private IFacturaService serviceFactura;
	
	@Autowired
	private IEstadoService serviceEstado;
	
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
			model.addAttribute("msgalert", "Error no se ha podido guardar el pedido2. "+result.getFieldError().toString());
			model.addAttribute("disabled", false);
			return "pedidofondo/form";
		}	
		
		try {
			boolean nuevo = false;
			if(pedidofondo.getId() == null) {
				pedidofondo.setCreate_date(new Date());
				pedidofondo.setCreate_user(serviceUsuario.getUserLogged());
				pedidofondo.setEstado(serviceEstado.buscarPorId(1));
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
	
	@GetMapping("/carga/{pedidofondo_id}")
	public String carga(
									
								  HttpServletRequest request,
								  
								  Model model,
								  @PathVariable("pedidofondo_id") Integer idPedidofondo,
								  @RequestParam(defaultValue = "0") Integer page, 
								  @RequestParam(defaultValue = "50") Integer pageSize,
								  @RequestParam(required = false) String numero
							  ){
			
		
		Pedidofondo pedidofondo = servicePedidofondo.buscarPorId(idPedidofondo);		
		 
		if(pedidofondo != null) {
			Pageable pageRequest = PageRequest.of(page, pageSize);
			Page<Factura> facturas = serviceFactura.findConDeuda(numero, pageRequest);
			PageRender<Factura> pageRender = new PageRender<Factura>(request.getRequestURI()+"?"+request.getQueryString(), facturas,pageSize,page);
			
			model.addAttribute("numero", numero);
			model.addAttribute("titulo", "Listado de Facturas");
			model.addAttribute("facturas", facturas);
			model.addAttribute("idPedidofondo", idPedidofondo);
			model.addAttribute("page", pageRender);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("disabled", true);
			model.addAttribute("utils", NumberUtils.class);
		}	
		
		return "pedidofondo/carga";
	}
	
	@GetMapping("/cambiarEstado/{idEstado}/{id}")
	public String cambiarEstado(HttpServletRequest request,
						Model model,
						
						@PathVariable("idEstado") Integer idEstado,
						@PathVariable("id") Integer id,
						RedirectAttributes attributes
					   ){
			
		try {
		
		 
			if(id != null && idEstado != null) {
				Pedidofondo pedidofondo = servicePedidofondo.buscarPorId(id);
				Estado estado = serviceEstado.buscarPorId(idEstado);
				if(pedidofondo != null && estado != null) {
					pedidofondo.setEstado(estado);
					servicePedidofondo.guardar(pedidofondo);
					attributes.addFlashAttribute("msgsuccess", "Se ha cambiado el estado");
					return "redirect:/pedidofondos/show/"+id;
				}else {
					attributes.addFlashAttribute("msgalert", "Nose pueden determinar los parametros");
					return "redirect:/pedidofondos/show/"+id; 
				}
				
			}else {
				attributes.addFlashAttribute("msgalert", "Nose pueden determinar los parametros");
				return "redirect:/pedidofondos/show/"+id; 
			}
				
		
		}catch (Exception e) {
			
			attributes.addFlashAttribute("msgalert", "Error no se puede cambiar el estado."+e);
			return "redirect:/pedidofondos/show/"+id; 
		}
		
		
	}
	
	@GetMapping("/modalCargarPedido")
	public String modalCargarPedido(@ModelAttribute PedidofondoLinea pedidoFondoLinea,
									@RequestParam(required = true) Integer pedidofondo_id,
									@RequestParam(required = true) Integer factura_id, 
									Model model) {
		Factura f = serviceFactura.buscarPorId(factura_id);
		Pedidofondo p = servicePedidofondo.buscarPorId(pedidofondo_id);
		
		pedidoFondoLinea.setFactura(f);
		pedidoFondoLinea.setPedidofondo(p);
		return "pedidofondo/modal/cargarPedido";
	}
	
	@RequestMapping(value = "/saveLinea", method = RequestMethod.POST , produces = "application/json")
	public @ResponseBody ModelAndView guardarLinea(HttpServletRequest result,@ModelAttribute PedidofondoLinea pedidoFondoLinea, Model model, RedirectAttributes attributes ) {	
		
		ModelAndView modelAndView = new ModelAndView("pedidofondo/modal/cargarPedido");
		
		try {
			List<Integer> ids = new ArrayList<Integer>();
			
			/*if(result.getParameter("expediente_id") != null) {
				ids.add(new Integer(result.getParameter("expediente_id")));
			}else if(result.getParameter("check_listado[]") != null && result.getParameterValues("check_listado[]").length > 0) {
				ids = ArrayUtils.getSeleccionados(result.getParameterValues("check_listado[]"));
			}else {
				modelAndView.addObject("msgalert", "No se puede determinar el Expediente.");
				return modelAndView;
			}*/
			 
			
			if(pedidoFondoLinea.getId() == null) {
				pedidoFondoLinea.setCreate_date(new Date());
				pedidoFondoLinea.setCreate_user(serviceUsuario.getUserLogged());
				 
			}else {
				pedidoFondoLinea.setWrite_date(new Date());
				pedidoFondoLinea.setWrite_user(serviceUsuario.getUserLogged());
			}
			servicePedidofondoLinea.guardar(pedidoFondoLinea);
			modelAndView.addObject("msgsuccess", "El pedido fue guardado!");
			
			
			return modelAndView;
			
		} catch (RollbackException ex) {
			System.out.print("-RollbackException-----"+ex.getMessage());
			modelAndView.addObject("msgalert", "Error no se ha podido guardar el movimiento ");
			return modelAndView;
			
		}	
	}
}
